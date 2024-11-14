// Batch mode SCAN algorithm that runs for different eps values in [0,1]
// mu = 2 by default


package StructuralClusteringAlgorithmsRelease;

import javax.swing.*;
import java.io.File;
import java.lang.*;
import java.math.*;

public class Main implements Constants {

	public static void main(String[] args) {
		Main main = new Main();
		GUI gui = new GUI(main);
	}

	public void run(String[] args) {
		//String filename = args[0];
		boolean b_directed = false, b_weighted = false;

		if (args[0].equals("d=true")) b_directed = true;
		if (args[1].equals("w=true")) b_weighted = true;

		String input = args[2];
		String output = args[3];

		Network network = new Network(b_directed, b_weighted);
		OpenFile openFile = new OpenFile(network);
		SaveFile saveFile = new SaveFile(network);
		SCAN scan = new SCAN(network);
		Evaluate evaluate = new Evaluate(network);

		openFile.openPairsFile(new File(input));
		network.setSimilarityFunction(COS);
		network.calculateSimilarities();

		// SCAN algorithm
		double modularity = 0.0;
		long start = System.currentTimeMillis();

		for (double eps_iterator = 0.1; eps_iterator <= 1.0; eps_iterator += 0.1) {

			//round eps so that the values are 0.1, 0.2, 0.3, 0.4 etc.
			BigDecimal bd = new BigDecimal(eps_iterator);
			bd = bd.setScale(2, RoundingMode.HALF_UP);
			double eps = bd.doubleValue();

			scan.run(eps, 2);

			long elapsedTimeMillis = System.currentTimeMillis() - start;
			float elapsedTimeSec = elapsedTimeMillis / 1000F;

			System.out.println("\n\neps= " + eps + " mu= " + 2);
			System.out.println("Elapsed time in milliseconds: " + elapsedTimeMillis);
			System.out.println("Elapsed time in seconds: " + elapsedTimeSec);

			if (b_directed) modularity = evaluate.calculateDirectedModularity(NEWMAN_MOD);
			else modularity = evaluate.calculateUndirectedModularity(NEWMAN_MOD);

			saveFile.clustResultsCommaDelimited( new File(output), "TEST_" + b_directed + "_" + b_weighted, eps, 2, modularity);
		}
	}
}