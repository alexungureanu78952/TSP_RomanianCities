package org.model;

public class City {
    private final String name;
    private final Point location;

    public City(String name, Point location) {
        this.name = name;
        this.location = location;
    }

    public String getName() { return name; }
    public Point getLocation() { return location; }

    @Override
    public String toString() { return name; }
}
