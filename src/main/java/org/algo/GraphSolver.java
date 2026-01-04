package org.algo;

import org.model.Edge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphSolver {
    private final int nodeCount;
    private final double[][] distMatrix;
    private final List<Edge> mstEdges = new ArrayList<>();
    private final List<Integer> tspPath = new ArrayList<>();
    private boolean fwExecuted = false;
    private boolean mstExecuted = false;

    public GraphSolver(int n, List<Edge> initialEdges) {
        this.nodeCount = n;
        this.distMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j)
                    distMatrix[i][j] = 0;
                else
                    distMatrix[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        for (Edge e : initialEdges) {
            distMatrix[e.u()][e.v()] = e.weight();
            distMatrix[e.v()][e.u()] = e.weight();
        }
    }

    public void runFloydWarshall() {
        if (fwExecuted)
            return;
        for (int k = 0; k < nodeCount; k++) {
            for (int i = 0; i < nodeCount; i++) {
                for (int j = 0; j < nodeCount; j++) {
                    if (distMatrix[i][k] + distMatrix[k][j] < distMatrix[i][j]) {
                        distMatrix[i][j] = distMatrix[i][k] + distMatrix[k][j];
                    }
                }
            }
        }
        fwExecuted = true;
    }

    public List<Edge> getKnEdges() {
        if (!fwExecuted)
            runFloydWarshall();
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < nodeCount; i++) {
            for (int j = i + 1; j < nodeCount; j++) {
                if (distMatrix[i][j] != Double.POSITIVE_INFINITY) {
                    edges.add(new Edge(i, j, distMatrix[i][j]));
                }
            }
        }
        return edges;
    }

    public void runKruskal() {
        if (mstExecuted)
            return;
        if (!fwExecuted)
            runFloydWarshall();

        List<Edge> allEdges = getKnEdges();
        Collections.sort(allEdges);
        DisjointSet ds = new DisjointSet(nodeCount);
        mstEdges.clear();
        for (Edge e : allEdges) {
            if (ds.find(e.u()) != ds.find(e.v())) {
                mstEdges.add(e);
                ds.union(e.u(), e.v());
            }
        }
        mstExecuted = true;
    }

    public List<Edge> getMstEdges() {
        return mstEdges;
    }

    public void runTSPApprox() {
        if (!mstExecuted)
            runKruskal();

        List<List<Integer>> adj = new ArrayList<>(nodeCount);
        for (int i = 0; i < nodeCount; i++)
            adj.add(new ArrayList<>());
        for (Edge e : mstEdges) {
            adj.get(e.u()).add(e.v());
            adj.get(e.v()).add(e.u());
        }

        tspPath.clear();
        boolean[] visited = new boolean[nodeCount];
        if (nodeCount > 0) {
            dfs(0, adj, visited);
            tspPath.add(0);
        }
    }

    private void dfs(int u, List<List<Integer>> adj, boolean[] visited) {
        visited[u] = true;
        tspPath.add(u);
        for (int v : adj.get(u)) {
            if (!visited[v]) {
                dfs(v, adj, visited);
            }
        }
    }

    public List<Integer> getTspPath() {
        return tspPath;
    }
}
