package org.clustering.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Item implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


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
	
	public void removeKeyword(String keyword){
		keywords.remove(keyword);
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

	/**
	 * retains from this item's keywords, only those who are contained in the given parameter
	 * @param keywords2
	 */
	public void retainKeywords(Set<String> allRetainingKeywords) {
		keywords.retainAll(allRetainingKeywords);
	}

}
