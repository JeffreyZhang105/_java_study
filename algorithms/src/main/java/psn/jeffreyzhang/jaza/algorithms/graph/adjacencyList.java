package psn.jeffreyzhang.jaza.algorithms.graph;

public class adjacencyList {
	static int INF = Integer.MAX_VALUE;
	int mEdgNum;
	VNode[] mVexs;

	class ENode {
		int ivex;
		int weight;
		ENode nextEdge;
	}

	class VNode {
		char data;
		ENode firstEdge;
	}
}
