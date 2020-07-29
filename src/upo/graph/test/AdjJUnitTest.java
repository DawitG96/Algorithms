/**
 * 
 */
package upo.graph.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import upo.graph.base.Graph;
import upo.graph.base.VisitForest;
import upo.graph.base.VisitForest.Color;
import upo.graph.base.VisitForest.VisitType;
import upo.graph.implementation.AdjListDir;

/**
 * @author Dawit Gulino
 *
 */
public class AdjJUnitTest {

	Graph graph;
	
	@Before
	public void before() {
		graph = new AdjListDir(4);
		graph.addVertex();
		/* 
		 * 0 | 1
		 * 1 | 2 3
		 * 2 | null
		 * 3 | 1 2 4
		 * 4 | 0 2 1
		 */
		graph.addEdge(0, 1);
		graph.addEdge(1, 2);
		graph.addEdge(1, 3);
		graph.addEdge(3, 1);
		graph.addEdge(3, 2);
		graph.addEdge(3, 4);
		graph.addEdge(4, 0);
		graph.addEdge(4, 1);
		graph.addEdge(4, 2);
	}
	
	@Test
	public void testContains() {
		assertTrue("Nope",graph.containsVertex(0));
		assertTrue("Nope",graph.containsVertex(1));
		assertTrue("Nope",graph.containsVertex(2));
		assertTrue("Nope",graph.containsVertex(3));
		assertTrue("Nope",graph.containsVertex(4));
		assertFalse("Error, vertex exists",graph.containsVertex(5));
		assertFalse("Error, vertex exists",graph.containsVertex(23));
		assertFalse("Error, vertex exists",graph.containsVertex(66));
		
		assertTrue("Nope",graph.containsEdge(0, 1));
		assertTrue("Nope",graph.containsEdge(1,2) && graph.containsEdge(1,3));
		assertTrue("Nope",graph.containsEdge(3, 2));		
		assertFalse("False, edge exists",graph.containsEdge(0,0));
		assertFalse("False, edge exists",graph.containsEdge(2, 1));
		assertFalse("False, edge exists",graph.containsEdge(4,4));
	}
	
	@Test
	public void testRemove() {
		graph.removeVertex(2);
		/* 
		 * 0 | 1
		 * 1 | 2
		 * 2 | 1 3
		 * 3 | 0 1
		 */
		assertEquals(4,graph.size());
		assertTrue("Nope",graph.containsEdge(0,1));
		assertTrue("Nope",graph.containsEdge(1,2));
		assertTrue("Nope",graph.containsEdge(2,1));
		assertTrue("Nope",graph.containsEdge(2,3));
		assertTrue("Nope",graph.containsEdge(3,0));
		assertTrue("Nope",graph.containsEdge(3,1));
		
		assertFalse("Error, vertex shouldn't exists",graph.containsVertex(4));
		assertFalse("False, edge still exists",graph.containsEdge(2, 2));
		assertFalse("False, edge still exists",graph.containsEdge(3, 2));
		
		graph.removeEdge(2, 3);
		assertFalse("False, edge still exists",graph.containsEdge(2,3));
		
		graph.addEdge(2, 3);
		assertTrue("Nope",graph.containsEdge(2,3));
	}
	
	@Test
	public void testCyclic() {
		assertTrue("No cycle found", graph.isCyclic());
		
		graph.removeVertex(1);
		/* 
		 * 0 | null
		 * 1 | 2
		 * 2 | null
		 */
		assertFalse("Cycle found", graph.isCyclic());
	}
	
	@Test
	public void testTopologicalSort() {
		graph = new AdjListDir(6);
		if(!graph.isDirected())
			return;
		
		graph.addEdge(0, 3);
		graph.addEdge(0, 4);
		graph.addEdge(1, 5);
		graph.addEdge(2, 3);
		graph.addEdge(2, 4);
		graph.addEdge(3, 1);
		graph.addEdge(3, 5);
		graph.addEdge(4, 1);
		/* 
		 * 0 | 3 4
		 * 1 | 5
		 * 2 | 3 4
		 * 3 | 1 5
		 * 4 | 1
		 * 5 | null
		 */
		ArrayList<String> valid = new ArrayList<>();
		valid.add("[0, 2, 3, 4, 1, 5]");
		valid.add("[2, 0, 4, 3, 1, 5]");
		int[] ts = graph.topologicalSort();
		/* Multeplici soluzioni [0, 2, 3, 4, 1, 5]; [2, 0, 4, 3, 1, 5] */
		assertTrue(valid.contains(Arrays.toString(ts)));
		
		graph = createGraphVisits();
		graph.removeEdge(2, 0);
		graph.removeEdge(3, 3);
		ts = graph.topologicalSort();
		assertEquals("[4, 0, 1, 2, 3]", Arrays.toString(ts));
	}
	
	@Test
	public void testSCC() {
		graph = createGraphVisits();
		if(!graph.isDirected())
			return;
		
		Set<Set<Integer>> scc = new HashSet<>();
		Set<Integer> cc = new HashSet<>();
		cc.add(0);
		cc.add(1);
		cc.add(2);
		scc.add(cc);
		cc = new HashSet<>();
		cc.add(3);
		scc.add(cc);
		cc = new HashSet<>();
		cc.add(4);
		scc.add(cc);
		
		Set<Set<Integer>> actual = graph.stronglyConnectedComponents();
		
		assertEquals("size", scc.size(), actual.size());
		assertEquals("Object", scc,actual);
	}
	
	@Test
	public void testBFS() {
		graph = createGraphVisits();
		
		VisitForest v = graph.getBFSTree(2);
		assertEquals(VisitType.BFS, v.visitType);
		
		for(int vert = 0; vert < graph.size() - 1; vert++)
			assertEquals(Color.BLACK, v.getColor(vert));
		
		assertEquals(0, v.getStartTime(2));
		assertEquals(1, v.getStartTime(0));
		assertEquals(2, v.getStartTime(3));
		assertEquals(3, v.getEndTime(2));
		assertEquals(4, v.getStartTime(1));
		assertEquals(5, v.getEndTime(0));
		assertEquals(6, v.getEndTime(3));
		assertEquals(7, v.getEndTime(1));
		assertEquals(-1, v.getStartTime(4));
		assertEquals(-1, v.getEndTime(4));
		
		assertEquals(new Integer(2), v.getPartent(0));
		assertEquals(new Integer(0), v.getPartent(1));
		assertEquals(null, v.getPartent(2));
		assertEquals(new Integer(2), v.getPartent(3));
		assertEquals(null, v.getPartent(4));
		
		assertEquals(new Double(1), v.getDistance(0));
		assertEquals(new Double(2), v.getDistance(1));
		assertEquals(new Double(0), v.getDistance(2));
		assertEquals(new Double(1), v.getDistance(3));
		assertEquals(null, v.getDistance(4));
	}
	
	@Test
	public void testDFS() {
		graph = createGraphVisits();
		
		VisitForest v = graph.getDFSTree(0);
		assertEquals(VisitType.DFS, v.visitType);
		
		for(int vert = 0; vert < graph.size() - 1; vert++)
			assertEquals("Vertex " + vert, Color.BLACK, v.getColor(vert));
		
		assertEquals(0, v.getStartTime(0));
		assertEquals(1, v.getStartTime(1));
		assertEquals(2, v.getStartTime(2));
		assertEquals(3, v.getStartTime(3));
		assertEquals(4, v.getEndTime(3));
		assertEquals(5, v.getEndTime(2));
		assertEquals(6, v.getEndTime(1));
		assertEquals(7, v.getEndTime(0));
		assertEquals(-1, v.getStartTime(4));
		assertEquals(-1, v.getEndTime(4));
		
		assertEquals(null, v.getPartent(0));
		assertEquals(new Integer(0), v.getPartent(1));
		assertEquals(new Integer(1), v.getPartent(2));
		assertEquals(new Integer(2), v.getPartent(3));
		assertEquals(null, v.getPartent(4));
		
		assertEquals(new Double(0), v.getDistance(0));
		assertEquals(new Double(1), v.getDistance(1));
		assertEquals(new Double(2), v.getDistance(2));
		assertEquals(new Double(3), v.getDistance(3));
		assertEquals(null, v.getDistance(4));
		
		v = graph.getDFSTree(2);

		assertEquals(VisitType.DFS, v.visitType);
		
		for(int vert = 0; vert < graph.size() - 1; vert++)
			assertEquals(Color.BLACK, v.getColor(vert));
		
		assertEquals(0, v.getStartTime(2));
		assertEquals(1, v.getStartTime(0));
		assertEquals(2, v.getStartTime(1));
		assertEquals(3, v.getEndTime(1));
		assertEquals(4, v.getEndTime(0));
		assertEquals(5, v.getStartTime(3));
		assertEquals(6, v.getEndTime(3));
		assertEquals(7, v.getEndTime(2));
		assertEquals(-1, v.getStartTime(4));
		assertEquals(-1, v.getEndTime(4));
		
		assertEquals(new Integer(2), v.getPartent(0));
		assertEquals(new Integer(0), v.getPartent(1));
		assertEquals(null, v.getPartent(2));
		assertEquals(new Integer(2), v.getPartent(3));
		assertEquals(null, v.getPartent(4));
		
		assertEquals(new Double(1), v.getDistance(0));
		assertEquals(new Double(2), v.getDistance(1));
		assertEquals(new Double(0), v.getDistance(2));
		assertEquals(new Double(1), v.getDistance(3));
		assertEquals(null, v.getDistance(4));
	}
	
	@Test
	public void testDFSThot() {
		graph = createGraphVisits();
		
		VisitForest v = graph.getDFSTOTForest(0);
		assertEquals(VisitType.DFS_TOT, v.visitType);
		
		for(int vert = 0; vert < graph.size(); vert++)
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
		assertEquals(9, v.getEndTime(4));
		
		assertEquals(null, v.getPartent(0));
		assertEquals(new Integer(0), v.getPartent(1));
		assertEquals(new Integer(1), v.getPartent(2));
		assertEquals(new Integer(2), v.getPartent(3));
		assertEquals(null, v.getPartent(4));
		
		assertEquals(new Double(0), v.getDistance(0));
		assertEquals(new Double(1), v.getDistance(1));
		assertEquals(new Double(2), v.getDistance(2));
		assertEquals(new Double(3), v.getDistance(3));
		assertEquals(new Double(0), v.getDistance(4));
		
		v = graph.getDFSTOTForest(new int[]{4, 3, 0});
		assertEquals(VisitType.DFS_TOT, v.visitType);
		
		for(int vert = 0; vert < graph.size(); vert++)
			assertEquals(Color.BLACK, v.getColor(vert));
		
		assertEquals(0, v.getStartTime(4));
		assertEquals(1, v.getEndTime(4));
		assertEquals(2, v.getStartTime(3));
		assertEquals(3, v.getEndTime(3));
		assertEquals(4, v.getStartTime(0));
		assertEquals(5, v.getStartTime(1));
		assertEquals(6, v.getStartTime(2));
		assertEquals(7, v.getEndTime(2));
		assertEquals(8, v.getEndTime(1));
		assertEquals(9, v.getEndTime(0));
		
		assertEquals(null, v.getPartent(0));
		assertEquals(new Integer(0), v.getPartent(1));
		assertEquals(new Integer(1), v.getPartent(2));
		assertEquals(null, v.getPartent(3));
		assertEquals(null, v.getPartent(4));
		
		assertEquals(new Double(0), v.getDistance(0));
		assertEquals(new Double(1), v.getDistance(1));
		assertEquals(new Double(2), v.getDistance(2));
		assertEquals(new Double(0), v.getDistance(3));
		assertEquals(new Double(0), v.getDistance(4));
	}
	
	@Ignore
	private Graph createGraphVisits() {
		Graph graph = new AdjListDir(5);
		graph.addEdge(0, 1);
		graph.addEdge(0, 2);
		graph.addEdge(2, 3);
		graph.addEdge(2, 0);
		graph.addEdge(3, 3);
		graph.addEdge(1, 2);
		
		return graph;
	}
	
	@Ignore
	private void printGraph() {
		for(int i = 0; i<graph.size();i++) {
			System.out.println(i+" -> "+graph.getAdjacent(i));
		}
	}
}
