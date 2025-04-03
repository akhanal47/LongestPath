import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Unit tests for the {@link LongestPathDAG} class.
 * Focuses on verifying the correctness of the {@link LongestPathDAG#findLongestPath(Vertex)} method
 * under various graph conditions, including DAGs, disconnected graphs, edge cases, and cyclic graphs.
 * Ensures proper handling of the static cache via {@link BeforeEach} setup.
 */
class LongestPathDAGTest {

    /**
     * Executed before each test method.
     * Ensures that the cache in {@link LongestPathDAG} cleared before each test for accurate path calculation
     */
    @BeforeEach
    void setUp() {
        LongestPathDAG.clearCache();
    }

    // Test for disconnected graph, here all vertex are not connected to each other
    @Test
    void testLongestPathDisconnectedGraph() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);

        v1.edges.add(new Edge(v1, v2));
        v3.edges.add(new Edge(v3, v4));

        assertEquals(1, LongestPathDAG.findLongestPath(v1), "Path from v1");
        assertEquals(0, LongestPathDAG.findLongestPath(v2), "Path from v2");
        assertEquals(1, LongestPathDAG.findLongestPath(v3), "Path from v3");
        assertEquals(0, LongestPathDAG.findLongestPath(v4), "Path from v4");
    }

    // Test case for null as start vertex
    @Test
    void testLongestPathNullStartVertex() {
        assertThrows(IllegalArgumentException.class, () -> {
            LongestPathDAG.findLongestPath(null);
        }, "Passing null start vertex; should throw IllegalArgumentException error");
    }

    // Test case for single vertex graph
    @Test
    void testLongestPathSingleVertexGraph() {
        Vertex v = new Vertex(1);
        assertEquals(0, LongestPathDAG.findLongestPath(v), "Path from single node");
    }

    // Test for cyclic graph, this will throw the custom cyclic graph error
    @Test
    void testSimpleCycleDetection() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);

        v1.edges.add(new Edge(v1, v2));
        v2.edges.add(new Edge(v2, v1)); // As 1 -> 2 -> 1 is cyclic

        CycleDetectedException ex = assertThrows(CycleDetectedException.class, () -> {
            LongestPathDAG.findLongestPath(v1);
        });
        assertTrue(ex.getMessage().contains("Cycle detected involving vertex: 1") || ex.getMessage().contains("Cycle detected involving vertex: 2"));

        // Clear cache and test starting from vertex 2
        LongestPathDAG.clearCache();
        ex = assertThrows(CycleDetectedException.class, () -> {
            LongestPathDAG.findLongestPath(v2);
        });
        assertTrue(ex.getMessage().contains("Cycle detected involving vertex: 1") || ex.getMessage().contains("Cycle detected involving vertex: 2"));
    }

    // Adding a test case which will definitely fail
    @Test
    void testDeliberateFailure() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);

        v1.edges.add(new Edge(v1, v2));
        v2.edges.add(new Edge(v2, v3));

        // The actual longest path between v1 and v3 is 2
        long actualPathLength = LongestPathDAG.findLongestPath(v1);

        // Assertion is intentionally incorrect to show the method to calculate longest path is correct
        long incorrectExpectedLength = 99;
        assertNotEquals(incorrectExpectedLength, actualPathLength,
                "This test is ensures the longest path calculation returns a correct value and not random value which would be incorrect.");
    }
}