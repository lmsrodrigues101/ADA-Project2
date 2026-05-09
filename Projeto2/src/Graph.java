public class Graph {
    public Node[] nodes;

    public Graph(int numNodes) {
        this.nodes = new Node[numNodes + 1]; // Índices de 1 a B
        for (int i = 1; i <= numNodes; i++) {
            nodes[i] = new Node(i);
        }
    }

    // Cria a dependência: O blocker tem de sair ANTES do blocked
    public void addDependency(int blockerId, int blockedId) {
        Node blocker = nodes[blockerId];
        Node blocked = nodes[blockedId];

        // Como são linhas retas, não há colisões repetidas entre os mesmos dois raios.
        // Assim poupamos o tempo de fazer blocker.blocks.contains(blocked).
        blocker.blocks.add(blocked);
        blocked.blockedBy.add(blocker);
    }
}