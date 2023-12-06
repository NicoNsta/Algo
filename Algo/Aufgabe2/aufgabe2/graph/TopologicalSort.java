// O. Bittel;
// 22.02.2017

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Klasse zur Erstellung einer topologischen Sortierung.
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class TopologicalSort<V> {
    private List<V> ts = new LinkedList<>(); // topologisch sortierte Folge
	Map<V, Integer> inDegree;
	Queue<V> q;
	V v;

	/**
	 * Führt eine topologische Sortierung für g durch.
	 * @param g gerichteter Graph.
	 */
	public TopologicalSort(DirectedGraph<V> g) {
		inDegree = new HashMap<>();
		q = new LinkedList<>();

        for (V v : g.getVertexSet()) {
			inDegree.put(v, g.getPredecessorVertexSet(v).size());
			if (inDegree.get(v) == 0) 
				q.add(v);
		}

		while(!q.isEmpty()) {
			v = q.remove();
			ts.add(v);
			for (V w : g.getSuccessorVertexSet(v)){
				int updatedValue = inDegree.get(w) - 1;
            	inDegree.put(w, updatedValue);
            	if(updatedValue == 0)
                	q.add(w);
			}
		}
    }
    
	/**
	 * Liefert eine nicht modifizierbare Liste (unmodifiable view) zurück,
	 * die topologisch sortiert ist.
	 * @return topologisch sortierte Liste
	 */
	public List<V> topologicalSortedList() {
        return Collections.unmodifiableList(ts);
    }
    

	public static void main(String[] args) {
		// DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		// g.addEdge(1, 2);
		// g.addEdge(2, 3);
		// g.addEdge(3, 4);
		// g.addEdge(3, 5);
		// g.addEdge(4, 6);
		// g.addEdge(4, 7);
		// g.addEdge(5, 6);
		// g.addEdge(6, 7);

		// TopologicalSort<Integer> ts = new TopologicalSort<>(g);


		DirectedGraph<String> g = new AdjacencyListDirectedGraph<>();
		g.addEdge("Mütze", "Handschuh");
		g.addEdge("Schal", "Handschuh");
		g.addEdge("Schuhe", "Handschuh");
		g.addEdge("Mantel", "Schal");
		g.addEdge("Gürtel", "Mantel");
		g.addEdge("Hose", "Gürtel");
		g.addEdge("Unterhose", "Hose");
		g.addEdge("Socken", "Schuhe");
		g.addEdge("Pulli", "Mantel");
		g.addEdge("Hemd", "Pulli");
		g.addEdge("Unterhemd", "Hemd");
		g.addEdge("Hose", "Schuhe");
		g.addEdge("Schal", "Hose");

		TopologicalSort<String> ts = new TopologicalSort<>(g);




		System.out.println(g);
		
		if (ts.topologicalSortedList() != null) {
			System.out.println(ts.topologicalSortedList()); // [1, 2, 3, 4, 5, 6, 7]
		}
	}
}
