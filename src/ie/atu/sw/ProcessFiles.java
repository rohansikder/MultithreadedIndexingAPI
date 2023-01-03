package ie.atu.sw;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;

/**
 *	This class Processes all files which the user has inputed from the runner class.
 *	This class has a Integer List, Two ConcurrentSkiplistSets, 
 *	One called index which holds all the eBook words
 *	One called ignore which holds all the words the program is going to ignore
 *
 * @author Rohan Sikder
 *	
 */

public class ProcessFiles {
	List<Integer> pages = new ArrayList<>();
	int numOfPages = 0;

	// Ebook List
	public Set<String> index = new ConcurrentSkipListSet<>();
	// Ignore words List
	public Set<String> ignore = new ConcurrentSkipListSet<>();
	// Dictionary Map
	public Map<String, WordDetail> dictionary;

	// Constructor
	public ProcessFiles() {
		super();
		dictionary = new ConcurrentHashMap<>();
	}
	
	/**
	 * Go Takes all params and extracts informatioN
	 * 
	 * @param book
	 * @param dictionary
	 * @param ignoreWords
	 * @param outPutFile
	 * @throws Exception
	 */
	public void go(String book, String dictionary, String ignoreWords, String outPutFile) throws Exception {
		// All Books words
		try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
			Files.lines(Paths.get(book)).forEach(text -> pool
					.execute(() -> processEbook(text.replaceAll("[^A-Za-z]+", " ").replace(" ", ",").toLowerCase())));
		}

		
		// All Dictionary Words
		try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
			Files.lines(Paths.get(dictionary))
					.forEach(text -> pool.execute(() -> processDictionary(text.replace(",", " ").toLowerCase())));
		}

		// All ignore words
		try (var pool2 = Executors.newVirtualThreadPerTaskExecutor()) {
			Files.lines(Paths.get(ignoreWords))
					.forEach(text -> pool2.execute(() -> processGoogleFiles(text.toLowerCase())));
		}

		// Removing all ignore words from index - O(m * log(n))
		index.removeAll(ignore);
		
		displayAll();
		writeToFile(outPutFile);
		
		//Purging map and Set's
		this.dictionary.clear();
		this.ignore.clear();
		this.ignore.clear();
	}

	
	/**
	 * processEbook takes line of words and splits them the puts them into index List
	 * 
	 * @param text is the string of words
	 */
	// Process's Ebook all words and adds to index list. Big O Notation: O(n)
	public void processEbook(String text) {
		Arrays.stream(text.split(",")).forEach(w -> index.add(w));
	}
	
	/**
	 * processDictionary takes in the dictionary and puts all definitions and pages list into the dictionary map. 
	 * 
	 * 
	 * @param text is the line of text which includes the word and definition
	 */
	// Process Dictionary into a map. Big O Notation: O(n)
	public void processDictionary(String text) {
		WordDetail wd = new WordDetail(text, pages);
		wd.setDef(text);
		wd.setPages(pages);
		Arrays.stream(text.split(" ")).forEach(w -> dictionary.put(w, wd));
	}

	
	/**
	 * processGoogleFiles takes line of words and splits them the puts them into ignore List
	 * 
	 * @param text is the string of words
	 */
	// Process's Google top 1000 words and adds to ignore list. Big O Notation: O(n)
	public void processGoogleFiles(String text) {
		Arrays.stream(text.split(",")).forEach(w -> ignore.add(w));
	}
	
	/**
	 * displayAll displays all words from book along with its definition to the user
	 * 
	 */
	//Displays all ebook words and definition along with it. Big O Notation: O(n)
	public void displayAll() {
		Iterator<String> iterator = index.iterator();
		String word;
		
				System.out.println("______________________________________________________________");
				System.out.println("| 		Word    |    Details   			                     |");
		while (iterator.hasNext()) {
			WordDetail wdFind = dictionary.get(word = iterator.next());
		    
			if (wdFind != null) {
				StringBuilder sb = new StringBuilder(wdFind.getDef());
				int i = 0;
			    while ((i = sb.indexOf(" ", i + 45)) != -1) {
			        sb.replace(i, i + 1, "\n|\t");
			    }
			    System.out.println("______________________________________________________________");
				System.out.println("|"+ word +" |" + " Definition:                                ");
				System.out.println("|\t" + sb.toString());
				System.out.println("|_____________________________________________________________");
			}
		}
		
	}
	
	/**
	 * writeToFile Writes all words from book along with its definition to a text file
	 * 
	 */
	//Wrting To file - Big O Notation: O(n)
	public void writeToFile(String fileName) throws Exception {
		FileWriter fw = new FileWriter(fileName);
		PrintWriter out = new PrintWriter(fw);
		
		Iterator<String> iterator = index.iterator();
		String word;
		
				out.println("______________________________________________________________");
				out.println("| 		Word    |    Details   			                      |");
		while (iterator.hasNext()) {
			WordDetail wdFind = dictionary.get(word = iterator.next());
		    
			if (wdFind != null) {
				StringBuilder sb = new StringBuilder(wdFind.getDef());
				int i = 0;
			    while ((i = sb.indexOf(" ", i + 45)) != -1) {
			        sb.replace(i, i + 1, "\n|\t");
			    }
			    out.println("______________________________________________________________");
				out.println("|"+ word +" |" + " Definition:                                ");
				out.println("|\t" + sb.toString());
				out.println("|_____________________________________________________________");
			}
		}

		// Close the file.
		out.close();
	}
}