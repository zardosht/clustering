package org.clustering;

import java.util.ArrayList;

import org.clustering.mode.AbstractMode;
import org.clustering.mode.modes.CreateClusterMode;
import org.clustering.mode.modes.EvaluateMode;
import org.clustering.mode.modes.HierarchicalClusteringMode;
import org.clustering.mode.modes.ProductionMode;

public class Main {

	public static void main(String[] args) throws Exception {

		ArrayList<AbstractMode> modes = new ArrayList<AbstractMode>();
		modes.add(new CreateClusterMode());
		modes.add(new EvaluateMode());
		modes.add(new ProductionMode());
		modes.add(new HierarchicalClusteringMode());
		
		boolean hasRunned = false;
		for(AbstractMode mode : modes) {
			if(mode.run(args)) {
				hasRunned = true;
				break;
			}
		} 
		
		if(!hasRunned) {
			printUsage(modes);
		}
	}


	private static void printUsage(ArrayList<AbstractMode> modes) {
		System.out.println("Usage: ");
		for(AbstractMode mode : modes) {
			System.out.println(mode.getDescriptino());
		}
	}
}
