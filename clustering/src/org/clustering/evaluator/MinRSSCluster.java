//TODO: use MinRSS to evaluate clustering


//package org.clustering.evaluator;
//
//import org.clustering.model.Cluster;
//import org.clustering.model.Item;
//
//public class MinRSSCluster extends Cluster {
//
//	public MinRSSCluster(int id, Item initialCentoroid) {
//		super(id, initialCentoroid);
//	}
//
//	@Override
//	public void computeNewCnetroid() {
//		//find an element whose RSS is minimum. RSS = sigma(x element Cluster: sqr(dist(x, centroid)))
//		double leastRSS = Double.MAX_VALUE; 
//		Item newCentroid = null;
//		for(Item potentialCentroid : members){
//			double rss = getRss(potentialCentroid);
//			if(rss < leastRSS){
//				leastRSS = rss;
//				newCentroid = potentialCentroid;
//			}
//		}
//		
//		centroidChanged = (newCentroid == centroid) ? false : true;
//		centroid = newCentroid;
//	}
//
//	private double getRss(Item potentialCentroid) {
//		//  RSS = sigma(x element Cluster: pow(dist(x, centroid)), 2))
//		double sum = 0.0;
//		for(Item item : members){
//			double dist = potentialCentroid.getDistance(item);
//			sum += Math.pow(dist, 2);
//		}
//		return sum;
//	}
//
//}
