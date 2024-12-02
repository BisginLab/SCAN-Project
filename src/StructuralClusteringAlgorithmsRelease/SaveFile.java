/* author nxyuruk@ualr.edu
 *
 * September 19, 2007
 *
 * This class contains methods to save clustering results into files
 *
 */

package StructuralClusteringAlgorithmsRelease;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SaveFile implements Constants {

	private final Network net;

	public SaveFile(Network network) {
		this.net = network;
	}

	//
	// save clustering results into file
	// file format:
	// cluster1: vertex11, vertex12, vertex13
	// cluster2: vertex21, vertex22, vertex23
	// ...
	public void clustResultsCommaDelimited(File folder, String filename, double eps, int mu, double modularity) {

		int nonmembers = 0;

		// Find an output file that has not been written to yet.
		File output = Paths.get(folder.toURI()).resolve(filename + "__SCAN_clusters.txt").toFile();
		if (output.exists()) {
			int copy = 1;

			do {
				output = Paths.get(folder.toURI()).resolve(filename + "_" + copy + "__SCAN_clusters.txt").toFile();
				copy++;
			} while (output.exists());
		}

		try {
			output.createNewFile();

			// BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
			BufferedWriter out = new BufferedWriter(new FileWriter(output, true));
			out.write("\n\n---------- SCAN result ----------");
			out.write("\n" + filename + ", epsilon=" + eps + ", mu=" + mu);

			//iterate through vertices, build list of vertices for each cluster
			TreeMap<Integer, TreeSet<String>> clusterMembers = new TreeMap<>();
			Iterator<Vertex> itVertex = net.getVertexIterator();
			while (itVertex.hasNext()) {
				Vertex vertex = itVertex.next();
				if (!clusterMembers.containsKey(vertex.getClusterId())) {
					clusterMembers.put(vertex.getClusterId(), new TreeSet<>());
				}
				clusterMembers.get(vertex.getClusterId()).add(vertex.getLabel());
				vertex.setClusterId(UNCLASSIFIED);
			}


			// iterate through found clusters, print members of each cluster
			for (int cluster : clusterMembers.keySet()) {
				TreeSet<String> members = clusterMembers.get(cluster);
				if (members.isEmpty()) continue;
				else {
					out.write(switch (cluster) {
						case OUTLIER -> "\nOUTLIERS:";
						case HUB -> "\nHUBS:";
						default -> "\nCluster[" + cluster + "]:";
					});
				}

				for (String member : members) {
					out.write(member + ",");
					if (cluster == OUTLIER || cluster == HUB) {
						nonmembers++;
					}
				}
			}

			out.write("\nModularity: " + modularity);
			out.close();
		} catch (IOException e) {
			System.out.println("Error in writing to output file!");
		}

		System.out.println("outliers: " + nonmembers);
	}

	public void clustResultsCommaDelimited(String filename) {

		String outFile = filename + "__AHSCAN_clusters.txt";

		try {

			BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
			out.write("###");

			//iterate through vertices, build list of vertices for each cluster
			TreeMap<Integer, TreeSet<String>> clusterMembers = new TreeMap<>();
			Iterator<Vertex> itVertex = net.getVertexIterator();

			while (itVertex.hasNext()) {
				Vertex vertex = itVertex.next();
				if (!clusterMembers.containsKey(vertex.getClusterId())) {
					clusterMembers.put(vertex.getClusterId(), new TreeSet<String>());
				}
				clusterMembers.get(vertex.getClusterId()).add(vertex.getLabel());
			}
			// iterate through found clusters, print members of each cluster
			for (int cluster : clusterMembers.keySet()) {
				TreeSet<String> members = clusterMembers.get(cluster);
				if (members.isEmpty()) continue;
				else out.write("\n" + cluster + ":");

				for (String member : members) out.write(member + ",");

			}
			out.close();
		} catch (IOException e) {
			System.out.println("Error in writing to output file!");
		}
	}

	//
	// save clustering results into file
	// file format:
	// vertex \t cluster \n
	public void clustResultsTabDelimited(String filename) {

		String outFile = filename + "__AHSCAN_clusters.tc";
		try {

			BufferedWriter out = new BufferedWriter(new FileWriter(outFile));

			//iterate through vertices, print vertex label and clusterId
			Iterator<Vertex> itVertex = net.getVertexIterator();
			while (itVertex.hasNext()) {
				Vertex vertex = itVertex.next();
				int cluster = vertex.getClusterId();
				out.write(vertex.getLabel() + "\t" + cluster + "\n");
			}
			out.close();
		} catch (IOException e) {
			System.out.println("Error in writing to output file!");
		}
	}
}