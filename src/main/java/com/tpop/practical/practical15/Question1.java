package com.tpop.practical.practical15;

import com.tpop.TPOPTask;
import com.tpop.TPOPTaskMetadata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * User: sm1334
 * Date: 14/03/14
 */
@TPOPTaskMetadata(name = "Question 1",
        description = "Find the maximum total from top to bottom of the triangle",
        version = 1.00,
        practical = "Practical 15")
public class Question1 implements TPOPTask {


    private Vertex[] vertices;


    private static class Vertex<T extends Comparable> implements Comparable<Vertex> {
        private T data;
        private ArrayList<Edge> edges;
        private int distance;
        private Vertex previous;

        public Vertex(T data) {
            this.data = data;
            edges = new ArrayList<>();
        }

        public T getData() {
            return data;
        }

        public void setEdges(final ArrayList<Edge> edges) {
            this.edges = edges;
        }

        public List<Edge> getEdges() {
            return edges;
        }


        public void addEdge(final Edge e) {
            edges.add(e);
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getDistance() {
            return distance;
        }


        public void setPrevious(Vertex v) {
            this.previous = v;
        }

        public Vertex getPrevious() {
            return previous;
        }

        public String toString() {
            return "(" + data + ": " + Arrays.toString(edges.toArray()) + ")";
        }

        public int compareTo(Vertex o) {
            return this.data.compareTo(o.data);
        }
    }

    private static class Edge {
        private Vertex v1, v2;
        private int cost;
        private boolean directed;


        public Edge(Vertex v1, Vertex v2, boolean directed, int cost) {
            this.v1 = v1;
            this.v2 = v2;
            this.cost = cost;
            this.directed = directed;
        }

        /**
         * Undirected, unweighted
         */
        public Edge(Vertex v1, Vertex v2) {
            this(v1, v2, false, 0);
        }

        /**
         * Undirected, weighted
         */

        public Edge(Vertex v1, Vertex v2, int cost) {
            this(v1, v2, false, cost);
        }

        /**
         * Directed, unweighted
         */
        public Edge(Vertex v1, Vertex v2, boolean directed) {
            this(v1, v2, directed, 0);
        }

        public Vertex getV1() {
            return v1;
        }

        public Vertex getV2() {
            return v2;
        }

        public int getCost() {
            return cost;
        }

        public boolean isDirected() {
            return directed;
        }


        public String toString() {
            return "(" + getV1().data + "->" + getV2().data + ")";
        }

    }

    public void run() {
        try {
            vertices = parseData(getClass().getResource("/p15-data.txt").getFile());
            for (Vertex v : vertices) {
                System.out.print(v.toString());
                System.out.print(" ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static int[] parseArray(String[] data) {
        int[] d = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            d[i] = Integer.parseInt(data[i]);
        }

        return d;
    }

    private void solve() {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i].setDistance(Integer.MIN_VALUE);
            vertices[i].setPrevious(null);
        }
        vertices[0].setDistance(0);
        PriorityQueue<Vertex> queue = new PriorityQueue<>();
        queue.addAll(Arrays.asList(vertices));
        while (!queue.isEmpty()) {
            Vertex<Integer> v = queue.poll();
            if (v.getDistance() == Integer.MIN_VALUE) {
                break;
            }
            for (Object o : v.getEdges()) {
                Edge e = (Edge) o;
                int alt = v.getDistance() + ((int) e.getV2().getData() - v.getData());
                if (alt > e.getV2().getDistance()) {
                    e.getV2().setDistance(alt);
                    e.getV2().setPrevious(v);
                }
            }
        }

    }

    private Vertex[] parseData(String fileName) throws IOException {
        BufferedReader read = new BufferedReader(new FileReader(fileName));
        LinkedList<Vertex> vertices = new LinkedList<>();
        String line;
        List<Vertex> lastLine = new ArrayList<>();
        while ((line = read.readLine()) != null) {
            String[] parts = line.split(" ");
            int[] data = parseArray(parts);
            if (lastLine.isEmpty() && data.length == 1) {
                lastLine.add(new Vertex<>(data[0]));
                vertices.addAll(lastLine);
            } else {
                ArrayList<Vertex> newLastLine = new ArrayList<>();
                for (int i = 0; i < data.length; i++) {
                    int datum = data[i];
                    Vertex<Integer> vertex = new Vertex<>(datum);
                    Vertex parent = lastLine.get((int) Math.floor(i / 2.0));
                    Edge edge = new Edge(parent, vertex, true);
                    if (Double.compare(Math.ceil(i / 2.0), Math.floor(i / 2.0)) != 0 && lastLine.size() > Math.ceil(i / 2.0)) {
                        Vertex parent2 = lastLine.get((int) Math.ceil(i / 2.0));
                        Edge edge1 = new Edge(parent2, vertex, true);
                        parent2.addEdge(edge1);
                    }
                    parent.addEdge(edge);
                    //vertex.addEdge(edge);
                    newLastLine.add(vertex);
                    vertices.add(vertex);
                }
                lastLine = newLastLine;

            }
        }
        read.close();
        return vertices.toArray(new Vertex[vertices.size()]);
    }
}
