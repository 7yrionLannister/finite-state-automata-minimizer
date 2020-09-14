package model;

import java.util.HashMap;

public class MooreMachine<Q, S, R> extends Automaton<Q, S, R> {
    private HashMap<Q, R> responses;

    public MooreMachine(Q initialState) {
        super(initialState);
        responses = new HashMap<>();
    }

    public boolean insertState(Q state, R response) {
        if(!responses.containsKey(state)) {
            responses.put(state, response);
            insertVertex(state);
            return true;
        }
        return false;
    }

    @Override
    public IAutomaton minimize() {
        return null;
    }
}
