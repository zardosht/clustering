package org.clustering.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.clustering.model.DistanceTypes;
import org.clustering.model.Item;
import org.clustering.util.DistanceUtil;
import org.clustering.util.VisualisationUtil;

public class DataUtil {

	private FileUtil fileUtil;
	private List<Item> items;
	private Set<String> allKeywords;
	private Set<String> uniqueKeywords;
	private Set<String> nonUniqueKeywords;

	public void readData() throws FileNotFoundException, IOException {
		System.out.println("Start reading data: " + new Date());
		fileUtil = new FileUtil();
		fileUtil.readInput("data/keywords.txt");
		items = fileUtil.getItems();
		filter(items);
		allKeywords = fileUtil.getAllKeywords();
		uniqueKeywords = fileUtil.getUniqueKeywords();
		nonUniqueKeywords = fileUtil.getNonUniqueKeywords();
		System.out.println(String.format("%d out of %d keywords are unique.",
				uniqueKeywords.size(), allKeywords.size()));

		System.out.println("Starting Visualisation: " + new Date());
		new VisualisationUtil(nonUniqueKeywords,
				"results/items_before_clustering.png").drawItems(items);
		System.out.println("End Visualisation: " + new Date());

		System.out.println("Starting calcDistance: " + new Date());
		calcDistances(items, nonUniqueKeywords);
		System.out.println("End calcDistance: " + new Date());
		System.out.println("End reading data: " + new Date());
	}

	private void filter(List<Item> items) {
		Iterator<Item> iterator = items.iterator();
		while (iterator.hasNext()) {
			Item item = iterator.next();
			if (item.getKeywords().size() == 0) {
				iterator.remove();
			}
		}
	}

	/**
	 * @param items
	 * @param nonUniqueKeywords
	 */
	private void calcDistances(List<Item> items, Set<String> nonUniqueKeywords) {
		for (int i = 0; i < items.size(); i++) {
			for (int j = i + 1; j < items.size(); j++) {
				Item item1 = items.get(i);
				item1.setDistance(item1, 0.0);
				Item item2 = items.get(j);
				item2.setDistance(item2, 0.0);
				double distance = DistanceUtil.calcDistance(item1, item2,
						nonUniqueKeywords, DistanceTypes.OTSUKA_SIMILARITY);
				item1.setDistance(item2, distance);
				item2.setDistance(item1, distance);
			}
		}
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public List<Item> getItems() {
		return items;
	}

	public Set<String> getAllKeywords() {
		return allKeywords;
	}

	public Set<String> getUniqueKeywords() {
		return uniqueKeywords;
	}

	public Set<String> getNonUniqueKeywords() {
		return nonUniqueKeywords;
	}
}
