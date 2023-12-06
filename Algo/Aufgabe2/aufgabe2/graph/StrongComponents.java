// O. Bittel;
// 22.02.2017

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.List;
import java.util.ListIterator;


/**
 * Klasse für Bestimmung aller strengen Komponenten.
 * Kosaraju-Sharir Algorithmus.
 * @author Oliver Bittel
 * @since 22.02.2017
 * @param <V> Knotentyp.
 */
public class StrongComponents<V> {
	// comp speichert jede Komponente die zughörigen Knoten. 
	private final Map<Integer,Set<V>> comp = new TreeMap<>();
	private final List<V> postOrder = new LinkedList<>();
	private final List<V> invPostOrder = new LinkedList<>();
	private DirectedGraph<V> invertedGraph = new AdjacencyListDirectedGraph<>();
	private Set<V> besucht = new TreeSet<>();
	
	// Anzahl der Komponenten:
	private int numberOfComp = 0;
	
	/**
	 * Ermittelt alle strengen Komponenten mit
	 * dem Kosaraju-Sharir Algorithmus.
	 * @param g gerichteter Graph.
	 */
	public StrongComponents(DirectedGraph<V> g) {
		
		for (V v : g.getVertexSet()) {
            if (!besucht.contains(v)) {
                visitDF(v, g, besucht);           
            }
        }

		invertLinkedList();
		invertedGraph = g.invert();
		besucht.clear();

		for (V v : invPostOrder) {
            if (!besucht.contains(v)) {
				Set<V> component = new TreeSet<>();

                visitDF(v, invertedGraph, besucht, component);
				comp.put(comp.size(), component);
				numberOfComp++;                  
            }
		}
	}

    private void visitDF(V v, DirectedGraph<V> g, Set<V> besucht) {
        besucht.add(v);

        for (V w : g.getSuccessorVertexSet(v))
            if(!besucht.contains(w)) 
                visitDF(w, g, besucht);
		postOrder.add(v);
    }

	private void visitDF(V v, DirectedGraph<V> g, Set<V> besucht, Set<V> component) {
		besucht.add(v);
		component.add(v);

		for (V w : g.getSuccessorVertexSet(v))
			if(!besucht.contains(w)) 
				visitDF(w, g, besucht, component);
	}

	public void invertLinkedList() {
		ListIterator<V> iterator = postOrder.listIterator(postOrder.size());

		while (iterator.hasPrevious()) {
			invPostOrder.add(iterator.previous());
		}

		// System.out.println(postOrder + "post");
		// System.out.println(invPostOrder + "Ipost");
	}
	
	/**
	 * 
	 * @return Anzahl der strengen Komponeneten.
	 */
	public int numberOfComp() {
		return numberOfComp;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Map.Entry<Integer, Set<V>> entry : comp.entrySet()) {
			sb.append("Component ").append(entry.getKey()).append(": ");
			sb.append(entry.getValue().toString()).append("\n");
		}
		return sb.toString();
	}
	
		
	public static void main(String[] args) {
		DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
		g.addEdge(1,2);
		g.addEdge(1,3);
		g.addEdge(2,1);
		g.addEdge(2,3);
		g.addEdge(3,1);
		
		g.addEdge(1,4);
		g.addEdge(5,4);
		
		g.addEdge(5,7);
		g.addEdge(6,5);
		g.addEdge(7,6);
		
		g.addEdge(7,8);
		g.addEdge(8,2);
		
		StrongComponents<Integer> sc = new StrongComponents<>(g);
		
		System.out.println(sc.numberOfComp());  // 4
		
		System.out.println(sc);
			// Component 0: 5, 6, 7, 
        	// Component 1: 8, 
            // Component 2: 1, 2, 3, 
            // Component 3: 4, 
	}
}
