/* author nxyuruk@ualr.edu
 *
 * August 03, 2007
 *
 */

package StructuralClusteringAlgorithmsRelease;

import java.util.*;

public class Network implements Constants {

	private final TreeMap<String, Vertex> vertexMap;
	private final TreeMap<Integer, Edge> edgeMap;
	private int similarityFunc;

	private double currentSimilarity;
	private int currentSimIndex;

	private final boolean directedNet;
	private final boolean weightedNet;


	public Network(boolean directed, boolean weighted) {
		edgeMap = new TreeMap<>();
		vertexMap = new TreeMap<>();

		//for edge-thresholding algorithm and hierarchical algorithms
		this.currentSimIndex = 0;
		this.currentSimilarity = 0.0;

		this.directedNet = directed; // true if network is directed
		this.weightedNet = weighted; // true if network is weighted
	}


	// by default: there will be an edge to node itself
	// in Adjacency matrix, Aii will be always 1.
	public void addVertex(String vertex_s) {
		vertexMap.put(vertex_s, new Vertex(vertex_s));

		double weight = 1.0;
		if (this.directedNet) addDirectedEdge(vertex_s, vertex_s, weight);
		else addUndirectedEdge(vertex_s, vertex_s, weight);

	}


	// weight will be 1 for un-weighted networks
	public void addEdge(String vertexA_s, String vertexB_s, double weight) {
		if (vertexMap.get(vertexA_s) == null) addVertex(vertexA_s);
		if (vertexMap.get(vertexB_s) == null) addVertex(vertexB_s);

		if (this.directedNet) addDirectedEdge(vertexA_s, vertexB_s, weight);
		else addUndirectedEdge(vertexA_s, vertexB_s, weight);
	}

	//
	// add undirected-edge to the network
	//
	public void addUndirectedEdge(String vertexA_s, String vertexB_s, double weight) {

		Vertex vertexA, vertexB;

		vertexA = vertexMap.get(vertexA_s);
		vertexB = vertexMap.get(vertexB_s);

		if (!vertexA.isNeighbor(vertexB_s) || !vertexB.isNeighbor(vertexA_s)) {
			vertexA.addNeighbor(vertexB_s, weight);
			vertexB.addNeighbor(vertexA_s, weight);

			int id;
			if (!edgeMap.isEmpty()) id = edgeMap.lastKey() + 1;
			else id = 0;

			edgeMap.put(id, new Edge(id, vertexA_s, vertexB_s));
		}
	}

	// add directed edge to the network
	// for directed networks, A B pair shows an edge from A to B (A->B)
	//
	public void addDirectedEdge(String vertexA_s, String vertexB_s, double weight) {

		Vertex vertexA, vertexB;

		vertexA = vertexMap.get(vertexA_s);
		vertexB = vertexMap.get(vertexB_s);

		if (!vertexA.isNeighbor(vertexB_s, OUT) || !vertexB.isNeighbor(vertexA_s, IN)) { // if B is not OUT-Neighbor of A, or A is not IN-Neighbor of A
			vertexA.addNeighbor(vertexB_s, OUT, weight);
			vertexB.addNeighbor(vertexA_s, IN, weight);

			int id;
			if (!edgeMap.isEmpty()) id = edgeMap.lastKey() + 1;
			else id = 0;

			edgeMap.put(id, new Edge(id, vertexA_s, vertexB_s));
		}
	}

	public void calculateSimilarities() { //for each edge in the network
		Iterator<Edge> itEdge = getEdgeIterator();
		Edge edge;
		while (itEdge.hasNext()) {
			edge = itEdge.next();
			Vertex vertexA = vertexMap.get(edge.getVertexA());
			Vertex vertexB = vertexMap.get(edge.getVertexB());
			double sim = vertexA.calculateSimilarity(vertexB, similarityFunc, this.directedNet, this.weightedNet);

			if (!(vertexA.getLabel().equals(vertexB.getLabel()))) {
				System.out.println(vertexA.getLabel() + "\t" + vertexB.getLabel() + "\t" + sim);
			}

			// cosine similarity, and other similarity functions (min, max, jaccard) as well, is symmetric, so sim(A,B) = sim(B,A)
			vertexA.setSimilarity(edge.getVertexB(), sim);
			vertexB.setSimilarity(edge.getVertexA(), sim);
			edge.setSimilarity(sim);
		}
	}

	public Vertex getVertex(String vertexId) {
		return this.vertexMap.get(vertexId);
	}

	public Iterator<Vertex> getVertexIterator() {
		return this.vertexMap.values().iterator();
	}

	public Iterator<Edge> getEdgeIterator() {
		return this.edgeMap.values().iterator();
	}

	public int getNumVertices() {
		return this.vertexMap.values().size();
	}

	public double getSumEdgeFactor(int direction, int optFunction) {
		double totalFactor = 0.0;
		Iterator<Edge> itEdge = getEdgeIterator();
		while (itEdge.hasNext()) {
			Edge edge = itEdge.next();
			Vertex vertexA, vertexB;
			vertexA = getVertex(edge.getVertexA());
			vertexB = getVertex(edge.getVertexB());
			totalFactor += vertexA.getEdgeFactor(vertexB.getLabel(), direction, optFunction);
		}

		return totalFactor;
	}

	public void setSimilarityFunction(int func) {
		this.similarityFunc = func;
	}

	public boolean isWeighted() {
		return this.weightedNet;
	}
}