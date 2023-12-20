// O. Bittel;
// 19.03.2018

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Set;

/**
 * Implementierung von DirectedGraph mit einer doppelten TreeMap 
 * für die Nachfolgerknoten und einer einer doppelten TreeMap 
 * für die Vorgängerknoten. 
 * <p>
 * Beachte: V muss vom Typ Comparable&lt;V&gt; sein.
 * <p>
 * Entspicht einer Adjazenzlisten-Implementierung 
 * mit schnellem Zugriff auf die Knoten.
 * @author Oliver Bittel
 * @since 19.03.2018
 * @param <V> Knotentyp.
 */
public class AdjacencyListDirectedGraph<V> implements DirectedGraph<V> {
    // doppelte Map für die Nachfolgerknoten:
    private final Map<V, Map<V, Double>> succ = new TreeMap<>();

    // doppelte Map für die Vorgängerknoten:
    private final Map<V, Map<V, Double>> pred = new TreeMap<>();

    private int numberEdge = 0;

	@Override
	public boolean addVertex(V v) {
		if (succ.containsKey(v)) {	
			return false;
		} else {
			succ.put(v, new TreeMap<>());
			pred.put(v, new TreeMap<>());
			return true;
		}
	}

    @Override
	public boolean addEdge(V v, V w, double weight) {
		succ.putIfAbsent(v, new TreeMap<>());
		succ.putIfAbsent(w, new TreeMap<>());
		pred.putIfAbsent(v, new TreeMap<>());
		pred.putIfAbsent(w, new TreeMap<>());

		double oldWeight = succ.get(v).getOrDefault(w, Double.MAX_VALUE);

		if (weight < oldWeight) {
			succ.get(v).put(w, weight);
			pred.get(w).put(v, weight);
			if (oldWeight == Double.MAX_VALUE) {
				numberEdge++;
			}
			return true;
		}
   		return false;
	}

    @Override
    public boolean addEdge(V v, V w) {
		succ.putIfAbsent(v, new TreeMap<>());
		succ.putIfAbsent(w, new TreeMap<>());
		pred.putIfAbsent(v, new TreeMap<>());
		pred.putIfAbsent(w, new TreeMap<>());

		if (succ.get(v).containsKey(w)) {
			return false;
		}

		succ.get(v).put(w, 1.0);
		pred.get(w).put(v, 1.0);

		numberEdge++;

		return true;
	}

    @Override
    public boolean containsVertex(V v) {
		return succ.containsKey(v);
    }

    @Override
    public boolean containsEdge(V v, V w) {
		if (succ.get(v).containsKey(w))
			if (pred.get(w).containsKey(v))
				return true;
			else
				return false;
		else
			return false;
	}

    @Override
    public double getWeight(V v, V w) {
		Map<V, Double> innerMap = succ.get(v);
		if (innerMap != null && innerMap.containsKey(w)) {
			return innerMap.get(w);
		} else {
			throw new IllegalArgumentException("Keine Kante");
		}
	}


    @Override
    public int getInDegree(V v) {
		if (pred.containsKey(v))
			return pred.get(v).size();
		else
			throw new IllegalArgumentException("No such Vertex Bro");
    }

    @Override
    public int getOutDegree(V v) {
		if (succ.containsKey(v))
			return succ.get(v).size();
		else
			throw new IllegalArgumentException("No such Vertex Broo");
	}
	
	@Override
    public Set<V> getVertexSet() {
		return Collections.unmodifiableSet(succ.keySet()); // nicht modifizierbare Sicht
    }

    @Override
    public Set<V> getPredecessorVertexSet(V v) {
		Set<V> predecessorVertexSet = new TreeSet<>();

		for (V f : pred.get(v).keySet()) {
			predecessorVertexSet.add(f);
		}

		return predecessorVertexSet;
	}

    @Override
    public Set<V> getSuccessorVertexSet(V v) {
		Set<V> successorVertexSet = new TreeSet<>();

		for (V f : succ.get(v).keySet()) {
			successorVertexSet.add(f);
		}

		return successorVertexSet;
	}

    @Override
    public int getNumberOfVertexes() {
		return succ.size();
    }

    @Override
    public int getNumberOfEdges() {
		return numberEdge;
    }

	@Override
    public DirectedGraph<V> invert() {
		DirectedGraph<V> invertedGraph = new AdjacencyListDirectedGraph<>();

		for (V v : succ.keySet()) {
			for (V w : succ.get(v).keySet()) {
				double weight = getWeight(v, w);
				invertedGraph.addEdge(w, v, weight);
			}
		}
	
		return invertedGraph;
	}

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
	
		for (Map.Entry<V, Map<V, Double>> entry : succ.entrySet()) {
			V v = entry.getKey();
			Map<V, Double> edges = entry.getValue();
	
			for (Map.Entry<V, Double> edge : edges.entrySet()) {
				V w = edge.getKey();
				Double weight = edge.getValue();
	
				builder.append(v.toString())
					   .append(" --> ")
					   .append(w.toString())
					   .append(" weight = ")
					   .append(weight.toString())
					   .append("\n");
			}
		}
	
		return builder.toString();
	}
	
	
	public static void main(String[] args) {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1,2);
		g.addEdge(2,5);
		g.addEdge(5,1);
		g.addEdge(2,6);
		g.addEdge(3,7);
		g.addEdge(4,3);
		g.addEdge(4,6);
		g.addEdge(7,4);
		
		
		System.out.println(g.getNumberOfVertexes());	// 7
		System.out.println(g.getNumberOfEdges());		// 8
		System.out.println(g.getVertexSet());	// 1, 2, ..., 7
		System.out.println(g);
			// 1 --> 2 weight = 1.0 
			// 2 --> 5 weight = 1.0
			// 2 --> 6 weight = 1.0
			// 3 --> 7 weight = 1.0
			// ...
		
		System.out.println("");
		System.out.println(g.getOutDegree(2));			// 2
		System.out.println(g.getSuccessorVertexSet(2));	// 5, 6
		System.out.println(g.getInDegree(6));				// 2
		System.out.println(g.getPredecessorVertexSet(6));	// 2, 4
		
		System.out.println("");
		System.out.println(g.containsEdge(1,2));	// true
		System.out.println(g.containsEdge(2,1));	// false
		System.out.println(g.getWeight(1,2));	// 1.0
		g.addEdge(1, 2, 5.0);
		System.out.println(g.getWeight(1,2));	// 5.0
		
		System.out.println("");
		System.out.println(g.invert());
			// 1 --> 5 weight = 1.0
			// 2 --> 1 weight = 5.0
			// 3 --> 4 weight = 1.0
			// 4 --> 7 weight = 1.0
			// ...
			
		Set<Integer> s = g.getSuccessorVertexSet(2);
		System.out.println(s);
		s.remove(5);	// Laufzeitfehler! Warum?
	}
}
