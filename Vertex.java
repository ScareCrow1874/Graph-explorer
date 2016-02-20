/* CSE 373 MyGraph
 * yunluh@uw.edu Hao Yunlu  1337365
 * simenz@uw.edu Zhu Simeng  1336621
 * 
 * Representation of a graph vertex
 */
public class Vertex implements Comparable<Vertex>{
	private final String label;   // label attached to this vertex
	private int cost;
	private boolean known;
	private Vertex predecessor;

	/**
	 * Construct a new vertex
	 * @param label the label attached to this vertex
	 */
	public Vertex(String label) {
		if(label == null) {
			throw new IllegalArgumentException("null");
		}
		this.cost = Integer.MAX_VALUE;
		this.label = label;
		this.known = false;
		this.predecessor = null;
	}

	/**
	 * Get a vertex label
	 * @return the label attached to this vertex
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * A string representation of this object
	 * @return the label attached to this vertex
	 */
	public String toString() {
		return label;
	}
	
	// returns the minimum path cost to this vertex from starting vertex
	public int getCost() {
		return cost;
	}
	
	// returns the predecessor of this vertex
	public Vertex getPredecessor() {
		return predecessor;
	}
	
	// returns whether the shortest path to this vertex is known
	public boolean isKnown() {
		return known;
	}
	
	// sets the vertex as known
	public void setAsKnown() {
		this.known = true;
	}
	
    // resets vertices in graph to initial state to prepare for another call
	public void reset() {
		this.cost = Integer.MAX_VALUE;
		this.known = false;
		this.predecessor = null;
	}
	
	// sets the vertex's predecessor
	public void setPredecessor(Vertex b) {
		this.predecessor = b;
	}
	
	// sets the vertex's cost
	public void setCost(int cost) {
		if (cost < 0) {
			throw new IllegalArgumentException();
		}
		this.cost = cost;
	}
	
	//auto-generated: hashes on label
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}
	
	//compares this vertex to another vertex
	//returns positive integer if this vertex costs more
	//returns negative integer if the other vertex costs more
	//returns zero if they have the same cost
	public int compareTo(Vertex another) {
		return this.getCost() - another.getCost(); 
	}
	
	//auto-generated: compares labels
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Vertex other = (Vertex) obj;
		if (label == null) {
            return other.label == null;
		} else {
		    return label.equals(other.label);
		}
	}
}
