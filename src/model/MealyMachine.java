package model;

import dataStructures.Vertex;

import java.util.ArrayList;
import java.util.HashMap;

public class MealyMachine<Q, S, R> extends Automaton<Q, S, R> {
    /**
     * The responsesH represents the initial state of the machine and the response of the initial state
     */
    private HashMap<Q, HashMap<S, R>> responsesG;
    /**
     * This function initializes a new Mealy Machine
     * @param initialState the initial state of the machine
     */
    public MealyMachine(Q initialState) {
        super(initialState);
        responsesG = new HashMap<>();
    }
    /**
     * This function records more states to the machine
     * @param state the state of the machine
     * @return true or false if the recorded data is according to the initial conditions
     */
    public boolean insertState(Q state) {
        if(state == null) {
            return false;
        }
        return insertVertex(state);
    }
    /**
     * Returns the responses of a state
     * @param q state of the machine
     * @param s stimuli of the machine
     * @return the response of that already created state
     */
    public R getResponse(Q q, S s) {
        if(responsesG.containsKey(q)) {
            return responsesG.get(q).get(s);
        }
        return null;
    }

    /**
     * This function relates the states from the origin to the destination according to the stimuli and responses already defined
     * @param src origin of machine status
     * @param dst machine status destination
     * @param stimulus stimuli that the machine will have
     * @param response the responses of a state
     * @return true or false if the relationship was created or not
     */
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
    /**
     * This function minimizes meyaly machine
     * @return  moore machine minimized
     */
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
    /**
     * this function makes the partitioning on the automaton
     * @return moore's machine partitions list
     */
    private ArrayList<ArrayList<Q>> partitioningAlgorithm() {
        ArrayList<ArrayList<Q>> originPartitions = stepOneOfPartitioningAlgorithm();
        return super.stepsTwoAndThreeOfPartitioningAlgorithm(originPartitions);
    }
    /**
     * This function removes all states are not accessible from the initial state
     * @return a list the equivalent connected automaton
     */
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
