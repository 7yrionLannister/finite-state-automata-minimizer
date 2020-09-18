package model;

import dataStructures.AdjacencyListGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Automaton<Q, S, R> extends AdjacencyListGraph<Q> {
    private HashSet<S> stimuliSet;
    private HashSet<R> responsesSet;
    private HashMap<PairQS, Q> f;
    private Q q0;
    private Q currentState;

    /**The vertices of the graph compose the set of states
     * */
    public Automaton(Q initialState) {
        super(true);
        stimuliSet = new HashSet<>();
        responsesSet = new HashSet<>();
        f = new HashMap<>();
        q0 = initialState;
        insertVertex(q0);
    }

    public boolean relate(Q src, Q dst, S stimulus) {
        PairQS fi = new PairQS(src, stimulus);
        if(!f.containsKey(fi) && containsVertex(src) && containsVertex(dst)) {
            super.link(src, dst, 1);
            f.put(fi, dst);
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
        return f.get(new PairQS(current, stimulus));
    }

    public HashSet<R> getResponsesSet() {
        return responsesSet;
    }
    public  HashSet<S> getStimuliSet() {
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

    public class PairQS {
        private Q state;
        private S stimulus;
        public PairQS(Q q, S s) {
            if(q == null || s == null) {
                throw new NullPointerException();
            }
            state = q;
            stimulus = s;
        }
        public Q getState() {
            return  state;
        }
        public S getStimulus() {
            return stimulus;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PairQS pairQS = (PairQS) o;
            return state.equals(pairQS.state) &&
                    stimulus.equals(pairQS.stimulus);
        }
    }
}
