package model;

import dataStructures.Vertex;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    private void setupStageGraphWithIsolatedVertices() {
        for (int i = 0; i < 8; i++) {
            mealyMachine.insertState((char) (i + 1));
        }
    }

    @Test
    public void insertAndSearchStateTest() {
        setupStage1();
        assertFalse(mealyMachine.isEmpty(), "Automata are never empty");
        stateInsertionLoop();
    }

    @Test
    public void relateStatesTest() {
        setupStage1();
        setupStageGraphWithIsolatedVertices();

        char src = 1;
        char dst = 2;
        boolean stimulus = false;
        mealyMachine.relate(src, dst, stimulus);

        src = 2;
        dst = 1;
        stimulus = true;
        mealyMachine.relate(src, dst, stimulus);

        src = 2;
        dst = 3;
        stimulus = false;
        mealyMachine.relate(src, dst, stimulus);

        src = 2;
        dst = 7;
        stimulus = true;
        mealyMachine.relate(src, dst, stimulus);

        src = 2;
        dst = 8;
        stimulus = false;
        mealyMachine.relate(src, dst, stimulus);

        src = 8;
        dst = 3;
        stimulus = true;
        mealyMachine.relate(src, dst, stimulus);

        src = 8;
        dst = 5;
        stimulus = false;
        mealyMachine.relate(src, dst, stimulus);

        src = 5;
        dst = 8;
        stimulus = true;
        mealyMachine.relate(src, dst, stimulus);

        src = 6;
        dst = 1;
        stimulus = false;
        mealyMachine.relate(src, dst, stimulus);
    }

    private void stateInsertionLoop() {
        int stateCount = 1; //just has the initial state
        for (int i = 0; i < 50; i++) {
            char r = (char) (Math.random() * 100);
            if (mealyMachine.insertState(r)) {
                stateCount++;
            }
            assertTrue(mealyMachine.containsVertex(r), "The vertex with key " + r + " must have been found as it was added either in a previous iteration of the for loop or in this iteration");
            assertTrue(mealyMachine.getOrder() == stateCount, "The order is not the expected");
            assertTrue(mealyMachine.getAdjacent(r).isEmpty(), "The vertex just added should not have any edges");
            assertFalse(mealyMachine.isEmpty(), "Graph must not be empty after insertion");
        }
        assertFalse(mealyMachine.containsVertex((char) 200), "No vertex with key 200 was added so it should not have been found");
    }

    @Test
    public void minimizeTest() {
        setupStageM1();
        mealyMachine = mealyMachine.minimize();
        //assertEquals(4, mealyMachine.getOrder(), "The number of states of the minimized machine is incorrect");
        char initialState = mealyMachine.getInitialState();
        mealyMachine.BFS(initialState); // It allows to know whether or not there are inaccessible states
        ArrayList<Character> states = mealyMachine.getVertices();
        for (int i = 0; i < states.size(); i++) {
            char state = states.get(i);
            assertTrue(mealyMachine.getVertexColor(state) == Vertex.Color.BLACK, "There should not be any inaccessible state starting processing from the initial state");
        }

        System.out.println(mealyMachine.stateTransitionFunction('A', false));
        System.out.println(mealyMachine.getResponse('A', false));
        System.out.println(mealyMachine.stateTransitionFunction('A', true));
        System.out.println(mealyMachine.getResponse('A', true));
        System.out.println(mealyMachine.getOrder());
        System.out.println(mealyMachine.getStimuliSet());
        System.out.println(mealyMachine.getResponsesSet());
        System.out.println(mealyMachine.getVertices());
    }
}
