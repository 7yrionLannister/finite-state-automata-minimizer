package model;

public class MealyMachine<Q, S, R> extends Automaton<Q, S, R> {

    public MealyMachine(Q initialState) {
        super(initialState);
    }

    @Override
    public MealyMachine<Q, S, R> minimize() {
        return null;
    }
}
