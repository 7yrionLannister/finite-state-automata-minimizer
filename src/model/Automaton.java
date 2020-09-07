package model;

import java.util.ArrayList;

public class Automaton<Q, S, R> implements IAutomaton<Q, S, R> {
    private ArrayList<Q> statesSet;
    private ArrayList<S> stimuliSet;
    private ArrayList<R> responsesSet;

    @Override
    public IAutomaton minimize() {
        return null;
    }
}
