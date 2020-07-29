package upo.graph.implementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;

import upo.graph.base.VisitForest;
import upo.graph.base.VisitForest.Color;
import upo.graph.base.VisitForest.VisitType;
import upo.graph.base.WeightedGraph;

/**
 * Implementazione mediante <strong>matrice di adiacenza</strong> di un grafo <strong>non orientato pesato</strong>.
 * 
 *  @author Dawit Gulino 20013954
 *
 */
public class AdjMatrixUndirWeight implements WeightedGraph{
	
	/**
	 * 
	 */
	private Double[][] adjMatrix;
	
	/**
	 * 
	 */
	public AdjMatrixUndirWeight() {
		this(0);
	}

	/**
	 * 
	 * @param numVertici
	 */
	public AdjMatrixUndirWeight(int numVertici) {
		adjMatrix = new Double[numVertici][numVertici];
	}
	
	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#addVertex()
	 */
	@Override
	public int addVertex() {
		Double[][] temp = adjMatrix;
		int newSize = adjMatrix.length +1;
		
		adjMatrix = new Double[newSize][newSize];
		for(int i = 0; i<temp.length;i++)
			for(int j = 0; j<temp.length;j++)
				adjMatrix[i][j] = temp[i][j];
		return newSize;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#containsVertex(int)
	 */
	@Override
	public boolean containsVertex(int index) {
		return adjMatrix.length>index && index>=0;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#removeVertex(int)
	 */
	@Override
	public void removeVertex(int index) throws NoSuchElementException {
		if(!containsVertex(index))
			throw new NoSuchElementException();
		Double[][] temp = adjMatrix;
		int newSize = adjMatrix.length -1;
		
		adjMatrix = new Double[newSize][newSize];
		for(int i = 0; i<adjMatrix.length;i++)
			for(int j = 0; j<adjMatrix.length;j++) {
				int newI = i, newJ = j;
				
				if(newI>=index)
					newI++;
				if(newJ>=index)
					newJ++;
				adjMatrix[i][j] = temp[newI][newJ];
			}
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException {
		containsEdge(sourceVertexIndex, targetVertexIndex);
		adjMatrix[sourceVertexIndex][targetVertexIndex] = WeightedGraph.defaultEdgeWeight;
		adjMatrix[targetVertexIndex][sourceVertexIndex] = WeightedGraph.defaultEdgeWeight;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#containsEdge(int, int)
	 */
	@Override
	public boolean containsEdge(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException {
		if(!containsVertex(sourceVertexIndex) || !containsVertex(targetVertexIndex))
			throw new IllegalArgumentException();
		return adjMatrix[sourceVertexIndex][targetVertexIndex]!=null;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#removeEdge(int, int)
	 */
	@Override
	public void removeEdge(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException, NoSuchElementException {
		if(!containsEdge(sourceVertexIndex, targetVertexIndex))
			throw new NoSuchElementException();
		adjMatrix[sourceVertexIndex][targetVertexIndex] = null;
		adjMatrix[targetVertexIndex][sourceVertexIndex] = null;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#getAdjacent(int)
	 */
	@Override
	public Set<Integer> getAdjacent(int vertexIndex) throws NoSuchElementException {
		if(!containsVertex(vertexIndex))
			throw new NoSuchElementException();
		
		Set<Integer> adj = new HashSet<>();
		for(int i=0;i<size();i++)
			if(adjMatrix[vertexIndex][i]!=null)
				adj.add(i);
		
		return adj;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#isAdjacent(int, int)
	 */
	@Override
	public boolean isAdjacent(int targetVertexIndex, int sourceVertexIndex) throws IllegalArgumentException {
		return containsEdge(sourceVertexIndex, targetVertexIndex);
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#size()
	 */
	@Override
	public int size() {
		return adjMatrix.length;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#isDirected()
	 */
	@Override
	public boolean isDirected() {
		return false;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#isCyclic()
	 */
	@Override
	public boolean isCyclic() {
		for(int i = 0; i<adjMatrix.length;i++)
			for(int j = i; j<adjMatrix.length;j++)
				if(adjMatrix[i][j]!=null)
					return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#isDAG()
	 */
	@Override
	public boolean isDAG() {
		return false;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#getBFSTree(int)
	 */
	@Override
	public VisitForest getBFSTree(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#getDFSTree(int)
	 */
	@Override
	public VisitForest getDFSTree(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		if(!containsVertex(startingVertex))
			throw new IllegalArgumentException();
		
		VisitForest visita = new VisitForest(this, VisitType.DFS);
		getDFSImpl(startingVertex, 0, visita);
		return visita;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#getDFSTOTForest(int)
	 */
	@Override
	public VisitForest getDFSTOTForest(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		if(!containsVertex(startingVertex))
			throw new IllegalArgumentException();
		
		VisitForest visita = new VisitForest(this, VisitType.DFS_TOT);
		int time = getDFSImpl(startingVertex, 0, visita);
		for(int i = 0; i<size();i++)
			if(visita.getColor(i).equals(Color.WHITE))
				time = getDFSImpl(i, time, visita);
		return visita;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#getDFSTOTForest(int, int[])
	 */
	@Override
	public VisitForest getDFSTOTForest(int[] vertexOrdering) throws UnsupportedOperationException, IllegalArgumentException {
		VisitForest visita = new VisitForest(this, VisitType.DFS_TOT);
		int time = 0;
		for(int startingVertex : vertexOrdering) {
			if(startingVertex > size() || startingVertex < 0)
				throw new IllegalArgumentException();
			time = getDFSImpl(startingVertex, time, visita);
		}
		
		for(int i = 0; i<size();i++)
			if(visita.getColor(i).equals(Color.WHITE))
				getDFSImpl(i, time, visita);
		
		return visita;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#topologicalSort()
	 */
	@Override
	public int[] topologicalSort() throws UnsupportedOperationException { 
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#stronglyConnectedComponents()
	 */
	@Override
	public Set<Set<Integer>> stronglyConnectedComponents() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#connectedComponents()
	 */
	@Override
	public Set<Set<Integer>> connectedComponents() throws UnsupportedOperationException {
		Set<Set<Integer>> scc = new HashSet<>();
		boolean[] black = new boolean[size()];
		VisitForest vf = new VisitForest(this, VisitType.DFS_TOT);
		
		for(int i = 0; i < size(); i++) {
			if(vf.getColor(i).equals(Color.WHITE)) {
				// Ogni volta che finisce una DFS aggiungi tutti i vertici al SCC
				this.getDFSImpl(i, 0, vf);
				Set<Integer> cc = new HashSet<>();
				
				for(int j = 0; j < size(); j++)
					if(vf.getColor(j).equals(Color.BLACK) && !black[j]) {
						cc.add(j);
						black[j] = true;	// evita di fare la dfs su quel vertex già aggiunto
					}
				scc.add(cc);
			}
		}
		return scc;
	}

	/**
	 * 
	 * @param startingVertex
	 * @param time
	 * @param visita
	 * @return
	 */
	private int getDFSImpl(int startingVertex, int time, VisitForest visita) {
		Stack<Integer> stack = new Stack<>();
		stack.push(startingVertex);
		
		visita.setStartTime(startingVertex, time++);
		visita.setColor(startingVertex, Color.GRAY);
		visita.setDistance(startingVertex, 0);
		
		while(!stack.isEmpty()) {
			int vertex = stack.peek();
			
			/* ordine alfabetico kinda~ UwU */
			List<Integer> sorted = new ArrayList<>(getAdjacent(vertex));
			sorted.sort(null);
			boolean pop = true;
			
			Iterator<Integer> iter = sorted.iterator();
			while(iter.hasNext() && pop) {
				Integer adj = iter.next();
				if(visita.getColor(adj).equals(Color.WHITE)) {
					Double distance = visita.getDistance(vertex);
					distance = (distance == null) ? 0 : distance+1;
					
					visita.setStartTime(adj, time++);
					visita.setColor(adj, Color.GRAY);
					visita.setDistance(adj, distance);
					visita.setParent(adj, vertex);
					
					stack.push(adj);
					pop = false;
				}
			}
			if(pop) {
				visita.setColor(vertex, Color.BLACK);
				visita.setEndTime(vertex, time++);
				stack.pop();
			}
		}
		return time;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.WeightedGraph#getEdgeWeight(int, int)
	 */
	@Override
	public double getEdgeWeight(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException, NoSuchElementException {
		if(!containsEdge(sourceVertexIndex, targetVertexIndex))
			throw new NoSuchElementException();
		return adjMatrix[sourceVertexIndex][targetVertexIndex];
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.WeightedGraph#setEdgeWeight(int, int, double)
	 */
	@Override
	public void setEdgeWeight(int sourceVertexIndex, int targetVertexIndex, double weight) throws IllegalArgumentException, NoSuchElementException {
		if(!containsEdge(sourceVertexIndex, targetVertexIndex))
			throw new NoSuchElementException();
		adjMatrix[sourceVertexIndex][targetVertexIndex] = weight;
		adjMatrix[targetVertexIndex][sourceVertexIndex] = weight;
	}

	@Override
	public WeightedGraph getBellmanFordShortestPaths(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public WeightedGraph getDijkstraShortestPaths(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public WeightedGraph getPrimMST(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public WeightedGraph getKruskalMST() throws UnsupportedOperationException {
		try {
			return new KruskalForest().generateForest(this);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public WeightedGraph getFloydWarshallShortestPaths() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
