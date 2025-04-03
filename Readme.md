## Longest Path in a Directed Acyclic Graph (DAG)
This is an implementation to find the length of the longest directed path starting from a given vertex within a Directed Acyclic Graph (DAG) using JAVA.

**Properties of any Graph to be A Directed Acyclic Graph:**\
For any graph to be considered DAG; it has to satisfy these basis conditions
1. Graph has to be directed (i.e. the Edges of the graph always have a specific direction).
2. It has to be acyclic (i.e it's not allowed to start at a vertex, follow a sequence of directed edges, and return to the same starting vertex)

## Solution Overview
The following concepts are used for finding the longest path in the ```LongestPathDAG.java``` file inside the ```src``` directory.

**Defining Graph:** Any graph (G) is formally defined as G(E,V). Hence for defining a DAG (which is a type of graph G), definition of edges (E) and vertex (V) class has been used, where each Vertex maintains a list of outgoing Edges.

**Algorithm Used:** Utilizes Depth-First Search (DFS) from the given vertex as specified on the requirement.

**Memoization:** Uses a cache to remember the longest path length computed so far for each vertex visited. Cache usage also avoids redundant calculation for overlapping sub-graphs, significantly improving the efficiency of the algorithm.

**Cycle Detection:** Implements a method to detect cycles (if present) in the graph during DFS traversal. If a cycle is found, an exception error is raised.

### Input and Output
**Input:**\
A DAG graph, and a starting Vertex within the graph.

**Output:**\
A value representing the longest directed path from the given starting vertex. If the starting vertex given has no outgoing paths, the length returned is 0.

---
## Execution Instructions
>**Prerequisite:**
JAVA(version 8 or above)
> 
**1. Compile Program:**\
```javac LongestPathDAG.java```\
This will generate ```LongestPathDAG.class``` file inside the ```src``` directory

**2. Run Program:**\
```java LongestPathDAG```\
This will print the following output in the terminal
```
--- For a DAG Graph ---
Longest path from vertex 1: 4
Longest path from vertex 2: 3
Longest path from vertex 3: 1
Longest path from vertex 4: 2
Longest path from vertex 5: 2
Longest path from vertex 6: 1
Longest path from vertex 7: 0
```

To calculate the longest path from any vertex (say vertex 3) of implemented sample graph, we can simply provide vertex id as argument
```
java LongestPathDAG 3
```
This will print the following output
```
--- For a DAG Graph ---
Longest path from vertex 3: 1
```

For, demonstration purposes, a sample graph is hardcoded inside the solution itself using ```createDAG()```. To apply the longest path calculation for a custom graph you will need to create a separate Java file or modify structure of graph in the ```createDAG()``` method.

Also, we can execute the test cases for the above solution, which are defined in the file ```LongestPathDAGTest.java``` inside the ```src``` directory
### For Test Cases Execution
**1. Compile Test Case:**\
```javac -cp "../lib/*" *.java```\
As the test is written using Junit Library (an external library); we need to specify the path for the compiler to look for when it encounters annotations or any classes which are not standard; which is what the ```-cp``` flag does

**2. Run Test Case:**\
```java -jar ../lib/junit-platform-console-standalone-1.9.0.jar --class-path ".:../lib/*" --scan-classpath```\
This will run the test cases written.


---
## QUESTIONS
**1. Does the solution work for larger graphs?**\
Yes, the solution does work for larger graphs as long as the graph representation and the cache used for recursion itself fits in the memory of the system. 

**2. Can you think of any optimizations?**\
There can be a few optimization on the provided approach. The current approach uses recursion, which can lead to memory overflow for extremely large graphs, to avoid this stack overflow error a better approach could be replacing recursion with a stack. Next optimization could be using topological sorting instead of recursion explore the graph. Finally, if the graph is certain to have consecutive integers we can use primitive arrays instead of HashMaps to reduce memory overhead to gain some minor performance improvements.

**3. What’s the computational complexity of your solution?**\
The computational complexity of the presented solution in terms of\
Time Complexity is O(E+V)\
Space Complexity is O(V)\
E: no. of Edges
V: no. of Vertices

**4. Are there any unusual cases that aren&#39;t handled?**
- If the graph has multiple disconnected components, this solution only explores the component reachable from given starting vertex.
- The solution assumes that weights of all edges of the graphs are 1.
- Since the cache is static, it must be explicitly cleared using clearCache() while processing multiple, separate graph instances in the same run to ensure the results are correct.



---
## Additional Questions
**1. What are some things you don’t like about Java?**
- A lot of boiler plate code as compared to other programing languages such as Python.

For example, for a simple task of create an array and printing it's element\
In Python:
```python
my_list = ["apple", "banana", "cherry"]
for item in my_list:
  print(item)
```

In JAVA:
```java
import java.util.*;

public class MySimpleList {
    public static void main(String[] args) {
        List<String> myList = List.of("apple", "banana", "cherry");
        for (String item : myList) {
            System.out.println(item);
        }
    }
}
```

- JVM-based applications can have longer startup times, hence making them less ideal for cloud environments like AWS Lambda, where fast cold star is an important factor. 


**2. If you could choose any language/framework/technology stack, what would you choose and
why?**
- For building a highly scalable, secure enterprise solution, I would stick with Java + Spring Boot + Angular.
- For AI/ML workloads and faster prototyping is of importance, I would select Python.