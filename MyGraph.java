import java.util.*;

/* CSE 373 MyGraph
 * yunluh@uw.edu Hao Yunlu  1337365
 * simenz@uw.edu Zhu Simeng  1336621
 * 
 * A representation of a graph.
 * Assumes that we do not have negative cost edges in the graph.
 */
public class MyGraph implements Graph {
    private Map<Vertex, Collection<Edge>> map;        // map a vertex with its edge as source
    private Map<Vertex, Set<Vertex>> mapAdj;   // map a vertex with its adjacentVertices

    /**
     * Creates a MyGraph object with the given collection of vertices
     * and the given collection of edges.
     * @param v a collection of the vertices in this graph
     * @param e a collection of the edges in this graph
     */
    public MyGraph(Collection<Vertex> v, Collection<Edge> e) {
        map = new HashMap<Vertex, Collection<Edge>>();
        mapAdj = new HashMap<Vertex, Set<Vertex>>();
        for (Vertex single_vertex: v) {
            if (!map.containsKey(single_vertex)) {
                map.put(single_vertex, new HashSet<Edge>());
                mapAdj.put(single_vertex, new HashSet<Vertex>());
            }
        }
        for (Edge single_edge: e) {
            if (!map.containsKey(single_edge.getSource()) || !map.containsKey(single_edge.getDestination())) {
                throw new IllegalArgumentException();
            }
            if (single_edge.getWeight() < 0) {
                throw new IllegalArgumentException();
            }
            if (!map.get(single_edge.getSource()).contains(single_edge)) {
                if (mapAdj.get(single_edge.getSource()).contains(single_edge.getDestination())) {
                     throw new IllegalArgumentException();
                }	
            }
            map.get(single_edge.getSource()).add(single_edge);
            mapAdj.get(single_edge.getSource()).add(single_edge.getDestination());

        }
    }

    /** 
     * Return the collection of vertices of this graph
     * @return the vertices as a collection (which is anything iterable)
     */
    public Collection<Vertex> vertices() {
        Collection<Vertex> copy = new HashSet<Vertex>();
        for (Vertex element : map.keySet()) {
        	copy.add(element);
        }
    	return map.keySet();
    }

    /** 
     * Return the collection of edges of this graph
     * @return the edges as a collection (which is anything iterable)
     */
    public Collection<Edge> edges() {
        // is there some why to return it directly?
        Collection<Edge> edgeSet = new HashSet<Edge>();
        for (Vertex single_vertex: map.keySet()) {
        	for (Edge single_edge: map.get(single_vertex)) {
        		edgeSet.add(single_edge);
        	}
        }
        return edgeSet;
    }

    /**
     * Return a collection of vertices adjacent to a given vertex v.
     *   i.e., the set of all vertices w where edges v -> w exist in the graph.
     * Return an empty collection if there are no adjacent vertices.
     * @param v one of the vertices in the graph
     * @return an iterable collection of vertices adjacent to v in the graph
     * @throws IllegalArgumentException if v does not exist.
     */
    public Collection<Vertex> adjacentVertices(Vertex v) {
        if (v == null) {
            throw new IllegalArgumentException();
        }
        if(!map.containsKey(v)) {
            throw new IllegalArgumentException();
        }
        // should a make another copy or i can return the filed.
        Collection<Vertex> adjacent = mapAdj.get(v);
        return adjacent;
    }

    /**
     * Test whether vertex b is adjacent to vertex a (i.e. a -> b) in a directed graph.
     * Assumes that we do not have negative cost edges in the graph.
     * @param a one vertex
     * @param b another vertex
     * @return cost of edge if there is a directed edge from a to b in the graph, 
     * return -1 otherwise.
     * @throws IllegalArgumentException if a or b do not exist.
     */
    public int edgeCost(Vertex a, Vertex b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException();
        }
        if (mapAdj.get(a).contains(b)) {
            for (Edge single_edge: map.get(a)) {
                if (single_edge.getDestination().equals(b)) {
                    return single_edge.getWeight();
                }
            }
        }
        return -1;
    }

    /**
     * Returns the shortest path from a to b in the graph, or null if there is
     * no such path.  Assumes all edge weights are nonnegative.
     * Uses Dijkstra's algorithm.
     * @param a the starting vertex
     * @param b the destination vertex
     * @return a Path where the vertices indicate the path from a to b in order
     *   and contains a (first) and b (last) and the cost is the cost of 
     *   the path. Returns null if b is not reachable from a.
     * @throws IllegalArgumentException if a or b does not exist.
     */
    public Path shortestPath(Vertex a, Vertex b) {
    	if (a == null || b == null) {
    		throw new IllegalArgumentException();
    	}	
    	// sets the cost of starting point to 0 to use the priority queue
        Vertex tempVertex = a;
        tempVertex.setCost(0);
        Collection<Edge> cache = map.get(a);
        map.remove(a);
        map.put(tempVertex, cache);         
    	
        Queue<Vertex> myHeap = new PriorityQueue<Vertex>(map.keySet());
        
        //set the map as the original one
        map.remove(tempVertex);
        map.put(new Vertex(a.getLabel()), cache); 

        Map<String, Vertex> predecessor = new HashMap<String, Vertex>();

    	while (!myHeap.isEmpty()) {
    		// takes out one with shortest path to a    		
    		Vertex node = myHeap.remove();
    		node.setAsKnown();
            predecessor.put(node.getLabel(), node);
            
    		// find in all edges the one that starts from current shortest path(node)
            for (Vertex adjacent: mapAdj.get(node)) {
            	if (predecessor.containsKey(adjacent.getLabel())) {
            		adjacent = predecessor.get(adjacent.getLabel());
            	}
            	
                if (node.getCost() + edgeCost(node, adjacent) < adjacent.getCost() && !adjacent.isKnown()) {
                	myHeap.remove(adjacent);
                    adjacent.setPredecessor(node);
                    adjacent.setCost(node.getCost() + edgeCost(node, adjacent));
                    predecessor.put(adjacent.getLabel(), adjacent);
                    myHeap.add(adjacent);
                }   
            }  
        }
    	
    	    	
        // finds out the shortest path by back tracing
        // the predecessor of each element
        List<Vertex> temp = new ArrayList<Vertex>();
        Vertex path = /*new Vertex*/predecessor.get(b.getLabel());
        /*for (String label: predecessor.keySet()) {
            if (label.equals(b.getLabel())) {
            	path = predecessor.get(label);
            }
        }*/
        
        
        int cost = path.getCost();
        while (path.getPredecessor() != null) {
            temp.add(path);
            path = path.getPredecessor();
        }
        if (path.getCost() != Integer.MAX_VALUE) {
            temp.add(path);
        }
        
        // restore the path to correct order
        for (int i = temp.size() - 1; i >= 0; i--) {
            temp.add(temp.get(i));
            temp.remove(i);
        }
        
        // reset data for each element to prepare for another call
        for (Vertex v1: mapAdj.keySet()) {
        	for (Vertex v2: mapAdj.get(v1)) {
        		v2.reset();
        	}
        }        

        return new Path(temp, cost);
    }
}