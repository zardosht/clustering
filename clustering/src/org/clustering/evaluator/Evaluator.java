package org.clustering.evaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.clustering.model.Cluster;
import org.clustering.model.Item;

public class Evaluator {

	public Evaluator(){
		
	}
	
	public Map<Cluster, List<KeywordCount>> getTopTenKeywordsPerCluster(List<Cluster> clusters){
		Map<Cluster, List<KeywordCount>> result = new HashMap<Cluster, List<KeywordCount>>();
		
		for(Cluster cluster : clusters) {
			Set<String> allKeywordsInCluster = new HashSet<String>();
			for(Item item : cluster.getMembers()) {
				allKeywordsInCluster.addAll(item.getKeywords());
			}

			List<KeywordCount> keyWordCountsInCluster = new ArrayList<KeywordCount>();
			for(String keyword : allKeywordsInCluster) {
				int counter = 0;
				for(Item item : cluster.getMembers()) {
					if(item.getKeywords().contains(keyword)) counter++;
				}
				keyWordCountsInCluster.add(new KeywordCount(keyword, counter));
			}
			Collections.sort(keyWordCountsInCluster, new Comparator<KeywordCount>() {
				@Override
				public int compare(KeywordCount kc1, KeywordCount kc2) {
					return kc1.getCount() > kc2.getCount() ? -1 : 1;
				}
			});
			List<KeywordCount> topTenInCluster = new ArrayList<KeywordCount>();
			int toIndex = keyWordCountsInCluster.size() > 10 ? 10 : keyWordCountsInCluster.size();
			topTenInCluster.addAll(keyWordCountsInCluster.subList(0, toIndex));
			result.put(cluster, topTenInCluster);
			
		}
		return result;
	}
	
	
}
