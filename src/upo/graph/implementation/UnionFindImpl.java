/**
 * 
 */
package upo.graph.implementation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import upo.additionalstructures.UnionFind;

/**
 * @author Dawit Gulino
 *
 */
public class UnionFindImpl implements UnionFind<Integer> {
	
	private Map<Integer,Set<Integer>> map = new HashMap<>();
	//TODO forse aggiungere rank/size per ottimizzare
	
	public UnionFindImpl() {
		map = new HashMap<>();
	}
	
	@Override
	public void makeSet(Integer element) {
		if(find(element)!=null)
			throw new IllegalArgumentException();
		
		Set<Integer> set = new HashSet<>();
		set.add(element);
		map.put(element, set);
	}

	@Override
	public void union(Integer el1, Integer el2) {
		if(el1 == el2)
			throw new IllegalArgumentException();
		
		Set<Integer> set1 = map.get(el1);
		Set<Integer> set2 = map.get(el2);
		
		//bysize
		if(set1.size()>set2.size())
			set1.addAll(set2);
		else {
			set2.addAll(set1);
			map.put(el1, set2);
		}
		
		map.remove(el2);
	}

	@Override
	public Integer find(Integer el) {
		AtomicInteger num = new AtomicInteger(el);
		AtomicBoolean found = new AtomicBoolean(true);
		
		if(map.get(el) == null) {
			found.set(false);
			map.forEach((key, set) -> {
				if(set.contains(el)) {
					num.set(key);
					found.set(true);
				}
			});
		}
		
		return found.get()? num.get() : null;
	}
	
}
