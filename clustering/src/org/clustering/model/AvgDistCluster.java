package org.clustering.model;

public class AvgDistCluster extends Cluster {

	public AvgDistCluster(int id, Item initialCentoroid) {
		super(id, initialCentoroid);
	}

	@Override
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

}
