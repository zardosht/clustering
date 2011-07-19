package org.clustering.mode.modes;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.clustering.data.FileUtil;
import org.clustering.mode.AbstractMode;
import org.clustering.mode.ModeExec;
import org.clustering.model.Cluster;
import org.clustering.model.Item;
import org.clustering.util.PrintUtil;

public class ProductionMode extends AbstractMode {

	public ProductionMode() {
		super("",
				"To use the clustering results to find similar films. filmId must be between 1 and 1682; kCluster is one of"
						+ " 10, 20, or 70; outputKeyword to print out keywords of films (warning frightening output on the "
						+ "console ;) )\njava -jar clustering.jar filmId kCluster outputKeywords");
	}

	@ModeExec
	public void production(int filmId, int kCluster, boolean outputKeywords)
			throws Exception {
		production(filmId, "results/k" + kCluster + ".res", outputKeywords);
	}

	private void production(int filmId, String file, boolean outputKeywords)
			throws Exception {
		System.out.println("Start reading result file " + new Date());
		FileUtil fileUtil = new FileUtil();
		fileUtil.readInput("data/keywords.txt");
		List<Item> items = fileUtil.getItems();
		List<Cluster> readClusteringResults = fileUtil.readClusteringResults(
				new File(file), items);
		System.out.println("End reading result file " + new Date());
		PrintUtil.printSimilarFilms(readClusteringResults, filmId, items,
				outputKeywords);
	}
}
