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

	public void go(String book, String dictionary, String ignoreWords, String outPutFile) throws Exception {
		// All Books words
		try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
			Files.lines(Paths.get(book)).forEach(text -> pool
					.execute(() -> processEbook(text.replaceAll("[^A-Za-z]+", " ").replace(" ", ",").toLowerCase())));
		}

		// out.println("Index:" + index);
		// out.println("Index size:" + index.size());

		// All Dictionary Words
		try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
			Files.lines(Paths.get(dictionary))
					.forEach(text -> pool.execute(() -> processDictionary(text.replace(",", " ").toLowerCase())));
		}

		// System.out.println("Dictionary:" + dictionary);
		// System.out.println("Dictionary size:" + dictionary.size());
		// System.out.println("Dictionary size:" + dictionary.containsKey("exhibit"));


		// All ignore words
		try (var pool2 = Executors.newVirtualThreadPerTaskExecutor()) {
			Files.lines(Paths.get(ignoreWords))
					.forEach(text -> pool2.execute(() -> processGoogleFiles(text.toLowerCase())));
		}

		// out.println("Ignore:" + ignore);
		// out.println("Ingore size:" + ignore.size());

		// Removing all ignore words from index
		index.removeAll(ignore);
		//System.out.println("Index filtered:" + index);
		
		
		displayAll();
		writeToFile(outPutFile);
		
		//Purging map and Set's
		this.dictionary.clear();
		this.ignore.clear();
		this.ignore.clear();
	}

	// Process's Ebook all words and adds to index list
	public void processEbook(String text) {
		Arrays.stream(text.split(",")).forEach(w -> index.add(w));
	}

	// Process Dictionary into a map
	public void processDictionary(String text) {
		WordDetail wd = new WordDetail(text, pages);
		wd.setDef(text);
		wd.setPages(pages);
		Arrays.stream(text.split(" ")).forEach(w -> dictionary.put(w, wd));
	}

	// Process's Google top 1000 words and adds to ignore list
	public void processGoogleFiles(String text) {
		Arrays.stream(text.split(",")).forEach(w -> ignore.add(w));
	}
	
	//Displays all ebook words and definistion along with it
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

	//Wrting To file
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