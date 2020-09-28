package model;

import dataStructures.Vertex;

import java.util.*;

public class MooreMachine<Q, S, R> extends Automaton<Q, S, R> {
    /**
     * This is the H function of the Moore machine that tells the response for a given state
     */
    private final HashMap<Q, R> responsesH;

    /**
     * This function initializes a new Moore Machine
     * @param initialState The initial state of the machine
     * @param responseForInitialState The response for the initial state of the machine
     */
    public MooreMachine(Q initialState, R responseForInitialState) {
        super(initialState);
        responsesH = new HashMap<>();
        insertState(initialState, responseForInitialState);
    }

    /** The function records more states to the machine
     * @param state The new state for the machine
     * @param response The response associated to state
     * @return A boolean telling whether or not the state was inserted successfully
     */
    public boolean insertState(Q state, R response) {
        if (state != null && !responsesH.containsKey(state)) {
            responsesH.put(state, response);
            insertVertex(state);
            getResponsesSet().add(response);
            return true;
        }
        return false;
    }

    /**
     * The result of giving the specified state as input to the H function
     * @param q A state of the machine
     * @return The response associated with state q
     */
    public R getResponse(Q q) {
        return responsesH.get(q);
    }

    /**
     * This function minimizes moore machine
     * @return  The equivalent minimized automaton
     */
    @Override
    public MooreMachine<Q, S, R> minimize() {
        //do a BFS traversal starting from the initial state q0 in order to discard inaccessible states
        BFS(getInitialState());
        ArrayList<ArrayList<Q>> parts = partitioningAlgorithm();

        /* Create the minimized automaton and return it
         * newState is the new label for the state represented by a partition
         * */
        Q newState = parts.get(0).get(0);
        MooreMachine<Q, S, R> minimized = new MooreMachine<>(newState, getResponse(newState));
        //insert states
        for (int i = 1; i < parts.size(); i++) {
            newState = parts.get(i).get(0);
            minimized.insertState(newState, getResponse(newState));
        }
        //relate states
        for (Q src : minimized.getVertices()) {
            for (S s : getStimuliSet()) {
                Q dst = stateTransitionFunction(src, s);
                boolean related = false;
                for (int i = 0; i < parts.size() && !related; i++) {
                    ArrayList<Q> p = parts.get(i);
                    if (p.contains(dst)) {
                        minimized.relate(src, p.get(0), s);
                        related = true;
                    }
                }
            }
        }
        return minimized;
    }

    /** Perform the partitioning algorithm version for a Moore machine
     * @return The result of the partitioning algorithm; a set of refinements of the original automaton
     * */
    private ArrayList<ArrayList<Q>> partitioningAlgorithm() {
        ArrayList<ArrayList<Q>> parts = stepOneOfPartitioningAlgorithm();
        return super.stepsTwoAndThreeOfPartitioningAlgorithm(parts);
    }

    /** The function performs the step one in the partitioning algorithm for a Moore machine ignoring inaccessible(WHITE) states
     * @return A list with the initial partition P1 of the automaton
     */
    private ArrayList<ArrayList<Q>> stepOneOfPartitioningAlgorithm() {
        ArrayList<ArrayList<Q>> parts = new ArrayList<>();
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
                parts.add(new ArrayList<>());
                index++;
            }
            parts.get(responseToIndex.get(r)).add(q);
        }
        return parts;
    }
}
