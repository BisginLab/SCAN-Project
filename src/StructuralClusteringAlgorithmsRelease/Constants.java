package StructuralClusteringAlgorithmsRelease;

interface Constants
{
    int COS = 0;
    int MIN = 1;
    int MAX = 2;
    int JAC = 3;
    int AFF = 4;			// Affinity(v,w) = num-shared-neighbors(v,w) + 1 / num-neighbors(w)

	int UNDEFINED = 0;
    int UNCLASSIFIED = -1;
    int NONMEMBER = -2;
  	int OUTLIER = -3;
    int HUB = -4;


	int IN = 1;
	int OUT = -1;
	int BIDIRECTIONAL = 0;


	int NEWMAN_MOD = 0;
	int SIM_MOD = 1;
	int NEWMAN_WEIGHTED_MOD = 2;
}
