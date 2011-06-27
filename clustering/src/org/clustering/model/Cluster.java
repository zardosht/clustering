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
		item.setCluster(this);
		item.getCluster().removeItem(item);
		this.members.add(item);
	}


	private void removeItem(Item item) {
		this.members.remove(item);
	}


	public void computeNewCnetroid() {
		//Item newCentroid;
		//this.centorid = newCentroid;
	}
	

}
