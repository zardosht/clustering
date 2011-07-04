package org.clustering.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.clustering.model.Cluster;
import org.clustering.model.Item;

public class FileUtil {

	private Set<String> allKeywords;
	private Set<String> uniqueKeywords;
	private List<Item> items;

	public void readInput(String file) throws FileNotFoundException,
			IOException {
		allKeywords = new HashSet<String>();
		uniqueKeywords = new HashSet<String>();
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
		
		for(String keyword : allKeywords) {
			int count = 0;
			for(Item item : items) {
				if(item.getKeywords().contains(keyword)) {
					count++;
					if(count > 1) break;
				}
			}
			if(count == 1) {
				getUniqueKeywords().add(keyword);
			}
		}
	}

	public Set<String> getAllKeywords() {
		return allKeywords;
	}

	public List<Item> getItems() {
		return items;
	}

	public Set<String> getUniqueKeywords() {
		return uniqueKeywords;
	}
	
	public void wirteClusteringResult(File outputFile, List<Cluster> clusters){
		StringBuilder sb = new StringBuilder("");
		for(Cluster cluster : clusters){
			for(Item item : cluster.getMembers()){
				sb.append(item.getItemNumber());
				sb.append(";");
			}
			sb.append(String.format("\n"));
		}
	}
	
	public List<Cluster> readClusteringResults(File inputFile){
		return null;
	}
}
