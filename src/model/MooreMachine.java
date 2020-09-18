package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
        ArrayList<ArrayList<Q>> previousPartitions = null;
        ArrayList<ArrayList<Q>> originPartitions = new ArrayList<>();
        ArrayList<Q> statesSet = getVertices();
        int index = 0;
        HashMap<R, Integer> responseToIndex = new HashMap<>();
        for (Q q : statesSet) {
            R r = getResponse(q);
            if (responseToIndex.containsKey(r)) {

            } else {
                responseToIndex.put(r, index);
                originPartitions.add(new ArrayList<>());
                index++;
            }
            originPartitions.get(responseToIndex.get(r)).add(q);
        }
        //for(int i = 0) partition everything
        boolean previousEqualsCurrent = false;
        while (!previousEqualsCurrent) {
            previousPartitions = (ArrayList<ArrayList<Q>>) originPartitions.clone();
            HashMap<S, HashMap<Integer, HashMap<Integer, ArrayList<Q>>>> originPartitioned = new HashMap<>(); //for each stimulus there is a version of the partition

            //TODO partition with every stimulus and see if at least one change, if not then it's done
            for (S s : getStimuliSet()) {
                originPartitioned.put(s, new HashMap<>());
                for (int i = 0; i < originPartitions.size(); i++) {
                    originPartitioned.get(s).put(i, refineSubset(originPartitions, i, s));
                }
                //TODO join the subset as every hashmap that refine() returns has a part of it
            }
            previousEqualsCurrent = true; //TODO this is just to compile, change it for the real condition later (compare the previous partition with the current one)
        }
        //TODO complete and delete prints
        for (int i = 0; i < originPartitions.size(); i++) {
            String line = "";
            for (int j = 0; j < originPartitions.get(i).size(); j++) {
                line += originPartitions.get(i).get(j) + " ";
            }
            System.out.println(line);
        }
        //MooreMachine<Q, S, R> minimized = new MooreMachine<>();
        return this;
    }

    private HashMap<Integer, ArrayList<Q>> refineSubset(ArrayList<ArrayList<Q>> originalPartition, int targetSubset, S s) {

        ArrayList<Q> targetPartition = originalPartition.get(targetSubset);
        HashMap<Q, Integer> stateToIndexInOrigin = new HashMap<>();
        HashMap<Integer, ArrayList<Q>> refinedPartition = new HashMap<>();

        for (int i = 0; i < targetPartition.size(); i++) {
            Q src = targetPartition.get(i);
            Q dst = stateTransitionFunction(src, s);
            boolean found = false;
            int j = 0;
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
