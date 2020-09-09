package model;

public class MooreMachine<Q, S, R> extends Automaton<MooreState<Q, R>, S, R> {

    public MooreMachine(MooreState<Q, R> initialState) {
        super(initialState);
    }

    @Override
    public IAutomaton minimize() {
        return null;
    }
}
