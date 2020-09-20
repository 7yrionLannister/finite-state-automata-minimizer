package model;

import dataStructures.Edge;
import dataStructures.Vertex;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MooreMachineTest {
    private MooreMachine<Character, Boolean, Boolean> mooreMachine;
    private ArrayList<Edge<Integer>> removedEdges;

    private void setupStage1() {
        mooreMachine = new MooreMachine<>('A', false);
        mooreMachine = mooreMachine.minimize();
    }

    private void setupStageM1() {
        mooreMachine = new MooreMachine<>('A', false);
        mooreMachine.insertState('B', false);
        mooreMachine.insertState('C', false);
        mooreMachine.insertState('D', false);
        mooreMachine.insertState('E', false);
        mooreMachine.insertState('F', false);
        mooreMachine.insertState('G', false);
        mooreMachine.insertState('H', false);
        mooreMachine.insertState('I', true);
        mooreMachine.insertState('J', false);
        mooreMachine.insertState('K', true);

        mooreMachine.relate('A', 'B', false);
        mooreMachine.relate('A', 'A', true);
        mooreMachine.relate('B', 'C', false);
        mooreMachine.relate('B', 'D', true);
        mooreMachine.relate('C', 'E', false);
        mooreMachine.relate('C', 'C', true);
        mooreMachine.relate('D', 'F', false);
        mooreMachine.relate('D', 'B', true);
        mooreMachine.relate('E', 'G', false);
        mooreMachine.relate('E', 'E', true);
        mooreMachine.relate('F', 'H', false);
        mooreMachine.relate('F', 'F', true);
        mooreMachine.relate('G', 'I', false);
        mooreMachine.relate('G', 'G', true);
        mooreMachine.relate('H', 'J', false);
        mooreMachine.relate('H', 'H', true);
        mooreMachine.relate('I', 'A', false);
        mooreMachine.relate('I', 'K', true);
        mooreMachine.relate('J', 'K', false);
        mooreMachine.relate('J', 'J', true);
        mooreMachine.relate('K', 'A', false);
        mooreMachine.relate('K', 'K', true);
    }

    private void setupStageM2() {
        mooreMachine = new MooreMachine<>('A', false);
        mooreMachine.insertState('B', false);
        mooreMachine.insertState('C', false);
        mooreMachine.insertState('D', false);
        mooreMachine.insertState('E', false);
        mooreMachine.insertState('F', true);

        mooreMachine.relate('A', 'B', false);
        mooreMachine.relate('A', 'A', true);
        mooreMachine.relate('B', 'C', false);
        mooreMachine.relate('B', 'B', true);
        mooreMachine.relate('C', 'D', false);
        mooreMachine.relate('C', 'C', true);
        mooreMachine.relate('D', 'E', false);
        mooreMachine.relate('D', 'D', true);
        mooreMachine.relate('E', 'F', false);
        mooreMachine.relate('E', 'E', true);
        mooreMachine.relate('F', 'B', false);
        mooreMachine.relate('F', 'F', true);
    }

    private void setupStageGraphWithIsolatedVertices() {
        for (int i = 0; i < 8; i++) {
            mooreMachine.insertState((char) (i + 1), (Math.random() * 10) % 2 == 0);
        }
    }

    @Test
    public void insertAndSearchStateTest() {
        setupStage1();
        assertFalse(mooreMachine.isEmpty(), "Automata are never empty");
        stateInsertionLoop();
    }

    @Test
    public void relateStatesTest() {
        setupStage1();
        setupStageGraphWithIsolatedVertices();

        char src = 1;
        char dst = 2;
        boolean stimulus = false;
        mooreMachine.relate(src, dst, stimulus);

        src = 2;
        dst = 1;
        stimulus = true;
        mooreMachine.relate(src, dst, stimulus);

        src = 2;
        dst = 3;
        stimulus = false;
        mooreMachine.relate(src, dst, stimulus);

        src = 2;
        dst = 7;
        stimulus = true;
        mooreMachine.relate(src, dst, stimulus);

        src = 2;
        dst = 8;
        stimulus = false;
        mooreMachine.relate(src, dst, stimulus);

        src = 8;
        dst = 3;
        stimulus = true;
        mooreMachine.relate(src, dst, stimulus);

        src = 8;
        dst = 5;
        stimulus = false;
        mooreMachine.relate(src, dst, stimulus);

        src = 5;
        dst = 8;
        stimulus = true;
        mooreMachine.relate(src, dst, stimulus);

        src = 6;
        dst = 1;
        stimulus = false;
        mooreMachine.relate(src, dst, stimulus);
    }

    private void stateInsertionLoop() {
        int stateCount = 1; //just has the initial state
        for (int i = 0; i < 50; i++) {
            char r = (char) (Math.random() * 100);
            if (mooreMachine.insertState(r, ((int) (Math.random() * 100)) % 2 == 0)) {
                stateCount++;
            }
            assertTrue(mooreMachine.containsVertex(r), "The vertex with key " + r + " must have been found as it was added either in a previous iteration of the for loop or in this iteration");
            assertTrue(mooreMachine.getOrder() == stateCount, "The order is not the expected");
            assertTrue(mooreMachine.getAdjacent(r).isEmpty(), "The vertex just added should not have any edges");
            assertFalse(mooreMachine.isEmpty(), "Graph must not be empty after insertion");
        }
        assertFalse(mooreMachine.containsVertex((char) 200), "No vertex with key 200 was added so it should not have been found");
    }

    @Test
    public void minimizeTest() {
        setupStageM1();
        mooreMachine = mooreMachine.minimize();
        assertEquals(6, mooreMachine.getOrder(), "The number of states of the minimized machine is incorrect");
        char initialState = mooreMachine.getInitialState();
        mooreMachine.BFS(initialState); // It allows to know whether or not there are inaccessible states
        ArrayList<Character> states = mooreMachine.getVertices();
        for (int i = 0; i < states.size(); i++) {
            char state = states.get(i);
            assertTrue(mooreMachine.getVertexColor(state) == Vertex.Color.BLACK, "There should not be any inaccessible state starting processing from the initial state");
        }

        System.out.println(mooreMachine.stateTransitionFunction('A', false));
        System.out.println(mooreMachine.stateTransitionFunction('A', true));
        System.out.println(mooreMachine.getOrder());
        System.out.println(mooreMachine.getStimuliSet());
        System.out.println(mooreMachine.getResponsesSet());
        System.out.println(mooreMachine.getVertices());
    }
}
