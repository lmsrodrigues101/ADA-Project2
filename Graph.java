public class Graph {
    public Node[] nodes;
    public int numNodes;

    public Graph(int numNodes) {
        this.numNodes = numNodes;
        this.nodes = new Node[numNodes + 1]; // Índices de 1 a B
        for (int i = 1; i <= numNodes; i++) {
            nodes[i] = new Node(i);
        }
    }

    // Se 'blocker' bloqueia 'blocked', 'blocked' depende de 'blocker' sair primeiro.
    public void addEdge(int blockerId, int blockedId) {
        Node blocker = nodes[blockerId];
        Node blocked = nodes[blockedId];

        // Evitar arestas repetidas
        if (!blocker.blocks.contains(blocked)) {
            blocker.blocks.add(blocked);
            blocked.blockedBy.add(blocker);
        }
    }

    public Node getNode(int id) {
        return nodes[id];
    }
}