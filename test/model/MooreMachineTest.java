package model;

import dataStructures.Edge;
import dataStructures.Vertex;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MooreMachineTest {
    private MooreMachine<Integer,Integer, Integer> mooreMachine;
    private ArrayList<Edge<Integer>> removedEdges;

    private void setupStage1() {
        mooreMachine = new MooreMachine<>(1);
    }
    
    private void setupStageGraphWithIsolatedVertices() {
        for (int i = 0; i < 8; i++) {
            mooreMachine.insertState(i + 1, 8-i);
        }
    }

    @Test
    public void insertAndSearchStateTest() {
        setupStage1();
        assertTrue(mooreMachine.isEmpty(), "Graph must be initially empty");
        stateInsertionLoop();
    }

    @Test
    public void relateStatesTest() {
        setupStage1();
        assertTrue(mooreMachine.isEmpty(), "Graph must be initially empty");
        setupStageGraphWithIsolatedVertices();

        int src = 1;
        int dst = 2;
        int response = 4;
        mooreMachine.relate(src, dst, response);

        src = 2;
        dst = 1;
        response = 3;
        mooreMachine.relate(src, dst, response);

        src = 2;
        dst = 3;
        response = 9;
        mooreMachine.relate(src, dst, response);

        src = 2;
        dst = 7;
        response = 5;
        mooreMachine.relate(src, dst, response);

        src = 2;
        dst = 8;
        response = 5;
        mooreMachine.relate(src, dst, response);

        src = 8;
        dst = 3;
        response = 10;
        mooreMachine.relate(src, dst, response);

        src = 8;
        dst = 5;
        response = 7;
        mooreMachine.relate(src, dst, response);

        src = 5;
        dst = 8;
        response = 2;
        mooreMachine.relate(src, dst, response);

        src = 6;
        dst = 1;
        response = 8;
        mooreMachine.relate(src, dst, response);
    }

    @Test
    public void unlinkVerticesInDirectedGraphTest() {
        relateStatesTest();
        mooreMachine.unlink(2, 8);
        assertFalse(mooreMachine.containsEdge(2, 8), "The edge was not removed");
        mooreMachine.unlink(6, 1);
        assertFalse(mooreMachine.containsEdge(6, 1), "The edge was not removed");
    }

    @Test
    public void BFSInDirectedGraphTest() {
        relateStatesTest();
        int src = 8;
        mooreMachine.BFS(src);
        assertTrue(mooreMachine.getVertexColor(src) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance(src) == 0);
        assertTrue(mooreMachine.getVertexColor(3) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance(3) > 0);
        assertTrue(mooreMachine.getVertexColor(5) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance(5) > 0);
        assertTrue(mooreMachine.getVertexColor(6) == Vertex.Color.WHITE && mooreMachine.getSingleSourceDistance(6) == Integer.MAX_VALUE);
        assertTrue(mooreMachine.getVertexColor(1) == Vertex.Color.WHITE && mooreMachine.getSingleSourceDistance(1) == Integer.MAX_VALUE);
        assertTrue(mooreMachine.getVertexColor(2) == Vertex.Color.WHITE && mooreMachine.getSingleSourceDistance(2) == Integer.MAX_VALUE);
        assertTrue(mooreMachine.getVertexColor(4) == Vertex.Color.WHITE && mooreMachine.getSingleSourceDistance(4) == Integer.MAX_VALUE);
        assertTrue(mooreMachine.getVertexColor(7) == Vertex.Color.WHITE && mooreMachine.getSingleSourceDistance(7) == Integer.MAX_VALUE);
        ArrayList<Integer> leastStopsPath = mooreMachine.getSingleSourcePath(5);
        assertTrue(leastStopsPath.get(0) == src && mooreMachine.getLastSrc() == src, "Source is not the expected");
        assertTrue(leastStopsPath.get(1) == 5, "It is not the least stops path");

        leastStopsPath = mooreMachine.getSingleSourcePath(1);
        assertTrue(leastStopsPath.isEmpty(), "Vertex 1 is not reachable from source " + src + " so the path must be empty");

        src = 1;
        mooreMachine.BFS(src);
        assertTrue(mooreMachine.getVertexColor(src) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance(src) == 0);
        assertTrue(mooreMachine.getVertexColor(3) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance(3) > 0);
        assertTrue(mooreMachine.getVertexColor(5) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance(5) > 0);
        assertTrue(mooreMachine.getVertexColor(2) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance(2) > 0);
        assertTrue(mooreMachine.getVertexColor(7) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance(7) > 0);
        assertTrue(mooreMachine.getVertexColor(8) == Vertex.Color.BLACK && mooreMachine.getSingleSourceDistance(8) > 0);
        assertTrue(mooreMachine.getVertexColor(6) == Vertex.Color.WHITE && mooreMachine.getSingleSourceDistance(6) == Integer.MAX_VALUE);
        assertTrue(mooreMachine.getVertexColor(4) == Vertex.Color.WHITE && mooreMachine.getSingleSourceDistance(4) == Integer.MAX_VALUE);
        leastStopsPath = mooreMachine.getSingleSourcePath(4);
        assertTrue(leastStopsPath.isEmpty(), "Vertex 4 is not reachable from any vertex so the path must be empty");

        src = 6;
        mooreMachine.BFS(src);
        assertTrue(mooreMachine.getVertexColor(src) == Vertex.Color.BLACK);
        assertTrue(mooreMachine.getVertexColor(3) == Vertex.Color.BLACK);
        assertTrue(mooreMachine.getVertexColor(5) == Vertex.Color.BLACK);
        assertTrue(mooreMachine.getVertexColor(2) == Vertex.Color.BLACK);
        assertTrue(mooreMachine.getVertexColor(7) == Vertex.Color.BLACK);
        assertTrue(mooreMachine.getVertexColor(8) == Vertex.Color.BLACK);
        assertTrue(mooreMachine.getVertexColor(6) == Vertex.Color.BLACK);
        assertTrue(mooreMachine.getVertexColor(4) == Vertex.Color.WHITE);
        leastStopsPath = mooreMachine.getSingleSourcePath(5);
        assertTrue(leastStopsPath.get(0) == src && mooreMachine.getLastSrc() == src, "Source is not the expected");
        assertTrue(leastStopsPath.get(1) == 1, "It is not the least stops path");
        assertTrue(leastStopsPath.get(2) == 2, "It is not the least stops path");
        assertTrue(leastStopsPath.get(3) == 8, "It is not the least stops path");
        assertTrue(leastStopsPath.get(4) == 5, "It is not the least stops path");

        leastStopsPath = mooreMachine.getSingleSourcePath(3);
        assertTrue(leastStopsPath.get(0) == src && mooreMachine.getLastSrc() == src, "Source is not the expected");
        assertTrue(leastStopsPath.get(1) == 1, "It is not the least stops path");
        assertTrue(leastStopsPath.get(2) == 2, "It is not the least stops path");
        assertTrue(leastStopsPath.get(3) == 3, "It is not the least stops path");
    }
    
    @Test
    public void deleteVertexTest() {
        relateStatesTest();
        int orderBeforeDeletion = mooreMachine.getOrder();
        assertFalse(mooreMachine.deleteVertex(100), "There is not a vertex with key 100 in the graph");
        assertTrue(mooreMachine.deleteVertex(8), "The vertex should have been deleted");
        assertFalse(mooreMachine.containsVertex(8), "The vertex should have been deleted");
        assertTrue(orderBeforeDeletion == mooreMachine.getOrder() + 1, "The order after insertion should be a unit less");
        removedEdges = new ArrayList<>();
        removedEdges.add(new Edge<Integer>(2, 8, Integer.MAX_VALUE));
        removedEdges.add(new Edge<Integer>(8, 3, Integer.MAX_VALUE));
        removedEdges.add(new Edge<Integer>(8, 5, Integer.MAX_VALUE));
        removedEdges.add(new Edge<Integer>(5, 8, Integer.MAX_VALUE));
    }

    private void stateInsertionLoop() {
        int vertexCount = 0;
        for (int i = 0; i < 50; i++) {
            int r = (int) (Math.random() * 100);
            if (mooreMachine.insertState(r, (int) (Math.random() * 100))) {
                vertexCount++;
            }
            assertTrue(mooreMachine.containsVertex(r), "The vertex with key " + r + " must have been found as it was added either in a previous iteration of the for loop or in this iteration");
            assertTrue(mooreMachine.getOrder() == vertexCount, "The order is not the expected");
            assertTrue(mooreMachine.getAdjacent(r).isEmpty(), "The vertex just added should not have any edges");
            assertFalse(mooreMachine.isEmpty(), "Graph must not be empty after insertion");
        }
        assertFalse(mooreMachine.containsVertex(200), "No vertex with key 200 was added so it should not have been found");
    }
}
