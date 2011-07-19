package org.clustering.mode.modes;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.clustering.classifier.Classifier;
import org.clustering.mode.AbstractMode;
import org.clustering.mode.ModeExec;
import org.clustering.model.Cluster;
import org.clustering.util.PrintUtil;
import org.clustering.util.VisualisationUtil;

public class CreateClusterMode extends AbstractMode {

	public CreateClusterMode() {
		super("-cluster","To cluster the data set. kCluster is an integer for number of clusters:\njava -jar clustering.jar -cluster <int:kCluster>");
	}
	
	public void _runCreateCluster(int kCluster) throws Exception {
		System.out.println("Start Clustering: " + new Date());
		readData();
		Set<String> nonUniqueKeywords = getUniqueKeywords();
		Classifier classifier = new Classifier(kCluster, getItems());
		List<Cluster> clusters = classifier.createClusters();
		System.out.println("End Clustering: " + new Date());

		System.out.println("Start writing result file " + new Date());
		
		getFileUtil().wirteClusteringResult(
				new File("results/k" + kCluster + ".res"), clusters);
		
		System.out.println("End writing result file " + new Date());
	
		PrintUtil.printTopTenKeywordsPerCluster(clusters, nonUniqueKeywords);

		new VisualisationUtil(nonUniqueKeywords, "results/clusteredItems.png")
				.drawClusters(clusters);
	}
}
