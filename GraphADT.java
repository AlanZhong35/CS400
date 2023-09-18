import java.util.List;

/**
 * Abstract data type for the graph
 * @author amartens
 *
 * @param <NodeType>
 * @param <EdgeType>
 */
public interface GraphADT<NodeType, EdgeType> {
	//public Graph();
	public boolean insertNode(NodeType data);
	public boolean removeNode(NodeType data);
	public boolean containsNode(NodeType data);
	public int getNodeCount();
	public boolean insertEdge(NodeType pred, NodeType succ, EdgeType weight);
	public boolean removeEdge(NodeType pred, NodeType succ);
	public boolean containsEdge(NodeType pred, NodeType succ);
	public EdgeType getEdge(NodeType pred, NodeType succ);
	public int getEdgeCount();
	public int getEdgesInNode(NodeType node);
	public List<NodeType> shortestPathData(NodeType start, NodeType end);
	public double shortestPathCost(NodeType start, NodeType end); //Don’t use this for getting the number of miles/price of route. This returns the total cost.
}
