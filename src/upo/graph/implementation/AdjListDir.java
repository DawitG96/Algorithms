package upo.graph.implementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;

import upo.graph.base.Graph;
import upo.graph.base.VisitForest;
import upo.graph.base.VisitForest.Color;
import upo.graph.base.VisitForest.VisitType;

/**
 * Implementazione mediante <strong>liste di adiacenza</strong> di un grafo <strong>orientato non pesato</strong>.
 * 
 * @author Dawit Gulino 20013954
 *
 */
public class AdjListDir implements Graph{
	
	/**
	 * 
	 */
	private List<List<Integer>> adjList;
	
	/**
	 * 
	 */
	public AdjListDir() {
		this(0);
	}

	/**
	 * 
	 * @param numVertici
	 */
	public AdjListDir(int numVertici) {
		adjList = new ArrayList<>(numVertici);
		for(int i=0;i<numVertici;i++)
			addVertex();
	}
	
	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#addVertex()
	 */
	@Override
	public int addVertex() {
		adjList.add(adjList.size(), new ArrayList<>());
		return adjList.size()-1;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#containsVertex(int)
	 */
	@Override
	public boolean containsVertex(int index) {
		return index>=0 && index<adjList.size() ? true : false;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#removeVertex(int)
	 */
	@Override
	public void removeVertex(int index) throws NoSuchElementException {
		if(!containsVertex(index))
			throw new NoSuchElementException();
		
		adjList.remove(index);
		for(List<Integer> l : adjList) {
			for(int i=0; i<l.size();i++) {
				int value = l.get(i);
				if(value == index) {
					l.remove(i);
					i--;
				}
				else if(value>index)
					l.set(i, value-1);
			}
			
		} 
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException {
		if(!containsEdge(sourceVertexIndex,targetVertexIndex))
			adjList.get(sourceVertexIndex).add(targetVertexIndex);
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#containsEdge(int, int)
	 */
	@Override
	public boolean containsEdge(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException {
		if(!containsVertex(sourceVertexIndex) || !containsVertex(targetVertexIndex))
			throw new IllegalArgumentException();
		return adjList.get(sourceVertexIndex).contains(targetVertexIndex);
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#removeEdge(int, int)
	 */
	@Override
	public void removeEdge(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException, NoSuchElementException {
		if(!containsEdge(sourceVertexIndex, targetVertexIndex))
			throw new NoSuchElementException();
		adjList.get(sourceVertexIndex).remove(new Integer(targetVertexIndex));
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#getAdjacent(int)
	 */
	@Override
	public Set<Integer> getAdjacent(int vertexIndex) throws NoSuchElementException {
		Set<Integer> set = new HashSet<>();
		for(Integer l : adjList.get(vertexIndex))
			set.add(l);
		return set;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#isAdjacent(int, int)
	 */
	@Override
	public boolean isAdjacent(int targetVertexIndex, int sourceVertexIndex) throws IllegalArgumentException {
		if(!containsVertex(sourceVertexIndex) || !containsVertex(targetVertexIndex))
			throw new IllegalArgumentException();
		return adjList.get(sourceVertexIndex).contains(targetVertexIndex) == true ? true : false;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#size()
	 */
	@Override
	public int size() {
		return adjList.size();
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#isDirected()
	 */
	@Override
	public boolean isDirected() {
		return true;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#isCyclic()
	 */
	@Override
	public boolean isCyclic() {
		// Simil DFS
		
		boolean[] visited = new boolean[size()]; 
        boolean[] stacked = new boolean[size()]; 
        
        for (int i = 0; i < size(); i++) 
            if (isCyclicRec(i, visited, stacked)) 
                return true; 
		return false;
	}

	/**
	 * @param i
	 * @param visited come vertici neri 
	 * @param stacked tipo stack,quello che sto guardando ora, se si trova già in cima e lo "aggiungo" significa che si sta ciclando
	 * @return true : false
	 */
	private boolean isCyclicRec(int i, boolean[] visited, boolean[] stacked) {
		if(stacked[i])
			return true;
		if(visited[i])
			return false;
		visited[i] = true;
		
		stacked[i] = true;
		List<Integer> adiacents = adjList.get(i);
		for(Integer c: adiacents)
			if(isCyclicRec(c, visited, stacked))
				return true;
		
		stacked[i] = false;
		return false; 
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#isDAG()
	 */
	@Override
	public boolean isDAG() {
		return isDirected() && !isCyclic()? true : false;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#getBFSTree(int)
	 */
	@Override
	public VisitForest getBFSTree(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		if(!containsVertex(startingVertex))
			throw new IllegalArgumentException();
		
		VisitForest visita = new VisitForest(this, VisitType.BFS);
		List<Integer> coda = new LinkedList<>();
		coda.add(startingVertex);
		int time = 0;
		
		visita.setStartTime(startingVertex, time++);
		
		while(!coda.isEmpty()) {
			int vertex = coda.remove(0);
			
			Integer parent = visita.getPartent(vertex);
			if(parent == null) parent = 0; 
			Double distance = visita.getDistance(parent);
			distance = (distance == null) ? 0 : distance+1;
			
			visita.setColor(vertex, Color.GRAY);
			visita.setDistance(vertex, distance);
			
			/* ordine alfabetico kinda~ UwU */
			List<Integer> sorted = new ArrayList<>(adjList.get(vertex));
			sorted.sort(null);
						
			for(Integer adj : sorted) {
				if(visita.getColor(adj).equals(Color.WHITE)) {
					visita.setStartTime(adj, time++);
					visita.setParent(adj, vertex);
					coda.add(adj);
				}
			}
			
			visita.setColor(vertex, Color.BLACK);
			visita.setEndTime(vertex, time++);
		}
		return visita;
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
				getDFSImpl(i, time, visita);
		return visita;
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
			List<Integer> sorted = new ArrayList<>(adjList.get(vertex));
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
	 * @see upo.graph.base.Graph#topologicalSort()
	 */
	@Override
	public int[] topologicalSort() throws UnsupportedOperationException {
		if(!isDAG())
			throw new UnsupportedOperationException();
		
		VisitForest dfstot = getDFSTOTForest(0);
		
		// costruisci array dei vertici in base all'ordine dei tempi di fine visita decrescente
		int[] arr = new int[size()];
		for(int i = 0; i<size();i++) {
			int max = 0;
			for(int j=1; j< size(); j++) 
				if(dfstot.getEndTime(j) > dfstot.getEndTime(max))
					max = j;
			dfstot.setEndTime(max, -1);
			arr[i] = max;
		}
		return arr;
	}
	
	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#stronglyConnectedComponents()
	 */
	@Override
	public Set<Set<Integer>> stronglyConnectedComponents() throws UnsupportedOperationException {
		if(!isDirected())
			throw new UnsupportedOperationException();
		
		//Pensavo fosse dfs+gt+dfs e invece KOSARAJU
		// DFS TOT
		VisitForest dfstot = getDFSTOTForest(0);
		
		// costruisci array dei vertici in base all'ordine dei tempi di fine visita decrescente
		int[] arr = new int[size()];
		for(int i = 0; i<size();i++) {
			int max = 0;
			for(int j=1; j< size(); j++) 
				if(dfstot.getEndTime(j) > dfstot.getEndTime(max))
					max = j;
			dfstot.setEndTime(max, -1);
			arr[i] = max;
		}
		
		// G transposed
		AdjListDir dragonBallGT = new AdjListDir(size());
		for(int i = 0; i<size(); i++)
			for(int vert: adjList.get(i))
				dragonBallGT.addEdge(vert, i);
		
		// DFS su GT usando l'array creato prima
		Set<Set<Integer>> scc = new HashSet<>();
		boolean[] black = new boolean[size()];
		VisitForest vf = new VisitForest(dragonBallGT, VisitType.DFS_TOT);
		
		for(int i = 0; i < size(); i++) {
			if(vf.getColor(arr[i]).equals(Color.WHITE)) {
				// Ogni volta che finisce una DFS aggiungi tutti i vertici al SCC
				dragonBallGT.getDFSImpl(arr[i], 0, vf);
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

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#connectedComponents()
	 */
	@Override
	public Set<Set<Integer>> connectedComponents() throws UnsupportedOperationException {
		if(isDirected())
			throw new UnsupportedOperationException();
		return null;
	}

	/* (non-Javadoc)
	 * @see upo.graph.base.Graph#getDFSTOTForest(int[])
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
}
