package model;

import dataStructures.Vertex;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MooreMachineTest {
    private MooreMachine<Character, Boolean, Boolean> mooreMachine;

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

    private void setupStageGraphWithIsolatedVertices() {
        mooreMachine = new MooreMachine<>('A', false);
        for (int i = 0; i < 8; i++) {
            mooreMachine.insertState((char) (i + 1), (Math.random() * 10) % 2 == 0);
        }
    }

    @Test
    public void insertAndSearchStateTest() {
        setupStageGraphWithIsolatedVertices();
        assertFalse(mooreMachine.isEmpty(), "Automata are never empty");
    }

    @Test
    public void minimizeTest() {
        setupStageM1();
        mooreMachine = mooreMachine.minimize();
        assertEquals(6, mooreMachine.getOrder(), "The number of states of the minimized machine is incorrect");
        char initialState = mooreMachine.getInitialState();
        mooreMachine.BFS(initialState); // It allows to know whether or not there are inaccessible states
        ArrayList<Character> states = mooreMachine.getVertices();
        for (char state : states) {
            assertSame(mooreMachine.getVertexColor(state), Vertex.Color.BLACK, "There should not be any inaccessible state starting processing from the initial state");
        }
    }
}
