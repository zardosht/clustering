package org.clustering.model;

import java.util.List;

public class Cluster {

	private Item centroid;
	private List<Item> members;

	public Cluster(Item initialCentoroid) {
		this.centroid = initialCentoroid;
	}

	public Item getCentroid() {
		return centroid;
	}

	public void addItem(Item item) {
		this.members.add(item);
	}

	public void computeNewCnetroid() {
		// Item newCentroid;
		// this.centorid = newCentroid;
	}

	public void removeItem(Item item) {
		members.remove(item);
	}

	public boolean contains(Item item) {
		return members.contains(item);
	}

}
