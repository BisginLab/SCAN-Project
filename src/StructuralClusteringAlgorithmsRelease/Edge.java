/* author nxyuruk@ualr.edu
 *
 * August 02, 2007
 *
 */

package StructuralClusteringAlgorithmsRelease;

public class Edge {

    private final int id;
    private final String vertexA, vertexB;
    private double similarity;

    public Edge(int id, String vertex1, String vertex2) {
        this.id = id;
        this.vertexA = vertex1;
        this.vertexB = vertex2;
        this.similarity = 0;
    }

    public int getId() {
        return this.id;
    }

    public String getVertexA() {
        return this.vertexA;
    }

    public String getVertexB() {
        return this.vertexB;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
}