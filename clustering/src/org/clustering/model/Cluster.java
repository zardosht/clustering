package org.clustering.model;

import java.util.ArrayList;
import java.util.List;

public class Cluster {

	private int id; 
	protected Item centroid;
	protected boolean centroidChanged;
	protected List<Item> members;

	public Cluster(int id, Item initialCentoroid) {
		this.id = id;
		this.centroid = initialCentoroid;
		this.centroidChanged = true;
		members = new ArrayList<Item>();
		if(initialCentoroid != null){
			members.add(centroid);
		}
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

	public void computeNewCnetroid(){
		double[] distances = new double[members.size()];
		for (int i = 0; i < members.size(); i++) {
			for (int j = i + 1; j < members.size(); j++) {
				double distance = members.get(i).getDistance(members.get(j));
				distances[i] += distance;
				distances[j] += distance;
			}
		}
		int lastIndex = 0;
		double smallestDistance = Double.MAX_VALUE;
		for (int i = 0; i < distances.length; i++) {
			if (distances[i] < smallestDistance) {
				smallestDistance = distances[i];
				lastIndex = i;
			}
		}
		Item newCentroid = members.get(lastIndex);
		centroidChanged = (newCentroid == centroid) ? false : true;
		centroid = newCentroid;
	}

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
