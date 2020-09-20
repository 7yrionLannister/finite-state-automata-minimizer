package model;

import dataStructures.Vertex;

import java.util.*;

public class MooreMachine<Q, S, R> extends Automaton<Q, S, R> {
    private HashMap<Q, R> responses;

    public MooreMachine(Q initialState, R responseForInitialState) {
        super(initialState);
        responses = new HashMap<>();
        insertState(initialState, responseForInitialState);
    }

    public boolean insertState(Q state, R response) {
        if (!responses.containsKey(state)) {
            responses.put(state, response);
            insertVertex(state);
            getResponsesSet().add(response);
            return true;
        }
        return false;
    }

    public R getResponse(Q q) {
        return responses.get(q);
    }

    @Override
    public MooreMachine<Q, S, R> minimize() {
        //do a BFS traversal starting from the initial state q0 in order to discard inaccessible states
        BFS(getInitialState());
        ArrayList<ArrayList<Q>> originPartitions = partitioningAlgorithm();

        /* Create the minimized automaton and return it
         * newState is the new label for the state represented by a partition
         * */
        Q newState = originPartitions.get(0).get(0);
        MooreMachine<Q, S, R> minimized = new MooreMachine<>(newState, getResponse(newState));
        //insert states
        for (int i = 1; i < originPartitions.size(); i++) {
            newState = originPartitions.get(i).get(0);
            minimized.insertState(newState, getResponse(newState));
        }
        //relate states
        for (Q src : minimized.getVertices()) {
            for (S s : getStimuliSet()) {
                Q dst = stateTransitionFunction(src, s);
                boolean related = false;
                for (int i = 0; i < originPartitions.size() && !related; i++) {
                    ArrayList<Q> p = originPartitions.get(i);
                    if (p.contains(dst)) {
                        minimized.relate(src, p.get(0), s);
                        related = true;
                    }
                    minimized.relate(src, dst, s);
                }
            }
        }
        return minimized;
    }

    /** Perform the partitioning algorithm version for a Moore machine
     * @return The result of the partitioning algorithm; a set of refinements of the original automaton
     * */
    private ArrayList<ArrayList<Q>> partitioningAlgorithm() {
        ArrayList<ArrayList<Q>> originPartitions = stepOneOfPartitioningAlgorithm();
        ArrayList<ArrayList<Q>> previousPartitions = null;
        //steps two and three
        boolean previousEqualsCurrent = false;
        while (!previousEqualsCurrent) {
            previousPartitions = (ArrayList<ArrayList<Q>>) originPartitions.clone();
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
                    previousPartitions = originPartitions;
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

    private ArrayList<ArrayList<Q>> stepOneOfPartitioningAlgorithm() {
        ArrayList<ArrayList<Q>> originPartitions = new ArrayList<>();
        HashMap<R, Integer> responseToIndex = new HashMap<>();
        ArrayList<Q> statesSet = getVertices();
        int index = 0;
        //set the initial partition P1
        for (Q q : statesSet) {
            if(getVertexColor(q) == Vertex.Color.WHITE) {
                //the state is inaccessible so don't process it
                continue;
            }
            R r = getResponse(q);
            if (!responseToIndex.containsKey(r)) {
                responseToIndex.put(r, index);
                originPartitions.add(new ArrayList<>());
                index++;
            }
            originPartitions.get(responseToIndex.get(r)).add(q);
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
    private HashMap<Integer, ArrayList<Q>> refineSubset(ArrayList<ArrayList<Q>> originalPartition, int targetSubset, S s) {
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
                for (int k = 0; k < currentPartition.size() && !found; k++) {
                    if (currentPartition.get(k).equals(dst)) {
                        found = true;
                    }
                }
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
