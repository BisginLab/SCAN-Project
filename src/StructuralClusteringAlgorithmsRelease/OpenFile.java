/* author nxyuruk@ualr.edu
 *
 * August 03, 2007
 *
 * This class contains methods to open and load different types of input files
 * Input files: .pairs, .gml, .graphml
 */

package StructuralClusteringAlgorithmsRelease;

import java.io.*;
import java.util.*;

public class OpenFile {

	private final Network net;

	public OpenFile(Network network) {
		this.net = network;
	}

	//
	// open and loads network from plain .pairs file
	// file format: vertex1 vertex2
	// builds the network in the memory
	//
	public void openPairsFile(String filename) {

		String line;
		System.out.println(filename);

		String[] elements;

		try {
			FileInputStream fstream = new FileInputStream(filename);
			BufferedReader in = new BufferedReader(new InputStreamReader(fstream));
			while ((line = in.readLine()) != null) {

				if (line.charAt(0) == '#') continue;

				// Split by space or tab
				elements = line.split(" |\t");

				String vertexA = elements[0];
				String vertexB = elements[1];

				double weight = 1.0;
				if (net.isWeighted()) weight = Double.parseDouble(elements[2]);

				// vertexA, vertexB are names for vertices, as read from the input file
				net.addEdge(vertexA, vertexB, weight);
			}

			in.close();
		} catch (Exception e) {
			System.err.println("File input error");
		}
	}

	/**
	 * Open file for true classes of vertices
	 * File format: vertex-name \t cluster-id
	 * Set cluster ids for the vertices of the network
	 */
	public void openTrueClasses(String filename) {

		String line;
		System.out.println(filename);

		int num_clusters = 0;
		int clusterId = 0;
		TreeMap<String, Integer> clusterIds = new TreeMap<String, Integer>();

		try {
			FileInputStream fstream = new FileInputStream(filename);
			BufferedReader in = new BufferedReader(new InputStreamReader(fstream));

			while ((line = in.readLine()) != null) {
				String[] vertices = line.split("\t");
				String vertex = vertices[0];
				String cluster = vertices[1];

				if (!clusterIds.containsKey(cluster)) {
					clusterIds.put(cluster, num_clusters);
					num_clusters++;
				} else {
					clusterId = clusterIds.get(cluster);
				}

				net.getVertex(vertex).setClusterId(clusterId);
			}

			in.close();
		} catch (Exception e) {
			System.err.println("File input error --true classes file");
		}
	}
}