package com.tpop.practical.practical14;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: sm1334
 * Date: 28/02/14
 * Time: 16:05
 * To change this template use File | Settings | File Templates.
 */
public class WeightedGraph {
    private City[] cities;
    private int[][] edges;
    private int n;
    private int currentSize = 0;

    public WeightedGraph(int n) {
        cities = new City[n];
        edges = new int[n][n];
        this.n = n;
    }


    private int nextFreeSpace() {
        for (int i = 0; i < cities.length; i++) {
            if (cities[i] == null)
                return i;
        }
        return -1;
    }

    @Override
    public String toString() {
        String vertices = Arrays.toString(cities);
        StringBuilder edgs = new StringBuilder("[ ");
        for (int[] array : this.edges) {
            edgs.append(Arrays.toString(array)).append(" ");
        }
        return vertices + "\n" + edgs.append("]").toString();
    }

    public boolean addVertex(final City city) {
        if (getIndex(city) >= 0) {
            return false;
        }
        int index = nextFreeSpace();
        if (index >= 0) {
            cities[index] = city;
            currentSize++;
        } else {
            int oldLength = this.cities.length;
            City[] cities = new City[oldLength + n];
            System.arraycopy(this.cities, 0, cities, 0, oldLength);
            int[][] oldEdges = this.edges;
            int[][] edgesNew = new int[oldEdges.length + n][oldEdges.length + n];
            System.arraycopy(oldEdges, 0, edgesNew, 0, oldEdges.length);
            this.edges = edgesNew;
            for (int i = 0; i < oldEdges.length; i++) {
                int[] edgeArray = new int[oldEdges[i].length + n];
                System.arraycopy(oldEdges[i], 0, edgeArray, 0, oldEdges.length);
                this.edges[i] = edgeArray;
            }
            cities[oldLength] = city;
            this.cities = cities;
            ++currentSize;
        }
        return true;
    }


    public boolean addEdge(final City city, final City city2, int distance) {
        int index1 = getIndex(city);
        int index2 = getIndex(city2);
        if (index1 < 0 || index2 < 0) {
            return false;
        }
        edges[index1][index2] = distance;
        edges[index2][index1] = distance;
        return true;
    }

    public boolean removeEdge(final City city, final City city2) {
        int index1 = getIndex(city);
        int index2 = getIndex(city2);
        if (index1 < 0 || index2 < 0) {
            return false;
        }
        edges[index1][index2] = 0;
        edges[index2][index1] = 0;
        return true;
    }


    public boolean removeVertex(final City city) {
        int index = getIndex(city);
        if (index < 0) {
            return false;
        } else {
            cities[index] = null;
        }
        for (int i = 0; i < edges.length; i++) {
            if (i == index) {
                edges[index] = new int[edges[index].length];
            } else {
                for (int x = 0; x < edges[i].length; x++) {
                    if (x == index) {
                        edges[i][x] = 0;
                    }
                }
            }
        }
        currentSize--;

        reduceSpace();
        return true;
    }

    private void reduceSpace() {
        if (freeSpace() >= n) {
            //reduce space by n
            int length = cities.length;
            int newLength = length - n;
            for (int i = newLength; i < cities.length; i++) {
                if (cities[i] != null) {
                    int index = nextFreeSpace();
                    if (index >= newLength || index == -1) {
                        //something has REALLY gone wrong here
                        //java or nextFreeSpace is bleeding
                        break;
                    } else {
                        City city = cities[i];
                        //move city to the free space
                        cities[index] = city;
                        cities[i] = null;
                        //update the graph
                        for (int x = 0; x < edges.length; x++) {
                            for (int y = 0; y < edges.length; y++) {
                                if (x == i) {
                                    edges[index][y] = edges[x][y];
                                    edges[x][y] = 0;
                                } else if (y == i) {
                                    edges[x][index] = edges[x][y];
                                    edges[x][y] = 0;
                                }
                            }
                        }
                        //resize things
                        City[] newList = new City[newLength];
                        int[][] newEdges = new int[newLength][newLength];
                        System.arraycopy(cities, 0, newList, 0, newLength);
                        this.cities = newList;
                        int[][] oldEdges = edges;
                        System.arraycopy(oldEdges, 0, newEdges, 0, newLength);
                        for (int j = 0; j < newEdges.length; j++) {
                            int[] newArray = new int[newLength];
                            System.arraycopy(oldEdges[j], 0, newArray, 0, newLength);
                            newEdges[j] = newArray;
                        }
                        this.edges = newEdges;
                    }
                }
            }
        }
    }

    private int freeSpace() {
        int freeSpace = 0;
        for (int i = 0; i < cities.length; i++) {
            if (cities[i] == null) freeSpace++;
        }
        return freeSpace;
    }


    public int getDegree(final City city) {
        int index = getIndex(city);
        if (index < 0) {
            return -1;
        }
        int degree = 0;
        for (int i = 0; i < edges.length; i++) {
            for (int x = 0; x < edges[i].length; x++) {
                if (x == index && edges[i][x] != 0) {
                    degree++;
                    /*if (i == index) {
                        ++degree;
                    }   */
                }
            }
        }
        return degree;
    }

    public int getSize() {
        return currentSize;
    }


    public int getIndex(City city) {
        if (city == null) {
            return -1;
        }
        for (int i = 0; i < cities.length; i++) {
            if (city.equals(cities[i])) {
                return i;
            }
        }
        return -1;
    }


    public ArrayList<City> getNeighbours(City city) {
        int index = getIndex(city);
        if (index < 0) {
            return null;
        }
        ArrayList<City> list = new ArrayList<>();
        for (int i = 0; i < edges.length; i++) {
            for (int x = 0; x < edges[i].length; i++) {
                if (edges[i][x] != 0) {
                    if (i == index) {
                        list.add(cities[x]);
                    } else if (x == index) {
                        list.add(cities[i]);
                    }
                }
            }
        }
        return list;
    }

}
