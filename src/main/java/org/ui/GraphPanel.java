package org.ui;

import org.model.City;
import org.model.Edge;
import org.model.Point;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GraphPanel extends JPanel {
    private final List<City> cities;
    private List<Edge> edges;
    private List<Integer> tspPath;
    private final int padding = 60;

    public GraphPanel(List<City> cities) {
        this.cities = cities;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
        repaint();
    }

    public void setTspPath(List<Integer> tspPath) {
        this.tspPath = tspPath;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cities == null || cities.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

        for (City c : cities) {
            minX = Math.min(minX, c.getLocation().x());
            maxX = Math.max(maxX, c.getLocation().x());
            minY = Math.min(minY, c.getLocation().y());
            maxY = Math.max(maxY, c.getLocation().y());
        }

        int w = getWidth() - 2 * padding;
        int h = getHeight() - 2 * padding;
        
        double dataW = maxX - minX;
        double dataH = maxY - minY;

        if (dataW == 0) dataW = 1;
        if (dataH == 0) dataH = 1;


        double scale = Math.min((double)w / dataW, (double)h / dataH);
        

        int drawnW = (int) (dataW * scale);
        int drawnH = (int) (dataH * scale);
        

        int offsetX = (getWidth() - drawnW) / 2;
        int offsetY = (getHeight() - drawnH) / 2;

        if (edges != null) {
            g2.setColor(Color.LIGHT_GRAY);
            for (Edge e : edges) {
                Point p1 = getTransformedPoint(cities.get(e.u()).getLocation(), minX, minY, scale, offsetX, offsetY);
                Point p2 = getTransformedPoint(cities.get(e.v()).getLocation(), minX, minY, scale, offsetX, offsetY);
                g2.drawLine(p1.x(), p1.y(), p2.x(), p2.y());
            }
        }


        if (tspPath != null && tspPath.size() > 1) {
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2));
            for (int i = 0; i < tspPath.size() - 1; i++) {
                Point p1 = getTransformedPoint(cities.get(tspPath.get(i)).getLocation(), minX, minY, scale, offsetX, offsetY);
                Point p2 = getTransformedPoint(cities.get(tspPath.get(i + 1)).getLocation(), minX, minY, scale, offsetX, offsetY);
                g2.drawLine(p1.x(), p1.y(), p2.x(), p2.y());
            }
        }

        for (City c : cities) {
            Point p = getTransformedPoint(c.getLocation(), minX, minY, scale, offsetX, offsetY);
            g2.setColor(Color.BLUE);
            g2.fillOval(p.x() - 5, p.y() - 5, 10, 10);
            
            g2.setColor(Color.BLACK);
            g2.drawString(c.getName(), p.x() + 8, p.y() + 5);
        }
    }

    private Point getTransformedPoint(Point original, int minX, int minY, double scale, int offsetX, int offsetY) {
        int x = offsetX + (int) ((original.x() - minX) * scale);
        int y = offsetY + (int) ((original.y() - minY) * scale);
        return new Point(x, y);
    }
}