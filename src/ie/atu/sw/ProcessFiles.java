package ie.atu.sw;

import static java.lang.System.out;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;

public class ProcessFiles {
	//Ebook List 
	//Map<String, WordDetail> index = new ConcurrentHashMap<>();
	Set<String> index = new ConcurrentSkipListSet<>();
	//Ignore words List
	Set<String> ignore = new ConcurrentSkipListSet<>();


	public void go(String book) throws Exception {
		//All Books words
		try (var pool = Executors.newVirtualThreadPerTaskExecutor()) {
			Files.lines(Paths.get(book)).forEach(text -> pool.execute(() -> processEbook(text.replaceAll("[^A-Za-z]+", " ").replace(" ", ",").toLowerCase())));
		}
		
		//out.println("Index:" + index);
		//out.println("Index size:" + index.size());
		
		//All ignore words
		try (var pool2 = Executors.newVirtualThreadPerTaskExecutor()) {
			Files.lines(Paths.get("./google-1000.txt")).forEach(text -> pool2.execute(() -> processGoogleFiles(text.toLowerCase())));
		}
		
		//out.println("Ignore:" + ignore);
		//out.println("Ingore size:" + ignore.size());
		
		//Removing all ignore words from index
		index.removeAll(ignore);
		out.println("Index filtered:" + index);
	}
	
	//Process's Ebook all words and adds to index list
	public void processEbook(String text) { 
		Arrays.stream(text.split(",")).forEach(w -> index.add(w));
	}
	
	//Process's dictionary all words and adds to ignore list
	public void processGoogleFiles(String text) { 
		Arrays.stream(text.split(",")).forEach(w -> ignore.add(w));
	}
	
	
}
