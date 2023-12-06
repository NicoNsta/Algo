import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.print.DocFlavor.INPUT_STREAM;
import javax.swing.JFileChooser;

/*
 * Test der verscheidenen Dictionary-Implementierungen
 *
 * O. Bittel
 * 1.8.2023
 */

/**
 * Static test methods for different Dictionary implementations.
 * @author oliverbittel
 */
public class DictionaryTest {

	
	/**
	 * @param args not used.
	 */
public static void main(String[] args)  {

	Scanner scanner = new Scanner(System.in);
	
	long startTime, endTime, duration;

	String input = scanner.nextLine(); 
	String[] words = input.split(" ");
	String firstWord = words[0];

	if (firstWord.equals("create")) {
		Dictionary<String, String> dict = new BinaryTreeDictionary<>(); // SortedArrayDictionary<>(); // HashDictionary<>(); // BinaryTreeDictionary<>();
	
		while (true) {

		input = scanner.nextLine(); 
		words = input.split(" ");
		firstWord = words[0];
		String secondWord = words.length > 1 ? words[1] : null;
		String thirdWord = words.length > 2 ? words[2] : null;

		
		if (firstWord.equals("r")) {

			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			int result = fileChooser.showOpenDialog(null);

			if (result == JFileChooser.APPROVE_OPTION) {
				int n;
				
				if(secondWord != null) {
					n = Integer.parseInt(secondWord); 
				} else { 
					n = Integer.MAX_VALUE;
				}

				File selectedFile = fileChooser.getSelectedFile();
				System.out.println("Ausgewählte Datei: " + selectedFile.getAbsolutePath());

				startTime = System.nanoTime();

				try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
					for (int i = 0; i < n; i++) {
						String line = reader.readLine();
						if (line != null) {
							StringTokenizer st = new StringTokenizer(line);
							if (st.hasMoreTokens()) {
								String fW = st.nextToken();
								if (st.hasMoreTokens()) {
									String sW = st.nextToken();
									dict.insert(fW, sW);
								}
							}
						} else {
							break;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				endTime = System.nanoTime();
				duration = endTime - startTime;
				System.out.println("Alle " + n + " Einträge, wurden in " + (duration / 1_000_000) + " Milisekunden (" + duration + " Nanosekunden) eingetragen");
			}
		} else if (firstWord.equals("p")) {
			for (Dictionary.Entry<String, String> e : dict) {
				System.out.println(e.getKey() + ": " + e.getValue() + " search: " + dict.search(e.getKey()));
			}

		} else if (firstWord.equals("s")) {
			System.out.println(dict.search(secondWord));
			
		} else if (firstWord.equals("listde")) {
			File file = new File("worldlist.txt");
			try (PrintWriter writer = new PrintWriter(file)) {
				for (Dictionary.Entry<String, String> e : dict) {
					writer.println(e.getKey());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("DE List created!");
		
		} else if (firstWord.equals("listen")) {
			File file = new File("worldlist.txt");
			try (PrintWriter writer = new PrintWriter(file)) {
				for (Dictionary.Entry<String, String> e : dict) {
					writer.println(e.getValue());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("EN List created!");

		} else if (firstWord.equals("stest")) {
			startTime = System.nanoTime();

			try (BufferedReader reader = new BufferedReader(new FileReader("worldlist.txt"))) {
				String line;
				while ((line = reader.readLine()) != null) {
					System.out.println(dict.search(line));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			endTime = System.nanoTime();
			duration = endTime - startTime;
			System.out.println("Alle Einträge, wurden in " + (duration / 1_000_000) + " Milisekunden (" + duration + " Nanosekunden) gesucht");


		} else if (firstWord.equals("i")) {
			dict.insert(secondWord, thirdWord);
			System.out.println("Inserted!");
		
		} else if (firstWord.equals("d")) {
			dict.remove(secondWord);
			System.out.println("Removed!");

		} else if (firstWord.equals("exit")) {
			break;
		} else {
			System.out.println("Falsche Eingabe!");
		}
		}
	

			// testSortedArrayDictionary();

			// testHashDictionary();

	testBinaryTreeDictionary();
	}
}

	// private static void testSortedArrayDictionary() {
	// 	Dictionary<String, String> dict = new SortedArrayDictionary<>();
	// 	testDict(dict);
	// }
	
	// private static void testHashDictionary() {
	// 	Dictionary<String, String> dict = new HashDictionary<>();
 	// 	testDict(dict);
	// }
	
	private static void testBinaryTreeDictionary() {
		Dictionary<String, String> dict = new BinaryTreeDictionary<>();
		testDict(dict);
        
        // Test für BinaryTreeDictionary mit prettyPrint 
        // (siehe Aufgabe 10; Programmiertechnik 2).
        // Pruefen Sie die Ausgabe von prettyPrint auf Papier nach.

        // BinaryTreeDictionary<Integer, Integer> btd = new BinaryTreeDictionary<>();
        // btd.insert(10, 0);
        // btd.insert(20, 0);
        // btd.insert(50, 0);
        // System.out.println("insert:");
        // btd.prettyPrint();

        // btd.insert(40, 0);
        // btd.insert(30, 0);
        // System.out.println("insert:");
        // btd.prettyPrint();

        // btd.insert(21, 0);
        // System.out.println("insert:");
        // btd.prettyPrint();

        // btd.insert(35, 0);
        // btd.insert(45, 0);
        // System.out.println("insert:");
        // btd.prettyPrint();

        // System.out.println("For Each Loop:");
        // for (Dictionary.Entry<Integer, Integer> e : btd) {
        //     System.out.println(e.getKey() + ": " + e.getValue());
        // }

        // btd.remove(30);
        // System.out.println("remove:");
        // btd.prettyPrint();

        // btd.remove(35);
        // btd.remove(40);
        // btd.remove(45);
        // System.out.println("remove:");
        // btd.prettyPrint();
		
		// btd.remove(50);
        // System.out.println("remove:");
        // btd.prettyPrint();

		// System.out.println("For Each Loop:");
		// for (Dictionary.Entry<Integer, Integer> e : btd) {
		// 	System.out.println(e.getKey() + ": " + e.getValue());
		// }
    }
	
	private static void testDict(Dictionary<String, String> dict) {
		System.out.println("===== New Test Case ========================");
		System.out.println("test " + dict.getClass());
		System.out.println(dict.insert("gehen", "go") == null);		// true
		String s = new String("gehen");
		System.out.println(dict.search(s) != null);					// true
		System.out.println(dict.search(s).equals("go"));			// true
		System.out.println(dict.insert(s, "walk").equals("go"));	// true
		System.out.println(dict.search("gehen").equals("walk"));	// true
		System.out.println(dict.remove("gehen").equals("walk"));	// true
		System.out.println(dict.remove("gehen") == null); // true
		dict.insert("starten", "start");
		dict.insert("gehen", "go");
		dict.insert("schreiben", "write");
		dict.insert("reden", "say");
		dict.insert("arbeiten", "work");
		dict.insert("lesen", "read");
		dict.insert("singen", "sing");
		dict.insert("schwimmen", "swim");
		dict.insert("rennen", "run");
		dict.insert("beten", "pray");
		dict.insert("tanzen", "dance");
		dict.insert("schreien", "cry");
		dict.insert("tauchen", "dive");
		dict.insert("fahren", "drive");
		dict.insert("spielen", "play");
		dict.insert("planen", "plan");
		dict.insert("diskutieren", "discuss");
		// dict.insert("testen", "test");

		System.out.println(dict.size());
		for (Dictionary.Entry<String, String> e : dict) {
			System.out.println(e.getKey() + ": " + e.getValue() + " search: " + dict.search(e.getKey()));
		}
	}
	
}
