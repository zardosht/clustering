package org.clustering.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Cluster {

	private int id; 
	protected Item centroid;
	protected boolean centroidChanged;
	protected List<Item> members;

	public Cluster(int id, Item initialCentoroid) {
		this.id = id;
		this.centroid = initialCentoroid;
		this.centroidChanged = true;
		members = new ArrayList<Item>();
		members.add(centroid);
	}

	public Item getCentroid() {
		return centroid;
	}

	public void addItem(Item item) {
		this.members.add(item);
	}

	public List<Item> getMembers() {
		return members;
	}

	public abstract void computeNewCnetroid(); 

	public void removeItem(Item item) {
		members.remove(item);
	}

	public boolean contains(Item item) {
		return members.contains(item);
	}

	public boolean centroidChanged() {
		return centroidChanged;
	}
	
	public int getId() {
		return id;
	}


	@Override
	public String toString() {
		return "Cluster " + id + "(Elements: " + getMembers().size() + ")";
	}
	
	

}
