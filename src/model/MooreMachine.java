package model;

import dataStructures.Vertex;

import java.util.*;

public class MooreMachine<Q, S, R> extends Automaton<Q, S, R> {
    private HashMap<Q, R> responsesH;

    public MooreMachine(Q initialState, R responseForInitialState) {
        super(initialState);
        responsesH = new HashMap<>();
        insertState(initialState, responseForInitialState);
    }

    public boolean insertState(Q state, R response) {
        if (state != null && !responsesH.containsKey(state)) {
            responsesH.put(state, response);
            insertVertex(state);
            getResponsesSet().add(response);
            return true;
        }
        return false;
    }

    public R getResponse(Q q) {
        return responsesH.get(q);
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
}
