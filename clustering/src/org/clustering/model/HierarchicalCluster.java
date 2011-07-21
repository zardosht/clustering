package org.clustering.model;

import java.util.HashSet;

public class HierarchicalCluster {

	private double simLevel;
	private Item item;
	private HierarchicalCluster firstChild;
	private HierarchicalCluster secondChild;
	private HierarchicalCluster parent;

	public double getSimLevel() {
		return simLevel;
	}

	public HierarchicalCluster getFirstChild() {
		return firstChild;
	}

	public HierarchicalCluster getSecondChild() {
		return secondChild;
	}

	public HierarchicalCluster getParent() {
		return parent;
	}

	public HierarchicalCluster(Item initialCentoroid) {
		this.item = initialCentoroid;
		simLevel = 1;
	}

	public HierarchicalCluster(double maxSim, HierarchicalCluster cluster1,
			HierarchicalCluster cluster2) {
				this.simLevel = maxSim;
				this.firstChild = cluster1;
				this.secondChild = cluster2;
				// set parent
				this.firstChild.setParent(this);
				this.secondChild.setParent(this);
	}

	private void setParent(HierarchicalCluster hierarchicalCluster) {
		this.parent = hierarchicalCluster;
	}
	
	public HashSet<Item> getItems() {
		HashSet<Item> children = new HashSet<Item>();
		getChildren(children);
		return children;
	}

	private void getChildren(HashSet<Item> children) {
		if(item != null) {
			children.add(item);
			return;
		}
		firstChild.getChildren(children);
		secondChild.getChildren(children);
	}

}
