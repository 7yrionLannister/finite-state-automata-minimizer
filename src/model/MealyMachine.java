package model;

import dataStructures.Vertex;

import java.util.ArrayList;
import java.util.HashMap;

public class MealyMachine<Q, S, R> extends Automaton<Q, S, R> {
    private HashMap<Q, HashMap<S, R>> responsesG;

    public MealyMachine(Q initialState) {
        super(initialState);
        responsesG = new HashMap<>();
    }

    public boolean insertState(Q state) {
        if(state == null) {
            return false;
        }
        return insertVertex(state);
    }

    public R getResponse(Q q, S s) {
        if(responsesG.containsKey(q)) {
            return responsesG.get(q).get(s);
        }
        return null;
    }

    public boolean relate(Q src, Q dst, S stimulus, R response) {
        boolean related = super.relate(src, dst, stimulus);
        if(related) {
            if(!responsesG.containsKey(src)) {
                responsesG.put(src, new HashMap<>());
            }
            responsesG.get(src).put(stimulus, response);
            getResponsesSet().add(response);
        }
        return related;
    }

    @Override
    public MealyMachine<Q, S, R> minimize() {
        //do a BFS traversal starting from the initial state q0 in order to discard inaccessible states
        BFS(getInitialState());
        ArrayList<ArrayList<Q>> originPartitions = partitioningAlgorithm();

        /* Create the minimized automaton and return it
         * newState is the new label for the state represented by a partition
         * */
        Q newState = originPartitions.get(0).get(0);
        MealyMachine<Q, S, R> minimized = new MealyMachine<>(newState);
        //insert states
        for (int i = 1; i < originPartitions.size(); i++) {
            newState = originPartitions.get(i).get(0);
            minimized.insertState(newState);
        }
        //relate states
        for (Q src : minimized.getVertices()) {
            for (S s : getStimuliSet()) {
                Q dst = stateTransitionFunction(src, s);
                R rsp = getResponse(src, s);
                boolean related = false;
                for (int i = 0; i < originPartitions.size() && !related; i++) {
                    ArrayList<Q> p = originPartitions.get(i);
                    if (p.contains(dst)) {
                        minimized.relate(src, p.get(0), s, rsp);
                        related = true;
                    }
                }
            }
        }
        return minimized;
    }

    private ArrayList<ArrayList<Q>> partitioningAlgorithm() {
        ArrayList<ArrayList<Q>> originPartitions = stepOneOfPartitioningAlgorithm();
        return super.stepsTwoAndThreeOfPartitioningAlgorithm(originPartitions);
    }

    private ArrayList<ArrayList<Q>> stepOneOfPartitioningAlgorithm() {
        ArrayList<ArrayList<Q>> originPartitions = new ArrayList<>();
        HashMap<ArrayList<R>, Integer> responseToIndex = new HashMap<>();
        ArrayList<Q> statesSet = getVertices();
        int index = 0;
        //set the initial partition P1
        for (Q q : statesSet) {
            if(getVertexColor(q) == Vertex.Color.WHITE) {
                //the state is inaccessible so don't process it
                continue;
            }
            ArrayList<R> responsesForStateQ = new ArrayList<>();
            for(S s : getStimuliSet()) {
                responsesForStateQ.add(getResponse(q, s));
            }
            if (!responseToIndex.containsKey(responsesForStateQ)) {
                responseToIndex.put(responsesForStateQ, index);
                originPartitions.add(new ArrayList<>());
                index++;
            }
            originPartitions.get(responseToIndex.get(responsesForStateQ)).add(q);
        }
        return originPartitions;
    }
}
