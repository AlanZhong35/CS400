import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;





/**
 * Graph class - Made with help from the project 3 week 2 code
 * @author amartens
 *
 * @param <String>
 * @param <FlightInterface>
 */
public class Graph<String, T extends FlightInterface> implements GraphADT<String, T> {

	
	protected Hashtable<String,Node> nodes = new Hashtable(); //Hashtable of all the nodes
	protected int edgeCount = 0; //Number of edges in the graph
	
	/**
	 * Search node class that contains the weight of the path and previous airport
	 * @author amartens
	 *
	 */
	protected class SearchNode implements Comparable<SearchNode> {
        public Node node; //The airport of this searchnode
        public double cost; //The cost of the path to get here
        public SearchNode predecessor; //The previous airport in the path
        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }
        public int compareTo(SearchNode other) { //Compares the weight of this searchnode to the other
            if( cost > other.cost ) return +1;
            if( cost < other.cost ) return -1;
            return 0;
        }
    }
	
	/**
	 * Class for the node that contains the edges leaving/entering and the airport name
	 * @author amartens
	 *
	 */
	protected class Node {
        public String data; //The name of the airport
        public List<Edge> edgesLeaving = new LinkedList<>(); //List of flights leaving
        public List<Edge> edgesEntering = new LinkedList<>(); //List of flights entering
        public Node(String data) { this.data = data; } //Constructor
    }
	
	protected class Edge {
        public FlightInterface data; // the weight or cost of this edge
        public Node predecessor;
        public Node successor;
        public Edge(FlightInterface data, Node pred, Node succ) {
            this.data = data;
            this.predecessor = pred;
            this.successor = succ;
        }
    }
	
	/**
	 * Inserts the node into the graph with the airport name as the parameter
	 */
	@Override
	public boolean insertNode(String data) {
        if(nodes.containsKey(data)) return false;
        nodes.put(data,new Node(data));
        return true;
    }

	/**
	 * Removes the node from the graph. Returns false if the node is not present
	 */
	@Override
	public boolean removeNode(String data) {
		// remove this node from nodes collection
        if(!nodes.containsKey(data)) return false; // throws NPE when data==null
        Node oldNode = nodes.remove(data);
        // remove all edges entering neighboring nodes from this one
        for(Edge edge : oldNode.edgesLeaving)
            nodes.get(edge.data.getDestinationAirport()).edgesEntering.remove(edge);
        // remove all edges leaving neighboring nodes toward this one
        for(Edge edge : oldNode.edgesEntering)
        	nodes.get(edge.data.getOriginAirport()).edgesLeaving.remove(edge);
        return true;
	}

	/**
	 * Checks if the airport is in the graph
	 */
	@Override
	public boolean containsNode(String data) {
		return nodes.containsKey(data);
	}

	/**
	 * Returns the number of airports in the graph
	 */
	@Override
	public int getNodeCount() {
		return nodes.size();
	}

	/**
	 * Inserts the flight into the graph
	 * @param pred - the origin airport as a string
	 * @param succ - the destination airport as a string
	 * @param flight - the flight of the edge to insert
	 * @return - true if the insertion was successful. False if the destination or origin is not found
	 */
	@Override
	public boolean insertEdge(String pred, String succ, T flight) {
		// find nodes associated with node data, and return false when not found
        Node predNode = nodes.get(pred);
        Node succNode = nodes.get(succ);
        if(predNode == null || succNode == null) return false;
        try {
            Edge existingEdge = getEdgeHelper(pred,succ);
        } catch(NoSuchElementException e) {
            // otherwise create a new edges
            Edge newEdge = new Edge(flight, predNode, succNode);
            this.edgeCount++;
            // and insert it into each of its adjacent nodes' respective lists
            predNode.edgesLeaving.add(newEdge);
            succNode.edgesEntering.add(newEdge);
        }
        return true;
	}

	/**
	 * Removes the edge from the airport. Returns false if unsuccessful
	 */
	@Override
	public boolean removeEdge(String pred, String succ) {
		try {
            // when an edge exists
            Edge oldEdge = getEdgeHelper(pred,succ);        
            // remove it from the edge lists of each adjacent node
            nodes.get(oldEdge.data.getOriginAirport()).edgesLeaving.remove(oldEdge);
            nodes.get(oldEdge.data.getDestinationAirport()).edgesEntering.remove(oldEdge);
            // and decrement the edge count before removing
            this.edgeCount--;
            return true;
        } catch(NoSuchElementException e) {
            // when no such edge exists, return false instead
            return false;
        }
	}

	/**
	 * Returns true if the flight is in the graph, false otherwise
	 */
	@Override
	public boolean containsEdge(String pred, String succ) {
		try { getEdgeHelper(pred,succ); return true; }
        catch(NoSuchElementException e) { return false; }
	}
	
	/**
	 * Returns the edge from two airports
	 */
	@Override
	public T getEdge(String pred, String succ) {
		return (T) getEdgeHelper(pred,succ).data;
	}

	/**
	 * Returns the edge with the given origin and destination. Throws if there is no edge
	 * @param pred - the origin airport as a string
	 * @param succ - the destination airport as a string
	 * @return - the flight connecting the two airports
	 */
	protected Edge getEdgeHelper(String pred, String succ) {
        Node predNode = nodes.get(pred);
        // search for edge through the predecessor's list of leaving edges
        for(Edge edge : predNode.edgesLeaving)
            // compare succ to the data in each leaving edge's successor
            if(edge.data.getDestinationAirport().equals(succ))
                return edge;
        // when no such edge can be found, throw NSE
        throw new NoSuchElementException("No edge from "+pred.toString()+" to "+
                                         succ.toString());
    }
	
	/**
	 * Returns the number of edges in the graph
	 */
	@Override
	public int getEdgeCount() {
		return this.edgeCount;
	}

	/**
	 * Returns the number of leaving edges in the node
	 */
	@Override
	public int getEdgesInNode(String node) {
		return nodes.get(node).edgesLeaving.size();
	}

	 /**
	  * Returns a list of strings for the shortest path between airports
	  */
	@Override
	public List<String> shortestPathData(String start, String end) {
		//Throws an error if the start or end isn't in the graph
		if(!nodes.containsKey(start) || !nodes.containsKey(end)) {
    		throw new NoSuchElementException("Start or End is not in the graph");
    	}
		//Returns a list of strings that shows the path of airports
    	SearchNode endNode = computeShortestPath(start, end);
    	LinkedList<String> backwards = new LinkedList<String>();
    	while(endNode != null) {
    		backwards.add(endNode.node.data);
    		endNode = endNode.predecessor;
    	}
    	LinkedList<String> path = new LinkedList<String>();
    	for (int i = backwards.size() - 1; i >= 0; i--) {
    		path.add(backwards.get(i));
    	}
        return path;
	}

	/**
	 * Returns the cost of the shortest path between airports
	 */
	@Override
	public double shortestPathCost(String start, String end) {
    	if(!nodes.containsKey(start) || !nodes.containsKey(end)) {
    		throw new NoSuchElementException("Start or End is not in the graph");
    	}
        return computeShortestPath(start, end).cost;
	}

	/**
	 * Uses Dijkstra's algorithm to compute the shortest path
	 * @param start - the starting airport
	 * @param end - the destination airport
	 * @return - the search node that contains the previous airport and the cost of the path
	 */
	protected SearchNode computeShortestPath(String start, String end) {
    	if(start == end) { //Returns a bank search node if start and end are equal
    		return new SearchNode(nodes.get(start), 0, null);
    	}
    	if(!nodes.containsKey(start) || !nodes.containsKey(end)) { //Throws if start or end isn't in the graph
    		throw new NoSuchElementException("Start or End is not in the graph");
    	}
    	SearchNode predecessor = null;
    	double cost = 0;
    	PriorityQueue<SearchNode> queue = new PriorityQueue<SearchNode>(); //Makes the priority queue of search nodes
    	queue.add(new SearchNode(nodes.get(start), cost, predecessor));
    	Hashtable<String, SearchNode> visitedNodes = new Hashtable<String, SearchNode>();
    	while(!queue.isEmpty()) { 
    		SearchNode newNode = queue.remove();
    		if(!visitedNodes.containsKey(newNode.node.data)) {
    			visitedNodes.put(newNode.node.data, newNode);
    			predecessor = newNode;
    			cost = newNode.cost;
    			for(Edge e : newNode.node.edgesLeaving) {
    				if(!visitedNodes.containsKey(e.data.getDestinationAirport())) {
    					queue.add(new SearchNode(nodes.get(e.data.getDestinationAirport()), e.data.value() + cost, predecessor));
    				}
    			}
    		}
    	}
    	if(!visitedNodes.containsKey(end)) {
    		throw new NoSuchElementException("There is not path for the start and end");
    	}
        return visitedNodes.get(end);
    }
	

}
