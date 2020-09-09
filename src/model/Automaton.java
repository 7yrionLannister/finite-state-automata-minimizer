package model;

import dataStructures.AdjacencyListGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public abstract class Automaton<Q, S, R> extends AdjacencyListGraph<Q> implements IAutomaton<Q, S, R> {
    private ArrayList<Q> statesSet;
    private ArrayList<S> stimuliSet;
    private ArrayList<R> responsesSet;
    private HashMap<PairQS, Q> f;
    private Q currentState;

    public Automaton() {
        super(true);
    }

    public Q stateTransitionFunction(Q current, S stimulus) {
        return f.get(new PairQS(current, stimulus));
    }

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
