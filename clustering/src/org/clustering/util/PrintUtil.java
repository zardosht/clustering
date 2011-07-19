package org.clustering.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.clustering.data.FileUtil;
import org.clustering.evaluator.Evaluator;
import org.clustering.evaluator.KeywordCount;
import org.clustering.model.Cluster;
import org.clustering.model.DistanceTypes;
import org.clustering.model.Item;

public class PrintUtil {

	public static void printTopTenKeywordsPerCluster(List<Cluster> clusters,
			Set<String> allKeywords) {
		Evaluator evaluator = new Evaluator(allKeywords,
				DistanceTypes.JACCARD_SIMILARITY);
		Map<Cluster, List<KeywordCount>> topTenKeywordsPerCluster = evaluator
				.getTopTenKeywordsPerCluster(clusters);

		for (Cluster cluster : topTenKeywordsPerCluster.keySet()) {
			List<KeywordCount> topTenKeywordsInCluster = topTenKeywordsPerCluster
					.get(cluster);
			StringBuilder sbTopTenKeywords = new StringBuilder("");
			for (KeywordCount kc : topTenKeywordsInCluster) {
				sbTopTenKeywords.append(kc.toString());
				sbTopTenKeywords.append("; ");
			}
			System.out.printf("%s: [%s] \n", cluster.toString(),
					sbTopTenKeywords.toString());
		}
	}

	public static void printSimilarFilms(List<Cluster> clusters, int filmId,
			List<Item> items, boolean outputKeywords) throws Exception {
		Item film = null;
		for (Item item : items) {
			if (item.getItemNumber() == filmId) {
				film = item;
				break;
			}
		}
		if (film == null) {
			throw new IllegalStateException(
					"Could not find the film with given id: " + filmId);
		}
		Cluster filmCluster = null;
		for (Cluster cluster : clusters) {
			if (cluster.contains(film)) {
				filmCluster = cluster;
				break;
			}
		}
		if (filmCluster == null) {
			throw new IllegalStateException(
					"The given film is not contained in any cluster. Id: "
							+ filmId);
		}

		Map<Integer, String> map = FileUtil.importMoviesFromFile("data/u.item");

		System.out.println("Looking for similar movies of movie "
				+ film.getItemNumber() + " " + map.get(film.getItemNumber()));

		int max = 20;
		for (Item item : filmCluster.getMembers()) {
			System.out.print("Movie " + item.getItemNumber() + ": "
					+ map.get(item.getItemNumber()) + " ");
			if (outputKeywords)
				System.out.print(item.getKeywords());
			System.out.println("");
			if (max-- <= 0)
				break;
		}

	}
}
