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
	private ArrayList<String> results = new ArrayList<>();
	private int lines;


	private static class Vertex<T extends Comparable> implements Comparable<Vertex> {
		private final T data;
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
			return Integer.valueOf(o.getDistance()).compareTo(this.getDistance());
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
			long start = System.currentTimeMillis();
			vertices = parseData(getClass().getResource("/p15-data.txt").getFile());
			solve();
			long finish = System.currentTimeMillis() - start;
			results.add("Time for q1: " + finish + " ms");
			start = System.currentTimeMillis();
			lines = 0;
			vertices = parseData(getClass().getResource("/p15-triangle.txt").getFile());
			solve();
			finish = System.currentTimeMillis() - start;
			results.add("Time for q2: " + finish + " ms");
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

	public String[] results() {
		return results.toArray(new String[results.size()]);
	}

	private void solve() {
		for (int i = 0; i < vertices.length; i++) {
			vertices[i].setDistance(Integer.MIN_VALUE);
			vertices[i].setPrevious(null);
		}
		vertices[0].setDistance(0);
		PriorityQueue<Vertex> queue = new PriorityQueue<>(Arrays.asList(vertices));
		while (!queue.isEmpty()) {
			Vertex<Integer> v = queue.poll();
			if (v.getDistance() == Integer.MIN_VALUE) {
				break;
			}
			for (Edge e : v.getEdges()) {
				int alt = v.getDistance() + ((int) e.getV2().getData() - v.getData());
				if (alt > e.getV2().getDistance()) {
					queue.remove(e.getV2());
					e.getV2().setDistance(alt);
					e.getV2().setPrevious(v);
					queue.add(e.getV2());
				}

			}
		}
		int greatest = Integer.MIN_VALUE;
		List<Vertex<Integer>> greatestPath = null;
		for (int i = vertices.length - lines; i < vertices.length; i++) {
			Vertex<Integer> v = vertices[i];
			ArrayList<Vertex<Integer>> path = new ArrayList<>(100);
			int size = 0;
			while (v != null) {
				path.add(v);
				size += v.data;
				v = v.getPrevious();
			}
			if (size > greatest) {
				greatest = size;
				Collections.reverse(path);
				greatestPath = path;
			}
		}
		if (greatestPath != null) {
			results.add(String.valueOf(greatestPath));
			results.add(String.valueOf(greatestPath.stream().map(Vertex::getData).reduce(0, (a, b) -> a + b)));
		}
	}

	private Vertex[] parseData(String fileName) throws IOException {
		BufferedReader read = new BufferedReader(new FileReader(fileName));
		ArrayList<Vertex> vertices = new ArrayList<>();
		String line = read.readLine();
		lines += 1;
		Vertex[] lastLine = new Vertex[1];
		while (true) {
			String[] parts = line.split(" ");
			int[] data = parseArray(parts);
			String line2 = read.readLine();
			if (line2 == null) {
				break;
			} else {
				lines +=1;
				line = line2;
				int[] data2 = parseArray(line2.split(" "));
				Vertex[] nextLastLine = new Vertex[data2.length];
				for (int i = 0; i < data.length; i++) {
					Vertex<Integer> v = null;
					if (lastLine[i] == null) {
						v = new Vertex<>(data[i]);
						vertices.add(v);
						lastLine[i] = v;
					} else {
						v = lastLine[i];
					}
					Vertex<Integer> v1;
					if (nextLastLine[i] != null) {
						v1 = nextLastLine[i];
					} else {
						v1 = new Vertex<>(data2[i]);
						nextLastLine[i] = v1;
						vertices.add(v1);
					}
					Vertex<Integer> v2;
					if (nextLastLine[i + 1] != null) {
						v2 = nextLastLine[i + 1];
					} else {
						v2 = new Vertex<>(data2[i + 1]);
						nextLastLine[i + 1] = v2;
						vertices.add(v2);
					}
					Edge e1 = new Edge(v, v1);
					Edge e2 = new Edge(v, v2);
					v.addEdge(e1);
					v.addEdge(e2);
				}
				lastLine = nextLastLine;
			}
		}
		read.close();
		return vertices.toArray(new Vertex[vertices.size()]);
	}
}
