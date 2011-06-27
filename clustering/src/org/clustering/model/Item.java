package org.clustering.model;

public class Item {
	
	private Cluster cluster; 

	public double getDistance(Item item) {
		return 0;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}
}
