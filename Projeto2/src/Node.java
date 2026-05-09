import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    public int id;
    public List<Node> blocks;    // Sucessores: Raios que este raio bloqueia
    public List<Node> blockedBy; // Antecessores: Raios que bloqueiam este raio

    public int inDegree;         // Para o Algoritmo de Kahn
    public boolean isRequired;   // Indica se faz parte do problema (tem de sair)

    public Node(int id) {
        this.id = id;
        this.blocks = new ArrayList<>();
        this.blockedBy = new ArrayList<>();
        this.inDegree = 0;
        this.isRequired = false;
    }

    @Override
    public int compareTo(Node other) {
        // Garante que o Eldrin escolhe sempre o ID mais baixo primeiro
        return Integer.compare(this.id, other.id);
    }
}