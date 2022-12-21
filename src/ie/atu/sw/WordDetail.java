package ie.atu.sw;

import java.util.List;

public class WordDetail {
	String definition;
	List<Integer> pages;
	
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

	public boolean isListed() {
		return pages.size() > 0;
	}
	
}

