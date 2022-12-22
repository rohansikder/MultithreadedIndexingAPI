package ie.atu.sw;

import java.util.ArrayList;
import java.util.List;

public class WordDetail {
	String word;
	String wordType;
	String definition;
	List<Integer> pages = new ArrayList<>();
	
	//Constructor
	public WordDetail(String definition, List<Integer> pages) {
		super();
		this.definition = definition;
		this.pages = pages;
	}

	//Getters and Setters
	public String getDef() {
		return definition;
	}

	public void setDef(String def) {
		this.definition = def;
	}

	public List<Integer> getPages() {
		return pages;
	}

	public void setPages(List<Integer> pages) {
		this.pages = pages;
	}

}

