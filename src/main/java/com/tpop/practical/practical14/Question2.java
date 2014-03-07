package com.tpop.practical.practical14;

import com.tpop.TPOPTask;
import com.tpop.TPOPTaskMetadata;

/**
 * Created with IntelliJ IDEA.
 * User: sm1334
 * Date: 28/02/14
 * Time: 15:59
 * To change this template use File | Settings | File Templates.
 */
@TPOPTaskMetadata(name = "Question 1",
        description = "Weighted Graph",
        version = 1.00D,
        practical = "Practical 14")
public class Question2 implements TPOPTask {

    public void run() {
        WeightedGraph graph = new WeightedGraph(2);
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
        System.out.println(graph.getDegree(london));
        System.out.println(graph.getSize());
        System.out.println(graph);
        graph.removeVertex(york);
        System.out.println(graph);
    }
}
