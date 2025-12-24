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
    private final int padding = 40;

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
            minX = Math.min(minX, c.getLocation().getX());
            maxX = Math.max(maxX, c.getLocation().getX());
            minY = Math.min(minY, c.getLocation().getY());
            maxY = Math.max(maxY, c.getLocation().getY());
        }

        int w = getWidth() - 2 * padding;
        int h = getHeight() - 2 * padding;
        double scaleX = (maxX == minX) ? 1 : (double) w / (maxX - minX);
        double scaleY = (maxY == minY) ? 1 : (double) h / (maxY - minY);

        // Draw Edges
        if (edges != null) {
            g2.setColor(Color.LIGHT_GRAY);
            for (Edge e : edges) {
                Point p1 = getScaledPoint(cities.get(e.getU()), minX, minY, scaleX, scaleY);
                Point p2 = getScaledPoint(cities.get(e.getV()), minX, minY, scaleX, scaleY);
                g2.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            }
        }

        // Draw TSP Path
        if (tspPath != null && tspPath.size() > 1) {
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2));
            for (int i = 0; i < tspPath.size() - 1; i++) {
                Point p1 = getScaledPoint(cities.get(tspPath.get(i)), minX, minY, scaleX, scaleY);
                Point p2 = getScaledPoint(cities.get(tspPath.get(i + 1)), minX, minY, scaleX, scaleY);
                g2.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            }
        }

        // Draw Nodes
        g2.setColor(Color.BLUE);
        for (City c : cities) {
            Point p = getScaledPoint(c, minX, minY, scaleX, scaleY);
            g2.fillOval(p.getX() - 5, p.getY() - 5, 10, 10);
            g2.setColor(Color.BLACK);
            g2.drawString(c.getName(), p.getX() + 8, p.getY());
            g2.setColor(Color.BLUE);
        }
    }

    private Point getScaledPoint(City c, int minX, int minY, double scaleX, double scaleY) {
        int x = padding + (int) ((c.getLocation().getX() - minX) * scaleX);
        int y = padding + (int) ((c.getLocation().getY() - minY) * scaleY);
        return new Point(x, y);
    }
}
