package model;

import dataStructures.AdjacencyListGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Automaton<Q, S, R> extends AdjacencyListGraph<Q> {
    /**
     * The stimuliSet represents the set of stimuli the automaton recognizes
     */
    private final HashSet<S> stimuliSet;
    /**
     * The responsesSet represents the set of the responses the automaton can retrieve
     */
    private final HashSet<R> responsesSet;
    /**
     * f is the state transition function that tells where the automaton goes when in a certain state and receives a certain stimulus
     */
    private final HashMap<Q, HashMap<S, Q>> f;
    /**
     * q0 represents the initial state 0 of the automaton
     */
    private final Q q0;

    /** The method initializes the Automaton given the initial state
     * @param initialState The initial state for the automaton
     */
    public Automaton(Q initialState) {
        super(true);
        stimuliSet = new HashSet<>();
        responsesSet = new HashSet<>();
        f = new HashMap<>();
        q0 = initialState;
        insertVertex(q0);
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
    public final boolean isEmpty() {
        // an automaton has at least the initial state, therefore it is never empty
        return false;
    }

    /** Minimize the automaton without modifying it and return the result
     * @return  An equivalent automaton without unnecessary states and transitions
     * */
    public abstract Automaton<Q, S, R> minimize();

    /** Performs steps two and three of the partitioning algorithm for any automaton
     * @return The result of the partitioning algorithm; a set of refinements of the original automaton
     * */
    public ArrayList<ArrayList<Q>> stepsTwoAndThreeOfPartitioningAlgorithm(ArrayList<ArrayList<Q>> parts) {
        ArrayList<ArrayList<Q>> prevParts;
        //steps two and three
        boolean previousEqualsCurrent = false;
        while (!previousEqualsCurrent) {
            prevParts = parts;
            for (S s : getStimuliSet()) {
                //Partition the original set of partitions using stimulus s
                HashMap<Integer, HashMap<Integer, ArrayList<Q>>> originPartitioned = new HashMap<>();
                int newNumberOfPartitions = 0;
                for (int i = 0; i < parts.size(); i++) {
                    //partition the subset at index i with the selected stimulus
                    HashMap<Integer, ArrayList<Q>> refinement = refineSubset(parts, i, s);
                    originPartitioned.put(i, refinement);
                    newNumberOfPartitions += refinement.size();
                }
                if (newNumberOfPartitions > parts.size()) {
                    /* Pk was actually partitioned.
                     * Replace the previous partition with the current one and fill the new "origin" with the last resulting partition
                     * */
                    parts = new ArrayList<>();
                    for (int i = 0; i < originPartitioned.size(); i++) {
                        parts.addAll(originPartitioned.get(i).values());
                    }
                    //get out of the stimuli loop as Pk != Pk+1
                    break;
                }
            }
            //Is the partitioning algorithm done?
            previousEqualsCurrent = prevParts.size() == parts.size();
        }
        return parts;
    }

    /** The method allows to refine a particular target subset of a set of partitions taking into account a stimulus s
     * @param parts             The original partition Pk
     * @param targetSubset      The index of the subset to be refined
     * @param s                 The stimulus to be passed to the machine
     * @return A new version of the original partition that may be the same if the stimulus did not take the states to different subsets, a refinement otherwise
     */
    public HashMap<Integer, ArrayList<Q>> refineSubset(ArrayList<ArrayList<Q>> parts, int targetSubset, S s) {
        ArrayList<Q> targetPartition = parts.get(targetSubset);
        HashMap<Integer, ArrayList<Q>> refinedPartition = new HashMap<>();

        //Perform the second step in the partitioning algorithm
        for (Q src : targetPartition) {
            Q dst = stateTransitionFunction(src, s);
            boolean found = false;
            int j;
            //find the index of the subset where dst is allocated (the state the automaton ends when at state src given stimulus s to read)
            for (j = 0; j < parts.size() && !found; j++) {
                ArrayList<Q> currentPartition = parts.get(j);
                found = currentPartition.contains(dst);
            }
            if (!refinedPartition.containsKey(j)) {
                refinedPartition.put(j, new ArrayList<>());
            }
            refinedPartition.get(j).add(src);
        }
        return refinedPartition;
    }
}
