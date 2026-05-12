package system;

import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    public int id;
    public List<Node> blocks;  // sucessores, guarda raios que nao podem sair enquanto este nao sair
    public List<Node> blockedBy; // antecessores guarda raios que nao o deixam sair

    public int inDegree;  // diz quantos raios ainda tem à frente
    public boolean isRequired; //  diz nos se este raio faz parte do problema
    // se o raio nao estiver no corredor nem estiver a bloquear ninguem importante fica a false e é ignorado até se
    //verifique necessario

    public Node(int id) {
        this.id = id;
        this.blocks = new ArrayList<>();
        this.blockedBy = new ArrayList<>();
        this.inDegree = 0;
        this.isRequired = false;
    }

    @Override
    public int compareTo(Node other) {
        //É o que permite que a nossa PriorityQueue funcione. Ao comparar os ids, garantimos que a fila nos entrega
        // sempre o raio desimpedido com o número mais baixo (a regra do Eldrin)
        return Integer.compare(this.id, other.id);
    }
}