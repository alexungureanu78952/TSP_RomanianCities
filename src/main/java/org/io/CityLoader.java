package org.io;

import org.model.City;
import org.model.Edge;
import org.model.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityLoader {
    private final List<City> cities = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();
    private final Map<String, Integer> nameToId = new HashMap<>();

    public void load(InputStream inputStream) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            boolean coordSection = false;
            boolean edgeSection = false;
            int cityIdCounter = 0;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.equals("NODE_COORD_SECTION")) {
                    coordSection = true;
                    edgeSection = false;
                    continue;
                }
                if (line.equals("EDGE_WEIGHT_SECTION")) {
                    coordSection = false;
                    edgeSection = true;
                    continue;
                }
                if (line.equals("EOF")) break;

                if (coordSection) {
                    String[] parts = line.split("\\s+");
                    String name = parts[0];
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    City city = new City(cityIdCounter, name, new Point(x, y));
                    cities.add(city);
                    nameToId.put(name, cityIdCounter);
                    cityIdCounter++;
                } else if (edgeSection) {
                    String[] parts = line.split("\\s+");
                    String uName = parts[0];
                    String vName = parts[1];
                    double weight = Double.parseDouble(parts[2]);
                    if (nameToId.containsKey(uName) && nameToId.containsKey(vName)) {
                        edges.add(new Edge(nameToId.get(uName), nameToId.get(vName), weight));
                    }
                }
            }
        }
    }

    public List<City> getCities() { return cities; }
    public List<Edge> getEdges() { return edges; }
}
