package org.clustering.model;

import java.util.HashMap;
import java.util.HashSet;

public class Item {
	

	private final int itemNumber;
	

	private HashSet<String> keywords;
	private HashMap<Item, Double> distances;

	public Item(int itemNumber) {
		this.itemNumber = itemNumber;
		keywords = new HashSet<String>();
		distances = new HashMap<Item, Double>();
	}

	public double getDistance(Item item) {
		Double dist = distances.get(item);
		return dist;
	}

	public void addKeyword(String keyword) {
		keywords.add(keyword);
	}

	

	public void setDistance(Item item, double distance) {
		distances.put(item, distance);
	}

	public HashSet<String> getKeywords() {
		return keywords;
	}

	public int getItemNumber() {
		return itemNumber;
	}
	
	
	@Override
	public String toString() {
		return "Item "+itemNumber+" keywords: "+keywords.size();
	}

}
