package model;

import dataStructures.Edge;
import dataStructures.Vertex;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MooreMachineTest {
    private MooreMachine<Character,Boolean, Boolean> mooreMachine;
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
            mooreMachine.insertState((char)(i + 1), (Math.random() * 10) % 2 == 0);
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
        assertTrue(mooreMachine.isEmpty(), "Graph must be initially empty");
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

    @Test
    public void BFSInDirectedGraphTest() {
        relateStatesTest();
        char src = 8;
        mooreMachine.BFS(src);
        assertTrue(mooreMachine.getVertexColor(src) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance(src) == 0);
        assertTrue(mooreMachine.getVertexColor((char) 3) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance((char) 3) > 0);
        assertTrue(mooreMachine.getVertexColor((char) 5) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance((char) 5) > 0);
        assertTrue(mooreMachine.getVertexColor((char) 6) == Vertex.Color.WHITE && mooreMachine.getSingleSourceDistance((char) 6) == Integer.MAX_VALUE);
        assertTrue(mooreMachine.getVertexColor((char) 1) == Vertex.Color.WHITE && mooreMachine.getSingleSourceDistance((char) 1) == Integer.MAX_VALUE);
        assertTrue(mooreMachine.getVertexColor((char) 2) == Vertex.Color.WHITE && mooreMachine.getSingleSourceDistance((char) 2) == Integer.MAX_VALUE);
        assertTrue(mooreMachine.getVertexColor((char) 4) == Vertex.Color.WHITE && mooreMachine.getSingleSourceDistance((char) 4) == Integer.MAX_VALUE);
        assertTrue(mooreMachine.getVertexColor((char) 7) == Vertex.Color.WHITE && mooreMachine.getSingleSourceDistance((char) 7) == Integer.MAX_VALUE);
        ArrayList<Character> leastStopsPath = mooreMachine.getSingleSourcePath((char) 5);
        assertTrue(leastStopsPath.get(0) == src && mooreMachine.getLastSrc() == src, "Source is not the expected");
        assertTrue(leastStopsPath.get(1) == 5, "It is not the least stops path");

        leastStopsPath = mooreMachine.getSingleSourcePath((char) 1);
        assertTrue(leastStopsPath.isEmpty(), "Vertex 1 is not reachable from source " + src + " so the path must be empty");

        src = 1;
        mooreMachine.BFS(src);
        assertTrue(mooreMachine.getVertexColor(src) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance(src) == 0);
        assertTrue(mooreMachine.getVertexColor((char) 3) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance((char) 3) > 0);
        assertTrue(mooreMachine.getVertexColor((char) 5) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance((char) 5) > 0);
        assertTrue(mooreMachine.getVertexColor((char) 2) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance((char) 2) > 0);
        assertTrue(mooreMachine.getVertexColor((char) 7) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance((char) 7) > 0);
        assertTrue(mooreMachine.getVertexColor((char) 8) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance((char) 8) > 0);
        assertTrue(mooreMachine.getVertexColor((char) 6) == Vertex.Color.WHITE && mooreMachine.getSingleSourceDistance((char) 6) == Integer.MAX_VALUE);
        assertTrue(mooreMachine.getVertexColor((char) 4) == Vertex.Color.WHITE && mooreMachine.getSingleSourceDistance((char) 4) == Integer.MAX_VALUE);
        leastStopsPath = mooreMachine.getSingleSourcePath((char) 4);
        assertTrue(leastStopsPath.isEmpty(), "Vertex 4 is not reachable from any vertex so the path must be empty");

        src = 6;
        mooreMachine.BFS(src);
        assertTrue(mooreMachine.getVertexColor(src) == Vertex.Color.BLACK);
        assertTrue(mooreMachine.getVertexColor((char) 3) == Vertex.Color.BLACK);
        assertTrue(mooreMachine.getVertexColor((char) 5) == Vertex.Color.BLACK);
        assertTrue(mooreMachine.getVertexColor((char) 2) == Vertex.Color.BLACK);
        assertTrue(mooreMachine.getVertexColor((char) 7) == Vertex.Color.BLACK);
        assertTrue(mooreMachine.getVertexColor((char) 8) == Vertex.Color.BLACK);
        assertTrue(mooreMachine.getVertexColor((char) 6) == Vertex.Color.BLACK);
        assertTrue(mooreMachine.getVertexColor((char) 4) == Vertex.Color.WHITE);
        leastStopsPath = mooreMachine.getSingleSourcePath((char) 5);
        assertTrue(leastStopsPath.get(0) == src && mooreMachine.getLastSrc() == src, "Source is not the expected");
        assertTrue(leastStopsPath.get(1) == 1, "It is not the least stops path");
        assertTrue(leastStopsPath.get(2) == 2, "It is not the least stops path");
        assertTrue(leastStopsPath.get(3) == 8, "It is not the least stops path");
        assertTrue(leastStopsPath.get(4) == 5, "It is not the least stops path");

        leastStopsPath = mooreMachine.getSingleSourcePath((char) 3);
        assertTrue(leastStopsPath.get(0) == src && mooreMachine.getLastSrc() == src, "Source is not the expected");
        assertTrue(leastStopsPath.get(1) == 1, "It is not the least stops path");
        assertTrue(leastStopsPath.get(2) == 2, "It is not the least stops path");
        assertTrue(leastStopsPath.get(3) == 3, "It is not the least stops path");
    }
    
    @Test
    public void deleteVertexTest() {
        relateStatesTest();
        int orderBeforeDeletion = mooreMachine.getOrder();
        assertFalse(mooreMachine.deleteVertex((char) 100), "There is not a vertex with key 100 in the graph");
        assertTrue(mooreMachine.deleteVertex((char) 8), "The vertex should have been deleted");
        assertFalse(mooreMachine.containsVertex((char) 8), "The vertex should have been deleted");
        assertTrue(orderBeforeDeletion == mooreMachine.getOrder() + 1, "The order after insertion should be a unit less");
        removedEdges = new ArrayList<>();
        removedEdges.add(new Edge<Integer>(2, 8, Integer.MAX_VALUE));
        removedEdges.add(new Edge<Integer>(8, 3, Integer.MAX_VALUE));
        removedEdges.add(new Edge<Integer>(8, 5, Integer.MAX_VALUE));
        removedEdges.add(new Edge<Integer>(5, 8, Integer.MAX_VALUE));
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
        System.out.println(mooreMachine.stateTransitionFunction('A', false));
        //mooreMachine.minimize();
    }
}
