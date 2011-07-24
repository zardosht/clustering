package org.clustering.mode.modes;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.clustering.classifier.Classifier;
import org.clustering.data.DataUtil;
import org.clustering.mode.AbstractMode;
import org.clustering.model.Cluster;
import org.clustering.model.DistanceTypes;
import org.clustering.util.PrintUtil;
import org.clustering.util.VisualisationUtil;

public class CreateClusterMode extends AbstractMode {

	public CreateClusterMode() {
		super("-cluster","To cluster the data set. kCluster is an integer for number of clusters:\njava -jar clustering.jar -cluster <int:kCluster>");
	}
	
	public void _runCreateCluster(int kCluster) throws Exception {
		System.out.println("Start Clustering: " + new Date());
		DataUtil dataUtil = new DataUtil();
		int nAtLeastKeywords = 10;
		dataUtil.readData(true, nAtLeastKeywords, DistanceTypes.OTSUKA_SIMILARITY);
		Set<String> keywords = dataUtil.getAtLeastNTimesKeywords(nAtLeastKeywords);
		Classifier classifier = new Classifier(kCluster, dataUtil.getItems());
		List<Cluster> clusters = classifier.createClusters();
		System.out.println("End Clustering: " + new Date());

		System.out.println("Start writing result file " + new Date());
		
		dataUtil.getFileUtil().wirteClusteringResult(
				new File("results/k" + kCluster + ".res"), clusters);
		
		System.out.println("End writing result file " + new Date());
	
		PrintUtil.printTopTenKeywordsPerCluster(clusters, keywords);

		new VisualisationUtil(keywords, "results/clusteredItems.png")
				.drawClusters(clusters);
	}
}
