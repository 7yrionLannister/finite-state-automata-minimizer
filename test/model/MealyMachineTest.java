package model;

import dataStructures.Vertex;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MealyMachineTest {
    private MealyMachine<Character, Boolean, Character> mealyMachine;

    private void setupStageM1() {
        mealyMachine = new MealyMachine<>('A');
        mealyMachine.insertState('B');
        mealyMachine.insertState('C');
        mealyMachine.insertState('D');
        mealyMachine.insertState('E');
        mealyMachine.insertState('F');
        mealyMachine.insertState('G');
        mealyMachine.insertState('H');

        mealyMachine.relate('A', 'A', false, 'a');
        mealyMachine.relate('A', 'B', true, 'b');
        mealyMachine.relate('B', 'B', false, 'a');
        mealyMachine.relate('B', 'C', true, 'b');
        mealyMachine.relate('C', 'C', false, 'a');
        mealyMachine.relate('C', 'D', true, 'b');
        mealyMachine.relate('D', 'D', false, 'a');
        mealyMachine.relate('D', 'E', true, 'c');
        mealyMachine.relate('E', 'E', false, 'a');
        mealyMachine.relate('E', 'F', true, 'b');
        mealyMachine.relate('F', 'F', false, 'a');
        mealyMachine.relate('F', 'G', true, 'b');
        mealyMachine.relate('G', 'G', false, 'a');
        mealyMachine.relate('G', 'H', true, 'b');
        mealyMachine.relate('H', 'H', false, 'a');
        mealyMachine.relate('H', 'A', true, 'c');
    }

    private void setupStage1() {
        mealyMachine = new MealyMachine<>('A');
        mealyMachine = mealyMachine.minimize();
    }

    @Test
    public void insertAndSearchStateTest() {
        setupStage1();
        assertFalse(mealyMachine.isEmpty(), "Automata are never empty");
    }

    @Test
    public void minimizeTest() {
        setupStageM1();
        mealyMachine = mealyMachine.minimize();
        assertEquals(4, mealyMachine.getOrder(), "The number of states of the minimized machine is incorrect");
        char initialState = mealyMachine.getInitialState();
        mealyMachine.BFS(initialState); // It allows to know whether or not there are inaccessible states
        ArrayList<Character> states = mealyMachine.getVertices();
        for (char state : states) {
            assertSame(mealyMachine.getVertexColor(state), Vertex.Color.BLACK, "There should not be any inaccessible state starting processing from the initial state");
        }
    }
}
