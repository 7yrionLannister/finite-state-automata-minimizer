package model;

import dataStructures.AdjacencyListGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Automaton<Q, S, R> extends AdjacencyListGraph<Q> {
    /**
     * The stimuliSet represents the set of stimuli of the automaton
     */
    private HashSet<S> stimuliSet;
    /**
     * The responsesSet represents the set of the responses of the automaton
     */
    private HashSet<R> responsesSet;
    /**
     * The f represents a transition function
     */
    private HashMap<Q, HashMap<S, Q>> f;
    /**
     * The q0 represents the initial state 0 of the automata
     */
    private Q q0;
    /**
     * The currentState represents the
     */
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

    /** Perform the partitioning algorithm version for a Moore machine
     * @return The result of the partitioning algorithm; a set of refinements of the original automaton
     * */
    public ArrayList<ArrayList<Q>> stepsTwoAndThreeOfPartitioningAlgorithm(ArrayList<ArrayList<Q>> originPartitions) {
        ArrayList<ArrayList<Q>> previousPartitions = null;
        //steps two and three
        boolean previousEqualsCurrent = false;
        while (!previousEqualsCurrent) {
            previousPartitions = originPartitions;
            for (S s : getStimuliSet()) {
                //Partition the original set of partitions using stimulus s
                HashMap<Integer, HashMap<Integer, ArrayList<Q>>> originPartitioned = new HashMap<>();
                int newNumberOfPartitions = 0;
                for (int i = 0; i < originPartitions.size(); i++) {
                    //partition the subset at index i with the selected stimulus
                    HashMap<Integer, ArrayList<Q>> refinement = refineSubset(originPartitions, i, s);
                    originPartitioned.put(i, refinement);
                    newNumberOfPartitions += refinement.size();
                }
                if (newNumberOfPartitions > originPartitions.size()) {
                    /* Pk was actually partitioned.
                     * Replace the previous partition with the current one and fill the new "origin" with the last resulting partition
                     * */
                    originPartitions = new ArrayList<>();
                    for (int i = 0; i < originPartitioned.size(); i++) {
                        for (ArrayList<Q> parts : originPartitioned.get(i).values()) {
                            originPartitions.add(parts);
                        }
                    }
                    //get out of the stimuli loop as Pk != Pk+1
                    break;
                }
            }
            //Is the partitioning algorithm done?
            previousEqualsCurrent = previousPartitions.size() == originPartitions.size();
        }
        return originPartitions;
    }

    /**
     * The method allows to refine a particular target subset of a set of partitions taking into account a stimulus s
     *
     * @param originalPartition The original partition Pk
     * @param targetSubset      The index of the subset to be refined
     * @param s                 The stimulus to be passed to the machine
     * @return A new version of the original partition that may be the same if the stimulus did not take the states to different subsets, a refinement otherwise
     */
    public HashMap<Integer, ArrayList<Q>> refineSubset(ArrayList<ArrayList<Q>> originalPartition, int targetSubset, S s) {
        ArrayList<Q> targetPartition = originalPartition.get(targetSubset);
        HashMap<Q, Integer> stateToIndexInOrigin = new HashMap<>();
        HashMap<Integer, ArrayList<Q>> refinedPartition = new HashMap<>();

        //Perform the second step in the partitioning algorithm
        for (int i = 0; i < targetPartition.size(); i++) {
            Q src = targetPartition.get(i);
            Q dst = stateTransitionFunction(src, s);
            boolean found = false;
            int j = 0;
            //find the index of the subset where dst is allocated (the state the automaton ends when at state src given stimulus s to read)
            for (j = 0; j < originalPartition.size() && !found; j++) {
                ArrayList<Q> currentPartition = originalPartition.get(j);
                found = currentPartition.contains(dst);
            }
            stateToIndexInOrigin.put(src, j);
            if (!refinedPartition.containsKey(j)) {
                refinedPartition.put(j, new ArrayList<>());
            }
            refinedPartition.get(j).add(src);
        }
        return refinedPartition;
    }
}
