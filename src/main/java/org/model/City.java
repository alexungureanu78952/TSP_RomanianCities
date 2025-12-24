package org.model;

public class City {
    private final int id;
    private final String name;
    private final Point location;

    public City(int id, String name, Point location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public String getName() { return name; }
    public Point getLocation() { return location; }

    @Override
    public String toString() { return name; }
}
