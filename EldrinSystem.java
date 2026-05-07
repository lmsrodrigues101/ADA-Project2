import java.util.*;

public class EldrinSystem {

    // Objeto de Transferência de Dados (DTO) para a Main enviar a informação dos raios
    public static class BeamData {
        public int id, r, c, l;
        public char dir;

        public BeamData(int id, int r, int c, int l, char dir) {
            this.id = id;
            this.r = r;
            this.c = c;
            this.l = l;
            this.dir = dir;
        }
    }

    // Método principal do sistema que orquestra tudo
    public List<Integer> solveProblem(int R, int C, int N, int L, List<BeamData> beams) {
        int B = beams.size();
        Graph graph = new Graph(B);
        int[][] grid = new int[R][C];
        List<Integer> corridorBeams = new ArrayList<>();

        // 1. LÓGICA DE CONSTRUÇÃO DA GRELHA
        for (BeamData b : beams) {
            int dr = 0, dc = 0;
            if (b.dir == 'N') dr = -1;
            else if (b.dir == 'S') dr = 1;
            else if (b.dir == 'E') dc = 1;
            else if (b.dir == 'W') dc = -1;

            // Desenhar o corpo do raio
            for (int k = 0; k < b.l; k++) {
                int currR = b.r + k * dr;
                int currC = b.c + k * dc;
                grid[currR][currC] = b.id;

                // Verificar se toca no corredor alvo
                if (currC >= L && currC < L + N) {
                    if (!corridorBeams.contains(b.id)) {
                        corridorBeams.add(b.id);
                    }
                }
            }
        }

        // 2. SIMULAÇÃO DE COLISÕES (Ray-tracing interno)
        for (BeamData b : beams) {
            int dr = 0, dc = 0;
            if (b.dir == 'N') dr = -1;
            else if (b.dir == 'S') dr = 1;
            else if (b.dir == 'E') dc = 1;
            else if (b.dir == 'W') dc = -1;

            // Começar a viagem imediatamente à frente do raio
            int currR = b.r + b.l * dr;
            int currC = b.c + b.l * dc;

            // Viajar até aos limites do mapa
            while (currR >= 0 && currR < R && currC >= 0 && currC < C) {
                int blockerId = grid[currR][currC];
                if (blockerId != 0 && blockerId != b.id) {
                    graph.addDependency(blockerId, b.id);
                }
                currR += dr;
                currC += dc;
            }
        }

        // 3. EXECUTAR ALGORITMOS DE GRAFOS
        return runEngine(graph, corridorBeams);
    }

    // Lógica privada de Grafos (BFS + Kahn)
    private List<Integer> runEngine(Graph graph, List<Integer> targets) {

        // --- FASE BFS: Encontrar quem tem de sair ---
        Queue<Node> q = new LinkedList<>();
        int reqCount = 0;

        for (int id : targets) {
            Node n = graph.nodes[id];
            if (!n.isRequired) {
                n.isRequired = true;
                reqCount++;
                q.add(n);
            }
        }

        while (!q.isEmpty()) {
            Node curr = q.poll();
            for (Node blocker : curr.blockedBy) {
                if (!blocker.isRequired) {
                    blocker.isRequired = true;
                    reqCount++;
                    q.add(blocker);
                }
            }
        }

        // Se ninguém precisa de sair, é alarme falso
        if (reqCount == 0) {
            return new ArrayList<>();
        }

        // --- FASE KAHN: Ordenação Topológica com PriorityQueue ---
        PriorityQueue<Node> ready = new PriorityQueue<>();

        for (int i = 1; i < graph.nodes.length; i++) {
            Node n = graph.nodes[i];
            if (n.isRequired) {
                for (Node blocker : n.blockedBy) {
                    if (blocker.isRequired) {
                        n.inDegree++;
                    }
                }
                if (n.inDegree == 0) {
                    ready.add(n);
                }
            }
        }

        List<Integer> result = new ArrayList<>();

        while (!ready.isEmpty()) {
            Node curr = ready.poll();
            result.add(curr.id);

            for (Node next : curr.blocks) {
                if (next.isRequired) {
                    next.inDegree--;
                    if (next.inDegree == 0) {
                        ready.add(next);
                    }
                }
            }
        }

        // Teste de Aciclicidade
        return (result.size() == reqCount) ? result : null;
    }
}