package model;

import dataStructures.AdjacencyListGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Automaton<Q, S, R> extends AdjacencyListGraph<Q> {
    private HashSet<S> stimuliSet;
    private HashSet<R> responsesSet;
    private HashMap<Q, HashMap<S, Q>> f;
    private Q q0;
    private Q currentState;

    /**
     * The vertices of the graph compose the set of states
     */
    public Automaton(Q initialState) {
        super(true);
        stimuliSet = new HashSet<>();
        responsesSet = new HashSet<>();
        f = new HashMap<>();
        q0 = initialState;
        insertVertex(q0);
    }

    public boolean relate(Q src, Q dst, S stimulus) {
        if (!f.containsKey(src)) {
            f.put(src, new HashMap<>());
        }
        if (!f.get(src).containsKey(stimulus) && containsVertex(src) && containsVertex(dst)) {
            super.link(src, dst, 1);
            f.get(src).put(stimulus, dst);
            stimuliSet.add(stimulus);
            return true;
        }
        return false;
    }

    @Override
    public boolean unlink(Q src, Q dst) {
        // Operation not supported in this subclass")
        return false;
    }

    public Q stateTransitionFunction(Q current, S stimulus) {
        if (f.containsKey(current)) {
            return f.get(current).get(stimulus);
        }
        return null;
    }

    public HashSet<R> getResponsesSet() {
        return responsesSet;
    }

    public HashSet<S> getStimuliSet() {
        return stimuliSet;
    }

    public Q getInitialState() {
        return q0;
    }

    @Override
    public boolean isEmpty() {
        // an automaton has at least the initial state, therefore it is never empty
        return false;
    }

    public abstract Automaton<Q, S, R> minimize();
}
