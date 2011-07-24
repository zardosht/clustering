package org.clustering.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.clustering.evaluator.KeywordCount;
import org.clustering.model.DistanceTypes;
import org.clustering.model.Item;
import org.clustering.util.DistanceUtil;

public class DataUtil {

	private FileUtil fileUtil;
	private List<Item> items;
	private Set<String> allKeywords;
	private Set<String> uniqueKeywords;
	private Set<String> atLeast2Keywords;

	public void readData() throws Exception {
		readData(true,-1, DistanceTypes.RUSSELL_AND_RAO_SIMILARITY);
	}
	
	public void readData(boolean calcDistancesNew, int nAtLeastKweywordFilter, DistanceTypes distanceType) throws Exception {
		readData(calcDistancesNew,-1, nAtLeastKweywordFilter, distanceType);
	}
	
	public void readData(boolean calcDistancesNew, int itemCount, int nAtLeastKweywordFilter, DistanceTypes distanceType)
			throws FileNotFoundException, IOException {
		System.out.println("Start reading data: " + new Date());
		fileUtil = new FileUtil();
		fileUtil.readInput("data/keywords.txt");
		items = fileUtil.getItems();
		if (itemCount != -1 && itemCount < items.size()) {
			items = items.subList(0, itemCount);
		}
		allKeywords = fileUtil.getAllKeywords();
		uniqueKeywords = fileUtil.getUniqueKeywords();
		atLeast2Keywords = getNonUniqueKeywords();
		Set<String> keywords = getAtLeastNTimesKeywords(nAtLeastKweywordFilter);
		System.out.println(String.format("%d keywords appear at least %d times.", keywords.size(), nAtLeastKweywordFilter));
		System.out.println(String.format("%d out of %d keywords are unique.",
				uniqueKeywords.size(), allKeywords.size()));
		filter(items, keywords);

//		 System.out.println("Starting Visualisation: " + new Date());
//		 new VisualisationUtil(atLeast10TimesKeywords,
//		 "results/items_before_clustering.png").drawItems(items);
//		 System.out.println("End Visualisation: " + new Date());
//		n = 10;
//		List<String> sortedAtLeast10TimesKeywords = getSortedAtLeastNTimesKeywords(n);
//		n = 2;
//		List<String> sortedAtLeast2TimesKeywords = getSortedAtLeastNTimesKeywords(n);
//		System.out.println("Starting Visualisation: " + new Date());
//		 new VisualisationUtil(sortedAtLeast10TimesKeywords,
//		 "results/items_before_clustering.png").drawItems(items);
//		 System.out.println("End Visualisation: " + new Date());

		if (calcDistancesNew) {
			System.out.println("Starting calcDistance: " + new Date());
			calcDistances(items, keywords, distanceType);
			persistDisatnces("results/distances.csv");
			System.out.println("End calcDistance: " + new Date());
		} else {
			System.out.println("Starting load distances: " + new Date());
			loadDistances("results/distances.csv");
			System.out.println("End load distances: " + new Date());
		}
		System.out.println("End reading data: " + new Date());
	}

	private void loadDistances(String fileName) throws IOException {
		List<String> lines = FileUtils.readLines(new File(fileName));
		for (String line : lines) {
			String[] itemDistances = line.split("#");
			int item1Id = Integer.valueOf(itemDistances[0]);
			Item item1 = findItemById(item1Id);
			if (item1 == null) {
				throw new IllegalStateException(String.format(
						"The item %d has no distances in file %s", item1Id,
						fileName));
			}
			for (int j = 1; j < itemDistances.length; j++) {
				String itemDistance = itemDistances[j];
				String[] split = itemDistance.split(";");
				int item2Id = Integer.valueOf(split[0]);
				Item item2 = findItemById(item2Id);   
				if (item2 == null) {
					throw new IllegalStateException(
							"This should never have happend! :'(");
				}
				double distance = Double.valueOf(split[1]);
				item1.setDistance(item2, distance);
			}
		}

	}

	private Item findItemById(int itemId) {
		for (Item item : items) {
			if (item.getItemNumber() == itemId) {
				return item;
			}
		}
		return null;
	}

	private void persistDisatnces(String fileName) throws IOException {
		FileWriter fileWriter = new FileWriter(fileName);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		for (Item item1 : items) {
			StringBuilder sb = new java.lang.StringBuilder("");
			sb.append(item1.getItemNumber());
			sb.append("#");
			for (Item item2 : items) {
				sb.append(item2.getItemNumber());
				sb.append(";");
				sb.append(item1.getDistance(item2));
				sb.append("#");
			}
			sb.append(String.format("\n", new Object[0]));
			bufferedWriter.write(sb.toString());
			bufferedWriter.flush();
		}
		bufferedWriter.close();
	}

	private void filter(List<Item> items, Set<String> keywords) {
		// reduce dataset dimension (remove keywords from items that are not
		// element of target keywords set)
		for (Item item : items) {
			item.retainKeywords(keywords);
		}

		// remove items that do not have any keywords (must happen after
		// reducing the filters)
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
	 * @param distanceType 
	 * @param atLeast2Keywords
	 */
	private void calcDistances(List<Item> items, Set<String> keywords, DistanceTypes distanceType) {
		for (int i = 0; i < items.size(); i++) {
			for (int j = i + 1; j < items.size(); j++) {
				Item item1 = items.get(i);
				item1.setDistance(item1, 0.0);
				Item item2 = items.get(j);
				item2.setDistance(item2, 0.0);
				double distance = DistanceUtil.calcDistance(item1, item2,
						keywords, distanceType);
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
		if (atLeast2Keywords == null) {
			atLeast2Keywords = getAtLeastNTimesKeywords(2);
		}
		return atLeast2Keywords;
	}

	/**
	 * return keywords that are present at least n times
	 * 
	 * @param allKeywords2
	 * @param n
	 * @return
	 */
	public Set<String> getAtLeastNTimesKeywords(int n) {
		if (n == 2) {
			Set<String> nonUniqueKeywords = new HashSet<String>();
			nonUniqueKeywords.addAll(allKeywords);
			nonUniqueKeywords.removeAll(uniqueKeywords);
			return nonUniqueKeywords;
		} else {
			Set<String> result = new HashSet<String>();
			Set<KeywordCount> allKeywordCounts = fileUtil.getAllKeywordCounts();
			for (KeywordCount kwc : allKeywordCounts) {
				if (kwc.getCount() >= n) {
					result.add(kwc.getKeyword());
				}
			}
			return result;
		}
	}
	
	/**
	 * return keywords that are present at least n times and sorts the result on keyword frequency
	 * 
	 * @param allKeywords2
	 * @param n
	 * @return
	 */
	public List<String> getSortedAtLeastNTimesKeywords(int n) {
			List<String> result = new ArrayList<String>();
			List<KeywordCount> allKeywordCounts = fileUtil.getSortedKeywordCounts();
			for (KeywordCount kwc : allKeywordCounts) {
				if (kwc.getCount() >= n) {
					result.add(kwc.getKeyword());
				}
			}
			return result;
	}
}
