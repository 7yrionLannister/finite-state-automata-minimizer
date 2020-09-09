package model;

import dataStructures.Vertex;

public class MooreState<Q, R> extends Vertex<Q> {
    private Q state;
    private R response;

    public MooreState(Q element) {
        super(element);
    }
}
