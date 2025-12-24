package org.model;

public record Edge(int u, int v, double weight) implements Comparable<Edge> {

    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.weight, other.weight);
    }
}
