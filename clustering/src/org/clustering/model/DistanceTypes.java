package org.clustering.model;

/**
 * Distance/similarity is described usign Operational Taxonomic Units (OTUs).
 * Given items i and j represented as binary vectors of length n, all features
 * we define the following OTUs:
 * a := positive matches (both i and j have a feature) (1, 1)
 * b := i-absence mismatches (item i does not have a feature, but item j has it) (0, 1) 
 * c := j-absence mismatches (item i has a feature, but item j does not have it) (1, 0)
 * d := negative matches (both i and j does not have a feature). 
 * 
 * a+d is the total number of matches, and b+c is the total number of mismatches.
 * a+b+c+d is always equal n. 
 * 
 * Different distance/similarity measures for binary data use these OTUs. 
 * Distance is a metric (see http://en.wikipedia.org/wiki/Metric_%28mathematics%29), similarity is a measure. 
 * Some formulas introduced here are similary measures (like Jakard, or Russell and Rao) and some are distance 
 * measure. We cannot directly turn a similarity to a measure (because these different things). But we do it here
 * using distance = 1.0 - similarity. 
 * 
 * 
 * 
 * 
 * @author zardosht
 *
 */

public enum DistanceTypes {
	
	
	/**
	 * Russell_and_Rao similarity = a / (a + b + c + d)
	 */
	RUSSELL_AND_RAO_SIMILARITY, 
	
	/**
	 * Jaccard similarity = a / (a + b + c) 
	 */
	JACCARD_SIMILARITY,
	
	/**
	 * otsuka similarity = a / pow(((a+b)(a+c)), 0.5)
	 */
	OTSUKA_SIMILARITY, 
	
	/**
	 * lance and williams distance = (b + c) / (2a + b + c)
	 */
	LANCE_AND_WILLIAMS_DIASTANCE, 
	
	/**
	 * chord distance = sqrt(2(1- (a / sqrt((a+b)(a+c)))))
	 */
	CHORD_DISTANCE
}
