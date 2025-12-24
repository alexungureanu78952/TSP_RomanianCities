package org.algo;

import org.model.Edge;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphSolver {
    private final int n;
    private final double[][] distMatrix;
    private final List<Edge> mstEdges = new ArrayList<>();
    private final List<Integer> tspPath = new ArrayList<>();
    private boolean fwExecuted = false;
    private boolean mstExecuted = false;

    public GraphSolver(int n, List<Edge> initialEdges) {
        this.n = n;
        this.distMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) distMatrix[i][j] = 0;
                else distMatrix[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        for (Edge e : initialEdges) {
            distMatrix[e.getU()][e.getV()] = e.getWeight();
            distMatrix[e.getV()][e.getU()] = e.getWeight();
        }
    }

    public void runFloydWarshall() {
        if (fwExecuted) return;
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distMatrix[i][k] + distMatrix[k][j] < distMatrix[i][j]) {
                        distMatrix[i][j] = distMatrix[i][k] + distMatrix[k][j];
                    }
                }
            }
        }
        fwExecuted = true;
    }
    public List<Edge> getKnEdges() {
        if (!fwExecuted) runFloydWarshall();
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (distMatrix[i][j] != Double.POSITIVE_INFINITY) {
                    edges.add(new Edge(i, j, distMatrix[i][j]));
                }
            }
        }
        return edges;
    }

    public void runKruskal() {
        if (mstExecuted) return;
        if (!fwExecuted) runFloydWarshall();
        
        List<Edge> allEdges = getKnEdges();
        Collections.sort(allEdges);
        DisjointSet ds = new DisjointSet(n);
        mstEdges.clear();
        for (Edge e : allEdges) {
            if (ds.find(e.getU()) != ds.find(e.getV())) {
                mstEdges.add(e);
                ds.union(e.getU(), e.getV());
            }
        }
        mstExecuted = true;
    }

    public List<Edge> getMstEdges() {
        return mstEdges;
    }

    public void runTSPApprox() {
        if (!mstExecuted) runKruskal();
        
        List<List<Integer>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (Edge e : mstEdges) {
            adj.get(e.getU()).add(e.getV());
            adj.get(e.getV()).add(e.getU());
        }

        tspPath.clear();
        boolean[] visited = new boolean[n];
        if (n > 0) {
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
