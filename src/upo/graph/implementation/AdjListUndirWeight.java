package upo.graph.implementation;

import java.util.NoSuchElementException;
import java.util.Set;

import upo.graph.base.*;

/**
 * Implementazione mediante <strong>liste di adiacenza</strong> di un grafo <strong>non orientato pesato</strong>.
 * 
 * @author Nome Cognome Matricola
 *
 */
public class AdjListUndirWeight implements WeightedGraph{

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#addVertex()
	 */
	@Override
	public int addVertex() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#containsVertex(int)
	 */
	@Override
	public boolean containsVertex(int index) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#removeVertex(int)
	 */
	@Override
	public void removeVertex(int index) throws NoSuchElementException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#containsEdge(int, int)
	 */
	@Override
	public boolean containsEdge(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#removeEdge(int, int)
	 */
	@Override
	public void removeEdge(int sourceVertexIndex, int targetVertexIndex)
			throws IllegalArgumentException, NoSuchElementException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#getAdjacent(int)
	 */
	@Override
	public Set<Integer> getAdjacent(int vertexIndex) throws NoSuchElementException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#isAdjacent(int, int)
	 */
	@Override
	public boolean isAdjacent(int targetVertexIndex, int sourceVertexIndex) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#size()
	 */
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#isDirected()
	 */
	@Override
	public boolean isDirected() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#isCyclic()
	 */
	@Override
	public boolean isCyclic() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#isDAG()
	 */
	@Override
	public boolean isDAG() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#getBFSTree(int)
	 */
	@Override
	public VisitForest getBFSTree(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#getDFSTree(int)
	 */
	@Override
	public VisitForest getDFSTree(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#getDFSTOTForest(int)
	 */
	@Override
	public VisitForest getDFSTOTForest(int startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#getDFSTOTForest(int, int[])
	 */
	@Override
	public VisitForest getDFSTOTForest(int[] vertexOrdering)
			throws UnsupportedOperationException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#topologicalSort()
	 */
	@Override
	public int[] topologicalSort() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#stronglyConnectedComponents()
	 */
	@Override
	public Set<Set<Integer>> stronglyConnectedComponents() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#connectedComponents()
	 */
	@Override
	public Set<Set<Integer>> connectedComponents() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.WeightedGraph#getEdgeWeight(int, int)
	 */
	@Override
	public double getEdgeWeight(int sourceVertexIndex, int targetVertexIndex)
			throws IllegalArgumentException, NoSuchElementException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.WeightedGraph#setEdgeWeight(int, int, double)
	 */
	@Override
	public void setEdgeWeight(int sourceVertexIndex, int targetVertexIndex, double weight)
			throws IllegalArgumentException, NoSuchElementException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WeightedGraph getBellmanFordShortestPaths(int startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WeightedGraph getDijkstraShortestPaths(int startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WeightedGraph getPrimMST(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WeightedGraph getKruskalMST() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WeightedGraph getFloydWarshallShortestPaths() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

}
