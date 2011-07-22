package org.clustering.mode.modes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.clustering.data.FileUtil;
import org.clustering.mode.AbstractMode;
import org.clustering.model.Item;

public class KeywordCountMode extends AbstractMode {

	public KeywordCountMode() {
		super("-keywordCount", "");
	}

	public void _runCount() throws Exception, IOException {
		FileUtil fileUtil = new FileUtil();
		fileUtil.readInput("data/keywords.txt");
		List<Item> items = fileUtil.getItems();
		HashMap<Integer, Integer> results = new HashMap<Integer, Integer>();
		for(Item item : items) {
			int size = item.getKeywords().size();
			Integer integer = results.get(size);
			results.put(size, (integer == null)?1:integer+1);
		}
		
		for(int i = 0; i < 5; i++) {
			System.out.println("i "+i+" :"+results.get(i));
		}
		
	}
	
}
