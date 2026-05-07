import java.util.*;

public class EldrinSystem {
    private Graph graph;
    private int requiredCount = 0;

    public EldrinSystem(Graph graph) {
        this.graph = graph;
    }

    // Percurso em Largura (BFS) para encontrar todos os raios que têm mesmo de sair
    public int findRequiredBeams(List<Integer> targetIds) {
        Queue<Node> queue = new LinkedList<>();

        for (int id : targetIds) {
            Node target = graph.getNode(id);
            if (!target.isRequired) {
                target.isRequired = true;
                requiredCount++;
                queue.add(target);
            }
        }

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // Recua nas arestas: quem é que está a bloquear este raio?
            for (Node blocker : current.blockedBy) {
                if (!blocker.isRequired) {
                    blocker.isRequired = true;
                    requiredCount++;
                    queue.add(blocker);
                }
            }
        }
        return requiredCount;
    }

    // Algoritmo de Kahn. Devolve NULL se for Disaster, Lista Vazia se False Alarm, ou a Ordem correta.
    public List<Integer> solve() {
        if (requiredCount == 0) {
            return new ArrayList<>(); // False alarm (lista vazia)
        }

        PriorityQueue<Node> readyQueue = new PriorityQueue<>();

        // Calcular graus de entrada só para quem é obrigatório sair
        for (int i = 1; i <= graph.numNodes; i++) {
            Node n = graph.getNode(i);
            if (n.isRequired) {
                for (Node blocker : n.blockedBy) {
                    if (blocker.isRequired) {
                        n.inDegree++;
                    }
                }
                if (n.inDegree == 0) {
                    readyQueue.add(n);
                }
            }
        }

        List<Integer> escapeOrder = new ArrayList<>();

        while (!readyQueue.isEmpty()) {
            Node current = readyQueue.poll();
            escapeOrder.add(current.id);

            for (Node blocked : current.blocks) {
                if (blocked.isRequired) {
                    blocked.inDegree--;
                    if (blocked.inDegree == 0) {
                        readyQueue.add(blocked);
                    }
                }
            }
        }
        // Teste de Aciclicidade: se não conseguimos tirar todos, há um ciclo (Disaster)
        if (escapeOrder.size() != requiredCount) {
            return null; // Representa Disaster
        }

        return escapeOrder;
    }
}