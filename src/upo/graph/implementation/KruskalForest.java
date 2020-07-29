/**
 * 
 */
package upo.graph.implementation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import upo.graph.base.WeightedGraph;

/**
 * @author Dawit Gulino
 *
 */
public class KruskalForest {
	
	/**
	 * 
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public WeightedGraph generateForest(WeightedGraph graph) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		/* 
		 * archi per ordine non decrescente per peso
		 * faccio i makeset in ordine
		 * per ognuno
		 * 	se find diverse allora unisco e salvo l'arco alla soluzione */
		
		if(graph.isDirected())
			throw new IllegalArgumentException();
		
		WeightedGraph returned = graph.getClass().getConstructor().newInstance();
		UnionFindImpl uuf = new UnionFindImpl();
		List<Edge> edges = new ArrayList<>();
	
		for(int i=0;i<graph.size();i++) {
			returned.addVertex();
			uuf.makeSet(i);
		}
		
		for(int i=0;i<graph.size();i++)
			for(Integer adj : graph.getAdjacent(i))
				edges.add(new Edge(i,adj,graph.getEdgeWeight(i, adj)));
		Collections.sort(edges);
		
		for(Edge e : edges) {
			Integer val1 = uuf.find(e.v1);
			Integer val2 = uuf.find(e.v2);
			if(val1!=val2) {
				uuf.union(val1, val2);
				returned.addEdge(e.v1,e.v2);
				returned.setEdgeWeight(e.v1, e.v2, e.weight);
			}
		}
		return returned;
	}
	
	
	private class Edge implements Comparable<Edge> {
		public final int v1,v2;
		public final double weight;
		
		private Edge(int a, int b, double weight) {
			this.weight=weight;
			v1=a;
			v2=b;
		}

		@Override
		public int compareTo(Edge o) {
			double val = this.weight - o.weight;
			if(val > 0)
				return 1;
			else if(val < 0)
				return -1;
			return 0;
		}
		
		@Override
		public String toString() {
			return "( "+v1+" - "+v2+" : "+weight+" )";
		}
	}
}
