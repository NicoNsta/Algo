// O. Bittel;
// 2.8.2023

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Klasse zur Analyse von Web-Sites.
 *
 * @author Oliver Bittel
 * @since 30.10.2023
 */
public class AnalyzeWebSite {
    public static void main(String[] args) throws IOException {
        // Graph aus Website erstellen und ausgeben:
        DirectedGraph<String> webSiteGraph = buildGraphFromWebSite("Aufgabe2\\aufgabe2\\data\\WebSiteKlein");
        // DirectedGraph<String> webSiteGraph = buildGraphFromWebSite("Aufgabe2\\aufgabe2\\data\\WebSiteGross");
        System.out.println("Anzahl Seiten: \t" + webSiteGraph.getNumberOfVertexes());
        System.out.println("Anzahl Links: \t" + webSiteGraph.getNumberOfEdges());
        //System.out.println(webSiteGraph);

        // Starke Zusammenhangskomponenten berechnen und ausgeben
        StrongComponents<String> sc = new StrongComponents<>(webSiteGraph);
        System.out.println(sc.numberOfComp());
        //System.out.println(sc);

        // Page Rank ermitteln und Top-100 ausgeben
        pageRank(webSiteGraph);
    }

    /**
     * Liest aus dem Verzeichnis dirName alle Web-Seiten und
     * baut aus den Links einen gerichteten Graphen.
     *
     * @param dirName Name eines Verzeichnis
     * @return gerichteter Graph mit Namen der Web-Seiten als Knoten und Links als gerichtete Kanten.
     */
    private static DirectedGraph buildGraphFromWebSite(String dirName) throws IOException {
        File webSite = new File(dirName);
        DirectedGraph<String> webSiteGraph = new AdjacencyListDirectedGraph();

        for (File f : webSite.listFiles()) {
            String from = f.getName();
            LineNumberReader in = new LineNumberReader(new FileReader(f));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("href")) {
                    String[] s_arr = line.split("\"");
                    String to = s_arr[1];
                    webSiteGraph.addEdge(from, to);
                }
            }
        }
        return webSiteGraph;
    }

    /**
     * pageRank ermittelt Gewichte (Ranks) von Web-Seiten
     * aufgrund ihrer Link-Struktur und gibt sie aus.
     *
     * @param g gerichteter Graph mit Web-Seiten als Knoten und Links als Kanten.
     */
    private static <V> void pageRank(DirectedGraph<V> g) {
        int nI = 10;
        double alpha = 0.5;
        Map<V, Double> rankTable = new HashMap<>();

        // Definiere und initialisiere rankTable:
        for (V v : g.getVertexSet())
            rankTable.put(v, 1.0);

        // Iteration:
        // Ihr Code: ...
        for (int i = 0; i < nI; i++){
            Map<V, Double> newRankTable = new HashMap<>();

            for (V v : g.getVertexSet()) {
                double newRank = (1 - alpha) + alpha;

                for (V pred : g.getPredecessorVertexSet(v)) {
                    newRank += alpha * rankTable.get(pred) / g.getSuccessorVertexSet(pred).size();
                }
                newRankTable.put(v, newRank);
            }
            rankTable = newRankTable;
        }

        // Rank Table ausgeben (nur für data/WebSiteKlein):
        // Ihr Code: ...
        // System.out.println(rankTable);

        // Nach Ranks sortieren Top 100 ausgeben (nur für data/WebSiteGross):
        // Ihr Code: ...

        Map<V, Double> sortedRankTable = rankTable.entrySet()
            .stream()
            .sorted(Map.Entry.<V, Double>comparingByValue().reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        int i = 0;
        for (Map.Entry<V, Double> entry : sortedRankTable.entrySet()) {
            if (i++ >= 100)
                break;
            // Top-Seite mit ihren Vorgängern und Ranks ausgeben (nur für data/WebSiteGross):
            System.out.println("Rank: " + i + " Key: " + entry.getKey() + ", Value: " + entry.getValue() + ", Vorgänger: " + g.getInDegree(entry.getKey()));
        }
    }
}
