import java.util.*;

/**
 * Represents a vertex (also known as node) in a graph.
 * Each vertex has a unique ID and maintains a list of outgoing edges.
 */
class Vertex {
    long id;
    List<Edge> edges; // List of outgoing edges from the vertex

    Vertex(long id) {
        this.id = id;
        this.edges = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return id == vertex.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}

/**
 * Represents a directed edge in a graph, connecting from one vertex to the next one.
 */
class Edge {
    Vertex from;
    Vertex to;

    Edge(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return (from != null ? from.id : "null") + "->" + (to != null ? to.id : "null");
    }
}

// Custom Exception Error for Cyclic Graphs
class CycleDetectedException extends RuntimeException {
    public CycleDetectedException(String message) {
        super(message);
    }
}


/**
 * Provides functionality to find the longest path in a DAG
 * The longest path is determined by the maximum number of edges in the path
 * Uses depth-first search (DFS) with memoization and cycle detection
 */
public class LongestPathDAG {
    // The cache keeps data between calls, so we will need to clear it manually when using different graphs to avoid mixing results.
    private static Map<Vertex, Long> cache = new HashMap<>();

    /**
     * Finds the length of the longest path starting from the given vertex in a DAG
     * @param startVertex The vertex from which to start the search for the longest path
     * @return The length of the longest path from a starting vertex; Returns 0 if the starting vertex has no outgoing edges
     * @throws IllegalArgumentException if startVertex is null
     * @throws CycleDetectedException   if a cycle is detected
     */
    public static long findLongestPath(Vertex startVertex) {
        if (startVertex == null) {
            throw new IllegalArgumentException("Start vertex cannot be null.");
        }
        return findLongestPathRecursive(startVertex, new HashSet<>());
    }

    /**
     * Helper function to calculate the longest path
     * @param currentVertex The current vertex being explored
     * @param visiting      A set containing vertices currently in the recursion stack, used to store the explored vertices
     * @return The length of the longest path starting from currentVertex
     * @throws CycleDetectedException if a cycle is detected (i.e. currentVertex is already in the visiting set)
     */
    private static long findLongestPathRecursive(Vertex currentVertex, Set<Vertex> visiting) {
        // If we revisit a node already in the current recursion path, it's a cycle.
        if (visiting.contains(currentVertex)) {
            throw new CycleDetectedException("Cycle detected involving vertex: " + currentVertex.id);
        }

        // If the result for this node is already cached, return it.
        if (cache.containsKey(currentVertex)) {
            return cache.get(currentVertex);
        }

        // Add current node to the path being explored.
        visiting.add(currentVertex);

        // Set the longest path variable to track the longest path to 0, will keep on increasing as new paths are visited
        long maxPath = 0;

        // Check if edges list is initialized
        if (currentVertex.edges != null) {
            for (Edge edge : currentVertex.edges) {
                Vertex neighbor = edge.to;
                if (neighbor != null) {
                    // Recursively find the longest path from the neighbor.
                    long pathFromNeighbor = findLongestPathRecursive(neighbor, visiting);

                    // Path length through this neighbor is 1 (edge) as we have assumed all edges are of equal weights
                    maxPath = Math.max(maxPath, pathFromNeighbor + 1);
                }
            }
        }

        // Remove current node from the path if we backtrack.
        visiting.remove(currentVertex);

        // Store the calculated max path length for this node before returning.
        cache.put(currentVertex, maxPath);
        return maxPath;
    }

    /**
     * Clears cache used for memoization.
     * This should be called before processing a new, independent graph
     */
    public static void clearCache() {
        cache.clear();
    }

    /**
     * Creates and returns a Sample DAG
     * @return A List containing all vertices of the constructed graph.
     */
    private static List<Vertex> createDAG() {
        Vertex v1 = new Vertex(1);
        Vertex v2 = new Vertex(2);
        Vertex v3 = new Vertex(3);
        Vertex v4 = new Vertex(4);
        Vertex v5 = new Vertex(5);
        Vertex v6 = new Vertex(6);
        Vertex v7 = new Vertex(7);

        // Build the graph structure by adding edges
        v1.edges.add(new Edge(v1, v2));
        v1.edges.add(new Edge(v1, v3));
        v1.edges.add(new Edge(v1, v4));
        v2.edges.add(new Edge(v2, v5));
        v3.edges.add(new Edge(v3, v7));
        v4.edges.add(new Edge(v4, v3));
        v4.edges.add(new Edge(v4, v7));
        v4.edges.add(new Edge(v4, v3));
        v5.edges.add(new Edge(v5, v6));
        v6.edges.add(new Edge(v6, v7));

        // Return all vertices
        return Arrays.asList(v1, v2, v3, v4, v5, v6, v7);
    }

    /**
     * Executes the longest path calculation for each vertex and prints the results to the console
     * @param vertices The list of vertices in the graph, including all possible start nodes
     */
    private static void calculateAndPrintPaths(List<Vertex> vertices) {
        LongestPathDAG.clearCache();
        try {
            for (Vertex startVertex : vertices) {
                long longestPath = findLongestPath(startVertex);
                System.out.println("Longest path from vertex " + startVertex.id + ": " + longestPath);
            }
        } catch (CycleDetectedException e) {
            System.err.println("\nError processing the graph: " + e.getMessage());
            System.err.println("Further calculations on this graph are stopped due to detected cycle.");
        } catch (IllegalArgumentException e) {
            System.err.println("\nError during calculation: " + e.getMessage());
        } catch (Exception e) {
            // Catch for any other unexpected errors
            System.err.println("\nAn unexpected error occurred: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Main entry point,orchestrates the creation of graph and the calculation/printing of longest paths.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        System.out.println("--- For a DAG Graph ---");

        // Create a sample graph as defined
        List<Vertex> graphVertices = createDAG();

        // Handle the user provided input of the vertex to calculate the path from
        if (args.length > 0) {
            try {
                long startVertexId = Long.parseLong(args[0]);
                Vertex startVertex = graphVertices.stream()
                        .filter(v -> v.id == startVertexId)
                        .findFirst()
                        .orElse(null);

                LongestPathDAG.clearCache();
                long longestPath = findLongestPath(startVertex);
                System.out.println("Longest path from vertex " + startVertex.id + ": " + longestPath);
            } catch (NumberFormatException e) {
                System.err.println("Error: Invalid vertex ID. Please provide a valid number between 1 and 7");
            }
        } else {
            calculateAndPrintPaths(graphVertices);
        }
    }
}