// O. Bittel;
// 26.09.2022

// import directedGraph.*;
import sim.SYSimulation;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


// ...

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 * @author Oliver Bittel
 * @since 27.01.2015
 * @param <V> Knotentyp.
 */
public class ShortestPath<V> {
	
	SYSimulation sim = null;
	
	Map<V,Double> dist; 		// Distanz für jeden Knoten
	Map<V,V> pred; 				// Vorgänger für jeden Knoten
	IndexMinPQ<V,Double> cand; 	// Kandidaten als PriorityQueue PQ
	DirectedGraph<V> graph; 	// Graph
	Heuristic<V> h; 			// Heuristik
	V start;
	V goal;

	/**
	 * Konstruiert ein Objekt, das im Graph g k&uuml;rzeste Wege 
	 * nach dem A*-Verfahren berechnen kann.
	 * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
	 * Wird h = null gewählt, dann ist das Verfahren identisch 
	 * mit dem Dijkstra-Verfahren.
	 * @param g Gerichteter Graph
	 * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
	 * dem Dijkstra-Verfahren gesucht.
	 */
	public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) {
		dist = new HashMap<>();
		pred = new HashMap<>();
		cand = new IndexMinPQ<>();

        this.graph = g;
        this.h = h;

	}

	/**
	 * Diese Methode sollte nur verwendet werden, 
	 * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
	 * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
	 * <p>
	 * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
	 * <p><blockquote><pre>
	 *    if (sim != null)
	 *       sim.visitStation((Integer) v, Color.blue);
	 * </pre></blockquote>
	 * @param sim SYSimulation-Objekt.
	 */
	public void setSimulator(SYSimulation sim) {
		this.sim = sim;
	}

	/**
	 * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
	 * <p>
	 * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
	 * der als nächstes aus der Kandidatenliste besucht wird, animiert.
	 * @param s Startknoten
	 * @param g Zielknoten
	 */
	public void searchShortestPath(V s, V g) {

		this.start = s;
		this.goal = g;

		if (h == null) {

			for (V v : graph.getVertexSet()) {
				dist.put(v, Double.POSITIVE_INFINITY);
				pred.put(v, null);
			}
			
			dist.put(s, 0.0);
			cand.add(s, 0.0);

			while (!cand.isEmpty()) {
				V v = cand.removeMin();
				System.out.println("Besuche Knoten " + v + " mit d = " + dist.get(v)); 
				
				for (V w : graph.getSuccessorVertexSet(v)) {
					if (dist.get(w) == Double.POSITIVE_INFINITY) {
						dist.put(w, dist.get(v) + graph.getWeight(v, w));
						pred.put(w, v);
						cand.add(w, dist.get(w));
					} else if (dist.get(w) > dist.get(v) + graph.getWeight(v, w)) {
						dist.put(w, dist.get(v) + graph.getWeight(v, w));
						pred.put(w, v);
						cand.change(w, dist.get(w));
					}
				}
			}
		} else {
			for (V v : graph.getVertexSet()) {
				dist.put(v, Double.POSITIVE_INFINITY);
				pred.put(v, null);
			}
			
			dist.put(s, 0.0);
			cand.add(s, 0.0 + h.estimatedCost(s, g));

			while (!cand.isEmpty()) {
				V v = cand.removeMin();
				System.out.println("Besuche Knoten " + v + " mit d = " + dist.get(v)); 

				if (v == g) 
					break;

				for (V w : graph.getSuccessorVertexSet(v)) {
					if (dist.get(w) == Double.POSITIVE_INFINITY) {
						dist.put(w, dist.get(v) + graph.getWeight(v, w));
						pred.put(w, v);
						cand.add(w, dist.get(w) + h.estimatedCost(w, g));
					} else if (dist.get(w) > dist.get(v) + graph.getWeight(v, w)) {
						dist.put(w, dist.get(v) + graph.getWeight(v, w));
						pred.put(w, v);
						cand.change(w, dist.get(w) + h.estimatedCost(w, g));
					}
				}
			}
		}	
	}

	/**
	 * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return kürzester Weg als Liste von Knoten.
	 */
	public List<V> getShortestPath() {
		if (start == null || goal == null) {
			throw new IllegalArgumentException("Kein kürzester Weg berechnet");
		}

		List<V> path = new LinkedList<>();

		for (V v = goal; v != null; v = pred.get(v)) {
			path.add(0, v);
		}

		if (!path.get(0).equals(start)) {
			throw new IllegalArgumentException("Kein kürzester Weg berechnet");
		}

		return path;
	}

	/**
	 * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g zurück.
	 * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
	 * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
	 * @return Länge eines kürzesten Weges.
	 */
	public double getDistance() {
		double distance = 0.0;

		V v = goal;
		while (v != null) {
			V w = pred.get(v);
			if( w != null) {
				distance += graph.getWeight(w, v);
			}
			v = w;
		}

		return distance;
	}

}