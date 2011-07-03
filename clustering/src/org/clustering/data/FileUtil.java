package org.clustering.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.clustering.model.Item;

public class FileUtil {
	
	private Set<String> allKeywords;
	private List<Item> items;

	
	
	public void readInput(String file) throws FileNotFoundException,
			IOException {
		allKeywords = new HashSet<String>();
		items = new ArrayList<Item>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			int indexOf = line.indexOf(":");
			if (indexOf == -1)
				continue;
			Item item = new Item(Integer.parseInt(line.substring(0, indexOf)));
			if (indexOf + 1 < line.length()) {
				String[] keywords = (line.substring(indexOf + 1)).split(";");
				for (String keyword : keywords) {
					keyword = keyword.trim();
					item.addKeyword(keyword);
					allKeywords.add(keyword);
				}
			}
			items.add(item);
		}
		reader.close();
	}



	public Set<String> getAllKeywords() {
		return allKeywords;
	}



	public List<Item> getItems() {
		return items;
	}
}
