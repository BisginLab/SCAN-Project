/** author nxyuruk@ualr.edu
 *
 *  August 10, 2007
 *
 * SCAN: Structural Clustering Algorithm for Networks
 * SCAN is one instance of structural clustering algorithms which basically exploits structural-similarity of vertices to find clusters in networks
 * by nature, it is sort of region-growing algorithm
 * If one vertex has sufficient number of neighbors which are similar enough to it, then a cluster grows around that vertex and enlarges through its neighbors as much as possible
 */

// modified for directed networks on September 16, 2008

package StructuralClusteringAlgorithmsRelease;

import java.util.*;

public class SCAN implements Constants {

	private final Network net;
	private final int direction;                // direction for cluster expansion
	private double eps;
	private int mu;

	public SCAN(Network network) {
		this.net = network;
		this.direction = BIDIRECTIONAL;
	}

	public SCAN(Network network, int direction) {
		this.net = network;
		this.direction = direction;
	}

	//
	// algorithm is written based on the pseudocode of Scan in KDD'07 paper
	//
	public void run(double eps, int mu) { // Scan algorithm
		this.eps = eps;
		this.mu = mu;

		int clusterID = 0;
		LinkedList<String> queue = new LinkedList<>();
		Vertex vertex, yVertex, xVertex;
		HashSet<String> epsNeighborhood, epsNeighborhoodY;
		Iterator<Vertex> itVertex = net.getVertexIterator();

		while (itVertex.hasNext()) {
			vertex = itVertex.next();
			if (vertex.getClusterId() == UNCLASSIFIED) {    // for each unclassified vertex

				epsNeighborhood = getEpsNeighborhood(vertex, eps);

				if (epsNeighborhood.size() >= mu) {    // vertex is a core
					clusterID++;
					vertex.setClusterId(clusterID);
					queue.addAll(epsNeighborhood);

					while (!queue.isEmpty()) {    // while queue is not empty
						yVertex = net.getVertex(queue.removeFirst());
						yVertex.setClusterId(clusterID);
						epsNeighborhoodY = getEpsNeighborhood(yVertex, eps);
						Iterator<String> itEpsNeighborY = epsNeighborhoodY.iterator();

						if (epsNeighborhoodY.size() >= mu) { // y is also a core
							while (itEpsNeighborY.hasNext()) {
								String epsNeighborY = itEpsNeighborY.next();
								xVertex = net.getVertex(epsNeighborY);

								switch (xVertex.getClusterId()) {
									case UNCLASSIFIED:
										queue.add(epsNeighborY);
									case NONMEMBER:
										xVertex.setClusterId(clusterID);
								}
							}
						}
					}
					// if vertex is core
				} else {
					vertex.setClusterId(NONMEMBER);
				}
			} // if vertex is unclassified
		} // while

		// determine hubs and outliers
		itVertex = net.getVertexIterator();
		while (itVertex.hasNext()) {
			vertex = itVertex.next();

			// non-member vertices are further classified as outliers or hubs
			if (vertex.getClusterId() == NONMEMBER) {
				Set<String> neighbors = vertex.getNeighborhood();
				Iterator<String> itNeighbors = neighbors.iterator();
				Set<Integer> neighbors_clusters = new HashSet<>();

				while (itNeighbors.hasNext()) {
					String neighbor_s = itNeighbors.next();
					Vertex neighbor = net.getVertex(neighbor_s);
					neighbors_clusters.add(neighbor.getClusterId());
				}

				if (neighbors.size() > 10 && neighbors_clusters.size() > 2) { // neighbors belong to more than two  different clusters
					vertex.setClusterId(HUB);
				} else {
					vertex.setClusterId(OUTLIER);
				}
			}
		}
	} // runScan

	public HashSet<String> getEpsNeighborhood(Vertex vertex, double eps) {
		HashSet<String> epsNeighborhood = new HashSet<>();

		for (String neighbor : vertex.getNeighborhood(this.direction)) {
			double similarity = vertex.getSimilarity(neighbor); // get similarity of vertex to its neighbor
			if (similarity >= eps) {
				epsNeighborhood.add(neighbor);
			}
		}

		return epsNeighborhood;
	}
}