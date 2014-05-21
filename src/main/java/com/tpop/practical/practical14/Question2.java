package com.tpop.practical.practical14;

import com.tpop.TPOPTask;
import com.tpop.TPOPTaskMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sm1334
 * Date: 28/02/14
 * Time: 15:59
 * To change this template use File | Settings | File Templates.
 */
@TPOPTaskMetadata(name = "Question 2",
        description = "Weighted Graph",
        version = 1.00D,
        practical = "Practical 14")
public class Question2 implements TPOPTask {

	private WeightedGraph graph;
	private List<String> results = new ArrayList<>();

    public void run() {
        graph = new WeightedGraph(2);
        //THESE AREN'T CORRECT
        City london = new City("London", "UK", "Greater London", "GMT", 51.507222D, -0.1275D);
        City york = new City("York", "UK", "North Yorkshire", "GMT", 10, -0.1275D);
        City manchester = new City("Manchester", "UK", "Greater Manchester", "GMT", 5.507222D, -0.275D);
        City paris = new City("Paris", "France", " Île-de-France", "GMT+1", 2.3232D, -3434.563D);
        City new_york = new City("New York City", "United States", "New York", "GMT-5", 23.3213D, 323.32465D);
        graph.addVertex(london);
        graph.addVertex(york);
        graph.addVertex(manchester);
        graph.addVertex(paris);
        graph.addVertex(new_york);
        graph.addEdge(london, manchester, 200);
        graph.addEdge(london, york, 300);
        graph.addEdge(york, manchester, 100);
        graph.addEdge(york, new_york, 10000);
        graph.addEdge(york, paris, 1000);
        graph.addEdge(london, paris, 400);
        graph.addEdge(london, new_york, 30000);
        graph.removeEdge(london, new_york);
        results.add(String.valueOf(graph.getDegree(london)));
        results.add(String.valueOf(graph.getSize()));
        results.add(String.valueOf(graph));
        graph.removeVertex(york);
		results.add(String.valueOf(graph.getSize()));
		results.add(String.valueOf(graph));
    }

	public String[] results() {
		return results.toArray(new String[results.size()]);
	}
}
