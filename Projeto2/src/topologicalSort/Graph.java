package topologicalSort;

public class Graph {
    public Node[] nodes;

    public Graph(int numNodes) {
        //Cria os vértices de 1 até B.
        this.nodes = new Node[numNodes + 1];
        for (int i = 1; i <= numNodes; i++) {
            nodes[i] = new Node(i);
        }
    }

    // Cria a dependência: O blocker tem de sair antes do blocked
    public void addDependency(int blockerId, int blockedId) {
        Node blocker = nodes[blockerId];
        Node blocked = nodes[blockedId];

        blocker.blocks.add(blocked);
        blocked.blockedBy.add(blocker);
    }
}