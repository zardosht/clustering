package org.clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.clustering.model.Item;

public class Main {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		BufferedReader reader = new BufferedReader(new FileReader(
				"data/keywords.txt"));

		List<Item> items = new ArrayList<Item>(1700);
		Set<String> allKeywords = new HashSet<String>();
		
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
		
		int numKeywords = allKeywords.size();
		for(int i = 0; i < items.size(); i++) {
			for(int j = i+1;j < items.size(); j++) {
				items.get(i).calcDistance(items.get(j),numKeywords);
			}
		}

		System.out.println(items.get(0).toString());
		
		System.out
				.println("It looks like the EGit developers are not responsible developers "
						+ "and still need to learn alot!");
	}

}
