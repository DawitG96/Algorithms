/**
 * 
 */
package upo.graph.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import upo.additionalstructures.UnionFind;
import upo.graph.base.VisitForest;
import upo.graph.base.VisitForest.Color;
import upo.graph.base.VisitForest.VisitType;
import upo.graph.base.WeightedGraph;
import upo.graph.implementation.AdjMatrixUndirWeight;
import upo.graph.implementation.UnionFindImpl;

/**
 * @author Dawit Gulino
 *
 */
public class MatrixJUnitTest {
	
	WeightedGraph graph;
	
	@Before
	public void before() {
		graph = new AdjMatrixUndirWeight(4);
	}
	
	
	@Test
	public void testCostructor() {
		graph = new AdjMatrixUndirWeight();
		assertEquals(0,graph.size());
		graph.addVertex();
		assertEquals(1,graph.size());
		
		graph = new AdjMatrixUndirWeight(2);
		assertEquals(2,graph.size());
		graph.removeVertex(0);
		assertEquals(1,graph.size());
		graph.removeVertex(0);
		assertEquals(0,graph.size());
		
		assertFalse(graph.isDirected());
	}
	
	@Test
	public void testWeight() {
		/* 
		 * 0 | 1
		 * 1 | 2 3
		 * 2 | null
		 * 3 | 1 2
		 */
		graph.addEdge(0, 1);
		graph.addEdge(1, 2);
		graph.addEdge(1, 3);
		graph.addEdge(3, 1);
		graph.addEdge(3, 2);

		assertEquals(WeightedGraph.defaultEdgeWeight, graph.getEdgeWeight(0,1), 0);
		assertEquals(WeightedGraph.defaultEdgeWeight, graph.getEdgeWeight(1,0), 0);
		assertEquals(WeightedGraph.defaultEdgeWeight, graph.getEdgeWeight(1,2), 0);
		assertEquals(WeightedGraph.defaultEdgeWeight, graph.getEdgeWeight(2,1), 0);
		assertEquals(WeightedGraph.defaultEdgeWeight, graph.getEdgeWeight(1,3), 0);
		assertEquals(WeightedGraph.defaultEdgeWeight, graph.getEdgeWeight(3,1), 0);
		assertEquals(WeightedGraph.defaultEdgeWeight, graph.getEdgeWeight(2,3), 0);
		assertEquals(WeightedGraph.defaultEdgeWeight, graph.getEdgeWeight(3,2), 0);
		
		try {
			graph.setEdgeWeight(0, 2, -3);
			fail("Deve sollevare una eccezione");
		} catch(NoSuchElementException e) {}
		
		graph.setEdgeWeight(0, 1, 3);
		graph.setEdgeWeight(3, 2, 23);
		
		assertEquals(WeightedGraph.defaultEdgeWeight, graph.getEdgeWeight(3,1), 0);
		assertEquals(3, graph.getEdgeWeight(0,1), 0);
		assertEquals(3, graph.getEdgeWeight(1,0), 0);
		assertEquals(23, graph.getEdgeWeight(3,2), 0);
		assertEquals(23, graph.getEdgeWeight(2,3), 0);
		
		graph.setEdgeWeight(2,1, 0);
		graph.setEdgeWeight(3, 2, -13);
		
		assertEquals(0, graph.getEdgeWeight(2,1), 0);
		assertEquals(0, graph.getEdgeWeight(1,2), 0);
		assertEquals(-13, graph.getEdgeWeight(3,2), 0);
		assertEquals(-13, graph.getEdgeWeight(2,3), 0);
	}
	
	@Test
	public void testContains() {
		assertTrue("Nope",graph.containsVertex(0));
		assertTrue("Nope",graph.containsVertex(1));
		assertTrue("Nope",graph.containsVertex(2));
		assertTrue("Nope",graph.containsVertex(3));
		assertFalse(graph.containsVertex(4));
		
		assertFalse("Error, vertex exists",graph.containsVertex(5));
		assertFalse("Error, vertex exists",graph.containsVertex(23));
		assertFalse("Error, vertex exists",graph.containsVertex(66));
		
		/* 
		 * 0 | 1
		 * 1 | 0 1 2 3
		 * 2 | 1 3
		 * 3 | 1 2
		 */
		graph.addEdge(0, 1);
		graph.addEdge(1, 1);
		graph.addEdge(1, 2);
		graph.addEdge(1, 3);
		graph.addEdge(3, 2);
		
		assertTrue("Nope",graph.containsEdge(0,1));
		assertTrue("Nope",graph.containsEdge(1,0));
		assertTrue("Nope",graph.containsEdge(1,1));
		assertTrue("Nope",graph.containsEdge(1,2));
		assertTrue("Nope",graph.containsEdge(2,1));
		assertTrue("Nope",graph.containsEdge(1,3));
		assertTrue("Nope",graph.containsEdge(3,1));
		assertTrue("Nope",graph.containsEdge(3,2));
		assertTrue("Nope",graph.containsEdge(2,3));
		assertFalse("False, edge exists",graph.containsEdge(0,0));
		assertFalse("False, edge exists",graph.containsEdge(0,2));
		assertFalse("False, edge exists",graph.containsEdge(2,0));
		assertFalse("False, edge exists",graph.containsEdge(0,3));
		assertFalse("False, edge exists",graph.containsEdge(3,0));
		assertFalse("False, edge exists",graph.containsEdge(2,2));
		assertFalse("False, edge exists",graph.containsEdge(3,3));
	}
	
	@Test
	public void testRemove() {
		assertEquals(4,graph.size());
		graph.addVertex();
		assertEquals(5,graph.size());
		
		/* 
		 * 0 | 1 4
		 * 1 | 0 2 3 4
		 * 2 | 1 4 2
		 * 3 | 1
		 * 4 | 0 1 2
		 */
		graph.addEdge(0, 1);
		graph.addEdge(1, 2);
		graph.addEdge(1, 3);
		graph.addEdge(2, 2);
		graph.addEdge(4, 0);
		graph.addEdge(4, 1);
		graph.addEdge(4, 2);

		assertTrue("Nope",graph.containsEdge(0,1));
		assertTrue("Nope",graph.containsEdge(2,2));
		
		graph.removeVertex(2);
		/* 
		 * 0 | 1 3
		 * 1 | 0 2 3
		 * 2 | 1
		 * 3 | 0 1
		 */
		assertEquals(4,graph.size());
		assertTrue("Nope",graph.containsEdge(0,1));
		assertTrue("Nope",graph.containsEdge(1,2));
		assertTrue("Nope",graph.containsEdge(2,1));
		assertTrue("Nope",graph.containsEdge(3,0));
		assertTrue("Nope",graph.containsEdge(3,1));
		
		assertFalse("Error, vertex shouldn't exists",graph.containsVertex(4));
		assertFalse("False, edge still exists",graph.containsEdge(2, 2));
		assertFalse("False, edge still exists",graph.containsEdge(3, 2));
		assertFalse("False, edge still exists",graph.containsEdge(2, 3));
		
		graph.removeEdge(2, 1);
		assertFalse("False, edge still exists",graph.containsEdge(2,1));
		
		graph.addEdge(2, 1);
		assertTrue("Nope",graph.containsEdge(2,1));
	}
	
	@Test
	public void testCyclic() {
		assertFalse("Cycle found", graph.isCyclic());

		graph.addEdge(0, 1);
		assertTrue("No cycle found", graph.isCyclic());
	}
	
	@Test
	public void testCC() {
		graph = createGraphVisits();
		
		Set<Set<Integer>> scc = new HashSet<>();
		Set<Integer> cc = new HashSet<>();
		cc.add(0);
		cc.add(1);
		cc.add(2);
		cc.add(3);
		scc.add(cc);
		cc = new HashSet<>();
		cc.add(4);
		cc.add(5);
		cc.add(7);
		cc.add(8);
		cc.add(10);
		cc.add(11);
		cc.add(12);
		scc.add(cc);
		cc = new HashSet<>();
		cc.add(6);
		cc.add(9);
		scc.add(cc);
		
		Set<Set<Integer>> actualCC = graph.connectedComponents();
		assertEquals("size", scc.size(), actualCC.size());
		assertEquals("Object", scc, actualCC);
		
		/*
		for(Set<Integer> expected : scc) {
			boolean ok = false;
			for(Set<Integer> actual : actualCC)
				if(expected.toString().equals(actual.toString()))
					ok = true;
			if(!ok)
				fail("Expected: " + expected);
		}
		*/
	}
	
	@Test
	public void testBFS() {
		try {
			createGraphVisits().getBFSTree(2);
			fail("Sould try UnsupportedOperationException");
		} catch(UnsupportedOperationException e) {};
	}
	
	@Test
	public void testDFS() {
		graph = createGraphVisits();
		
		VisitForest v = graph.getDFSTree(2);
		assertEquals(VisitType.DFS, v.visitType);
		
		for(int vert = 0; vert < 4; vert++)
			assertEquals(Color.BLACK, v.getColor(vert));
		
		//forabile
		assertEquals(0, v.getStartTime(2));
		assertEquals(1, v.getStartTime(0));
		assertEquals(2, v.getStartTime(1));
		assertEquals(3, v.getEndTime(1));
		assertEquals(4, v.getEndTime(0));
		assertEquals(5, v.getStartTime(3));
		assertEquals(6, v.getEndTime(3));
		assertEquals(7, v.getEndTime(2));
		
		assertEquals(new Integer(2), v.getPartent(0));
		assertEquals(new Integer(0), v.getPartent(1));
		assertEquals(null, v.getPartent(2));
		assertEquals(new Integer(2), v.getPartent(3));
		
		assertEquals(new Double(1), v.getDistance(0));
		assertEquals(new Double(2), v.getDistance(1));
		assertEquals(new Double(0), v.getDistance(2));
		assertEquals(new Double(1), v.getDistance(3));
		
		for(int vert = 4; vert < graph.size(); vert++) {
			assertEquals(Color.WHITE, v.getColor(vert));
			assertEquals(-1, v.getStartTime(vert));
			assertEquals(-1, v.getEndTime(vert));
			assertEquals(null, v.getPartent(vert));
			assertEquals(null, v.getDistance(vert));
		}
		
		v = graph.getDFSTree(4);
		assertEquals(VisitType.DFS, v.visitType);
		
		for(int vert = 4; vert < graph.size(); vert++)
			if(vert!=6 && vert!=9)
				assertEquals(Color.BLACK, v.getColor(vert));
		
		assertEquals(0, v.getStartTime(4));
		assertEquals(1, v.getStartTime(7));
		assertEquals(2, v.getStartTime(5));
		assertEquals(3, v.getStartTime(8));
		assertEquals(4, v.getEndTime(8));
		assertEquals(5, v.getStartTime(10));
		assertEquals(6, v.getStartTime(11));
		assertEquals(7, v.getStartTime(12));
		assertEquals(8, v.getEndTime(12));
		assertEquals(9, v.getEndTime(11));
		assertEquals(10, v.getEndTime(10));
		assertEquals(11, v.getEndTime(5));
		assertEquals(12, v.getEndTime(7));
		assertEquals(13, v.getEndTime(4));
		
		assertEquals(null, v.getPartent(4));
		assertEquals(new Integer(4), v.getPartent(7));
		assertEquals(new Integer(7), v.getPartent(5));
		assertEquals(new Integer(5), v.getPartent(8));
		assertEquals(new Integer(5), v.getPartent(10));
		assertEquals(new Integer(10), v.getPartent(11));
		assertEquals(new Integer(11), v.getPartent(12));
		
		assertEquals(new Double(0), v.getDistance(4));
		assertEquals(new Double(1), v.getDistance(7));
		assertEquals(new Double(2), v.getDistance(5));
		assertEquals(new Double(3), v.getDistance(8));
		assertEquals(new Double(3), v.getDistance(10));
		assertEquals(new Double(4), v.getDistance(11));
		assertEquals(new Double(5), v.getDistance(12));
		
		for(int vert = 0; vert < 9; vert++)
			if(vert!=4 && vert!=5 && vert!=7 && vert!=8) {
				assertEquals(Color.WHITE, v.getColor(vert));
				assertEquals(-1, v.getStartTime(vert));
				assertEquals(-1, v.getEndTime(vert));
				assertEquals(null, v.getPartent(vert));
				assertEquals(null, v.getDistance(vert));
			}
	}
	
	@Test
	public void testDFSThot() {
		graph = createGraphVisits();
		
		VisitForest v = graph.getDFSTOTForest(0);
		assertEquals(VisitType.DFS_TOT, v.visitType);
		
		for(int vert = 0; vert < 4; vert++)
			assertEquals(Color.BLACK, v.getColor(vert));
		
		assertEquals(0, v.getStartTime(0));
		assertEquals(1, v.getStartTime(1));
		assertEquals(2, v.getStartTime(2));
		assertEquals(3, v.getStartTime(3));
		assertEquals(4, v.getEndTime(3));
		assertEquals(5, v.getEndTime(2));
		assertEquals(6, v.getEndTime(1));
		assertEquals(7, v.getEndTime(0));
		assertEquals(8, v.getStartTime(4));
		assertEquals(9, v.getStartTime(7));
		assertEquals(10, v.getStartTime(5));
		assertEquals(11, v.getStartTime(8));
		assertEquals(12, v.getEndTime(8));
		assertEquals(13, v.getStartTime(10));
		assertEquals(14, v.getStartTime(11));
		assertEquals(15, v.getStartTime(12));
		assertEquals(16, v.getEndTime(12));
		assertEquals(17, v.getEndTime(11));
		assertEquals(18, v.getEndTime(10));
		assertEquals(19, v.getEndTime(5));
		assertEquals(20, v.getEndTime(7));
		assertEquals(21, v.getEndTime(4));
		assertEquals(22, v.getStartTime(6));
		assertEquals(23, v.getStartTime(9));
		assertEquals(24, v.getEndTime(9));
		assertEquals(25, v.getEndTime(6));
		
		assertEquals(null, v.getPartent(0));
		assertEquals(new Integer(0), v.getPartent(1));
		assertEquals(new Integer(1), v.getPartent(2));
		assertEquals(new Integer(2), v.getPartent(3));
		assertEquals(null, v.getPartent(4));
		assertEquals(new Integer(4), v.getPartent(7));
		assertEquals(new Integer(7), v.getPartent(5));
		assertEquals(new Integer(5), v.getPartent(8));
		assertEquals(new Integer(5), v.getPartent(10));
		assertEquals(new Integer(10), v.getPartent(11));
		assertEquals(new Integer(11), v.getPartent(12));
		assertEquals(null, v.getPartent(6));
		assertEquals(new Integer(6), v.getPartent(9));
		
		assertEquals(new Double(0), v.getDistance(0));
		assertEquals(new Double(1), v.getDistance(1));
		assertEquals(new Double(2), v.getDistance(2));
		assertEquals(new Double(3), v.getDistance(3));
		assertEquals(new Double(0), v.getDistance(4));
		assertEquals(new Double(1), v.getDistance(7));
		assertEquals(new Double(2), v.getDistance(5));
		assertEquals(new Double(3), v.getDistance(8));
		assertEquals(new Double(3), v.getDistance(10));
		assertEquals(new Double(4), v.getDistance(11));
		assertEquals(new Double(5), v.getDistance(12));
		assertEquals(new Double(0), v.getDistance(6));
		assertEquals(new Double(1), v.getDistance(9));
		
		/* SECVONDA VISITA */
		
		v = graph.getDFSTOTForest(new int[]{9, 1, 11});
		assertEquals(VisitType.DFS_TOT, v.visitType);
		
		for(int vert = 0; vert < graph.size(); vert++)
			assertEquals(Color.BLACK, v.getColor(vert));

		assertEquals(0, v.getStartTime(9));
		assertEquals(1, v.getStartTime(6));
		assertEquals(2, v.getEndTime(6));
		assertEquals(3, v.getEndTime(9));
		assertEquals(4, v.getStartTime(1));
		assertEquals(5, v.getStartTime(0));
		assertEquals(6, v.getStartTime(2));
		assertEquals(7, v.getStartTime(3));
		assertEquals(8, v.getEndTime(3));
		assertEquals(9, v.getEndTime(2));
		assertEquals(10, v.getEndTime(0));
		assertEquals(11, v.getEndTime(1));
		assertEquals(12, v.getStartTime(11));
		assertEquals(13, v.getStartTime(4));
		assertEquals(14, v.getStartTime(7));
		assertEquals(15, v.getStartTime(5));
		assertEquals(16, v.getStartTime(8));
		assertEquals(17, v.getEndTime(8));
		assertEquals(18, v.getStartTime(10));
		assertEquals(19, v.getEndTime(10));
		assertEquals(20, v.getEndTime(5));
		assertEquals(21, v.getEndTime(7));
		assertEquals(22, v.getEndTime(4));
		assertEquals(23, v.getStartTime(12));
		assertEquals(24, v.getEndTime(12));
		assertEquals(25, v.getEndTime(11));
		
		assertEquals(null, v.getPartent(9));
		assertEquals(new Integer(9), v.getPartent(6));
		assertEquals(null, v.getPartent(1));
		assertEquals(new Integer(1), v.getPartent(0));
		assertEquals(new Integer(0), v.getPartent(2));
		assertEquals(new Integer(2), v.getPartent(3));
		assertEquals(null, v.getPartent(11));
		assertEquals(new Integer(11), v.getPartent(4));
		assertEquals(new Integer(4), v.getPartent(7));
		assertEquals(new Integer(7), v.getPartent(5));
		assertEquals(new Integer(5), v.getPartent(8));
		assertEquals(new Integer(5), v.getPartent(10));
		assertEquals(new Integer(11), v.getPartent(12));
		
		assertEquals(new Double(0), v.getDistance(9));
		assertEquals(new Double(1), v.getDistance(6));
		assertEquals(new Double(0), v.getDistance(1));
		assertEquals(new Double(1), v.getDistance(0));
		assertEquals(new Double(2), v.getDistance(2));
		assertEquals(new Double(3), v.getDistance(3));
		assertEquals(new Double(0), v.getDistance(11));
		assertEquals(new Double(1), v.getDistance(4));
		assertEquals(new Double(2), v.getDistance(7));
		assertEquals(new Double(3), v.getDistance(5));
		assertEquals(new Double(4), v.getDistance(8));
		assertEquals(new Double(4), v.getDistance(10));
		assertEquals(new Double(1), v.getDistance(12));
	}
	
	@Test
	public void testKruskal() {
		graph = new AdjMatrixUndirWeight(5);
		graph.addEdge(0, 1);
		graph.setEdgeWeight(0, 1, 3);
		graph.addEdge(0, 3);
		graph.setEdgeWeight(0, 3, 1);
		graph.addEdge(0, 2);
		graph.setEdgeWeight(0, 2, 8);
		graph.addEdge(1, 2);
		graph.setEdgeWeight(1, 2, 2);
		graph.addEdge(1, 4);
		graph.setEdgeWeight(1, 4, 1);
		graph.addEdge(2, 3);
		graph.setEdgeWeight(2, 3, 5);
		graph.addEdge(2, 4);
		graph.setEdgeWeight(2, 4, 2);
		graph.addEdge(3, 4);
		graph.setEdgeWeight(3, 4, 5);
		
		WeightedGraph mar = graph.getKruskalMST();
		assertEquals(5, mar.size());
		assertEquals(new Double(1), new Double(mar.getEdgeWeight(0, 3)));
		assertEquals(new Double(3), new Double(mar.getEdgeWeight(0, 1)));
		assertEquals(new Double(2), new Double(mar.getEdgeWeight(1, 2)));
		assertEquals(new Double(1), new Double(mar.getEdgeWeight(1, 4)));
		
		assertFalse(mar.containsEdge(0, 2));
		assertFalse(mar.containsEdge(2, 3));
		assertFalse(mar.containsEdge(2, 4));
		assertFalse(mar.containsEdge(3, 4));
		
		
	}
	
	@Test
	public void testUnionFind() {
		UnionFind<Integer> uuf = new UnionFindImpl();
		
		assertEquals(null,uuf.find(0));
		uuf.makeSet(0);
		assertEquals(new Integer(0),uuf.find(0));
		assertEquals(null,uuf.find(1));
		uuf.makeSet(1);
		assertEquals(new Integer(1),uuf.find(1));
		assertEquals(null,uuf.find(2));
		uuf.makeSet(2);
		assertEquals(new Integer(2),uuf.find(2));
		assertEquals(null,uuf.find(3));
		uuf.makeSet(3);
		assertEquals(new Integer(3),uuf.find(3));
		assertEquals(null,uuf.find(4));
		uuf.makeSet(4);
		assertEquals(new Integer(4),uuf.find(4));
		
		uuf.union(uuf.find(0), uuf.find(1));
		assertEquals(new Integer(0), uuf.find(0));
		assertEquals(uuf.find(0), uuf.find(1));
		
		uuf.union(uuf.find(0), uuf.find(2));
		assertEquals(new Integer(0), uuf.find(0));
		assertEquals(uuf.find(0), uuf.find(2));
		
		uuf.union(uuf.find(4), uuf.find(3));
		assertEquals(new Integer(4), uuf.find(4));
		assertEquals(uuf.find(4), uuf.find(3));
		
		uuf.union(uuf.find(3), uuf.find(1));
		assertEquals(new Integer(4), uuf.find(3));
		assertEquals(uuf.find(3), uuf.find(1));
		
		
		assertEquals(new Integer(4), uuf.find(0));
		assertEquals(new Integer(4), uuf.find(1));
		assertEquals(new Integer(4), uuf.find(2));
		assertEquals(new Integer(4), uuf.find(3));
		assertEquals(new Integer(4), uuf.find(4));	
	}
	
	@Ignore
	private WeightedGraph createGraphVisits() {
		//http://graphonline.ru/en/?graph=wjDkhrLTBvtJiPoB
		
		WeightedGraph graph = new AdjMatrixUndirWeight(13);
		graph.addEdge(0, 1);
		graph.setEdgeWeight(0, 1, 1);
		graph.addEdge(0, 2);
		graph.setEdgeWeight(0, 2, 3);
		graph.addEdge(1, 2);
		graph.setEdgeWeight(1, 2, 6);
		graph.addEdge(2, 3);
		graph.setEdgeWeight(2, 3, 4);
		
		graph.addEdge(6, 9);
		graph.setEdgeWeight(6, 9, 1);
		
		graph.addEdge(4, 7);
		graph.setEdgeWeight(4, 7, 3);
		graph.addEdge(4, 8);
		graph.setEdgeWeight(4, 8, 5);
		graph.addEdge(4, 11);
		graph.setEdgeWeight(4, 11, 12);
		graph.addEdge(7, 5);
		graph.setEdgeWeight(7, 5, 2);
		graph.addEdge(8, 5);
		graph.setEdgeWeight(8, 5, 1);
		graph.addEdge(10, 5);
		graph.setEdgeWeight(10, 5, 4);
		graph.addEdge(10, 11);
		graph.setEdgeWeight(10, 11, 1);
		graph.addEdge(11, 12);
		graph.setEdgeWeight(11, 12, 2);
		
		return graph;
	}
	
	@Ignore
	private void printGraph() {
		for(int i = 0; i<graph.size();i++) {
			System.out.println(i+" -> "+graph.getAdjacent(i));
		}
	}
}
