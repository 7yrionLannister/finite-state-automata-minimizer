package ui;

import dataStructures.AdjacencyListGraph;

public class DesktopTest {
    public static void main(String[] args) {
        AdjacencyListGraph<Integer> alg = new AdjacencyListGraph<>(true);
        alg.link(1,2, 2);
        System.out.println(alg.getEdgeWeight(1, 2));
        alg.link(1, 2, 3);
        System.out.println(alg.getEdgeWeight(1, 2));
    }
}
