/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Oliver Bittel
 * @since 26.09.2022
 * @param <V> Knotentyp.
 */
public interface Heuristic<V> {
	/**
	 * Schätzt die Kosten (Distanz) von u nach v ab.
	 * @param u
	 * @param v
	 * @return Geschätzte Kosten.
	 */
	double estimatedCost(V u, V v);
}
