package org;

import org.algo.GraphSolver;
import org.io.CityLoader;
import org.model.City;
import org.ui.GraphPanel;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.List;

public class Main extends JFrame {

    public Main() {
        setTitle("TSP Romania");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            CityLoader loader = new CityLoader();
            InputStream is = getClass().getResourceAsStream("/cities.txt");
            if (is == null) throw new RuntimeException("File not found: /cities.txt");
            loader.load(is);
            List<City> cities = loader.getCities();

            System.out.println("Loaded " + cities.size() + " cities and " + loader.getEdges().size() + " edges.");

            GraphSolver solver = new GraphSolver(cities.size(), loader.getEdges());
            GraphPanel panel = new GraphPanel(cities);

            panel.setEdges(loader.getEdges());
            add(panel, BorderLayout.CENTER);

            JPanel controlPanel = new JPanel();
            JButton btnFW = new JButton("1. Floyd-Warshall (Kn)");
            JButton btnMST = new JButton("2. MST (Kruskal)");
            JButton btnTSP = new JButton("3. TSP Approx");

            btnFW.addActionListener(e -> {
                solver.runFloydWarshall();
                panel.setEdges(solver.getKnEdges());
                panel.setTspPath(null);
                panel.repaint();
            });

            btnMST.addActionListener(e -> {
                solver.runKruskal();
                panel.setEdges(solver.getMstEdges());
                panel.setTspPath(null);
                panel.repaint();
            });

            btnTSP.addActionListener(e -> {
                solver.runTSPApprox();
                panel.setEdges(solver.getMstEdges());
                panel.setTspPath(solver.getTspPath());
                panel.repaint();

                List<Integer> path = solver.getTspPath();
                StringBuilder sb = new StringBuilder();
                sb.append("Circuit TSP Aproximativ (Preordine MST): ");
                for (int i = 0; i < path.size(); i++) {
                    sb.append(cities.get(path.get(i)).getName());
                    if (i < path.size() - 1) sb.append(" -> ");
                }
                System.out.println(sb);
            });

            controlPanel.add(btnFW);
            controlPanel.add(btnMST);
            controlPanel.add(btnTSP);
            add(controlPanel, BorderLayout.SOUTH);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}