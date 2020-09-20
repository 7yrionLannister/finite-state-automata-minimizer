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

    /** The vertices of the graph compose the set of states
     */
    public Automaton(Q initialState) {
        super(true);
        stimuliSet = new HashSet<>();
        responsesSet = new HashSet<>();
        f = new HashMap<>();
        q0 = initialState;
        insertVertex(q0);
    }
    public Automaton(){
        super(true);
    }


    /** It allows you to insert a transition given the ends of the transition and a stimulus
     * @param src The source of the transition. src != null
     * @param dst The destiny or end of the transition. dst != null
     * @param stimulus The stimulus that leads to this transition. stimulus != null
     * @return A boolean representing whether or not the transition was added
     * */
    public boolean relate(Q src, Q dst, S stimulus) {
        if (!f.containsKey(src)) {
            f.put(src, new HashMap<>());
        }
        if (!f.get(src).containsKey(stimulus) && containsVertex(src) && containsVertex(dst) && stimulus != null) {
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

    /** Get the result q of F(Q,S)
     * @return The state mapped by the F function for the pair Q,S or null if there is not such mapping
     * */
    public Q stateTransitionFunction(Q current, S stimulus) {
        if (f.containsKey(current)) {
            return f.get(current).get(stimulus);
        }
        return null;
    }

    /** Get the response alphabet of the automaton
     * @return A set containing all the responses the automaton can return when it processes an input string
     * */
    public HashSet<R> getResponsesSet() {
        return responsesSet;
    }

    /** Get the stimuli alphabet of the automaton
     * @return A set containing all the stimuli associated with the transitions of the automaton
     * */
    public HashSet<S> getStimuliSet() {
        return stimuliSet;
    }

    /** Get the initial state q0 of the automaton, that is, the state where any input starts to be processed
     * @return The initial state
     * */
    public Q getInitialState() {
        return q0;
    }

    @Override
    public boolean isEmpty() {
        // an automaton has at least the initial state, therefore it is never empty
        return false;
    }

    /** Minimize the automaton without modifying it and return the result
     * @return  An equivalent automaton without unnecessary states and transitions
     * */
    public abstract Automaton<Q, S, R> minimize();
}
