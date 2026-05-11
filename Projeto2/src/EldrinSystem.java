import topologicalSort.Graph;
import topologicalSort.Node;

import java.util.*;

public class EldrinSystem {

    // Constantes para as direções
    public static final char NORTH = 'N';
    public static final char SOUTH = 'S';
    public static final char EAST = 'E';
    public static final char WEST = 'W';

    // Objeto de Transferência de Dados
    public static class BeamData {
        public int id, r, c, l;
        public char dir;
        public int dr, dc; // Vetores de direção (delta row, delta column)

        public BeamData(int id, int r, int c, int l, char dir) {
            this.id = id;
            this.r = r;
            this.c = c;
            this.l = l;
            this.dir = dir;

            // Tradução da letra para vetores matemáticos (Calculado apenas 1 vez)
            switch (dir) {
                case NORTH: this.dr = -1; this.dc = 0;  break;
                case SOUTH: this.dr = 1;  this.dc = 0;  break;
                case EAST:  this.dr = 0;  this.dc = 1;  break;
                case WEST:  this.dr = 0;  this.dc = -1; break;
                default:    this.dr = 0;  this.dc = 0;
            }
        }
    }

    // Estado do caso de teste
    private int R, C, N, L, B;
    private BeamData[] beams;
    private Graph graph;
    private int[][] grid;
    private boolean[] inCorridor;

    public EldrinSystem(int R, int C, int N, int L, int B, BeamData[] beams) {
        this.R = R;
        this.C = C;
        this.N = N;
        this.L = L;
        this.B = B;
        this.beams = beams;
        this.graph = new Graph(B);
        this.grid = new int[R][C];
        this.inCorridor = new boolean[B + 1];
    }

    public List<Integer> solveProblem() {
        List<Integer> corridorBeams = new ArrayList<>();

        // 1. LÓGICA DE CONSTRUÇÃO DA GRELHA
        for (int i = 1; i <= B; i++) {
            BeamData b = beams[i];

            // Desenhar o corpo do raio usando b.dr e b.dc diretamente
            for (int k = 0; k < b.l; k++) {
                int currR = b.r + k * b.dr;
                int currC = b.c + k * b.dc;
                grid[currR][currC] = b.id;

                // Verificar se toca no corredor alvo
                if (currC >= L && currC < L + N) {
                    if (!inCorridor[b.id]) {
                        inCorridor[b.id] = true;
                        corridorBeams.add(b.id);
                    }
                }
            }
        }

        // 2. SIMULAÇÃO DE COLISÕES (Ray-tracing interno)
        for (int i = 1; i <= B; i++) {
            BeamData b = beams[i];

            // Começar a viagem imediatamente à frente do raio
            int currR = b.r + b.l * b.dr;
            int currC = b.c + b.l * b.dc;

            // Viajar até aos limites do mapa
            while (currR >= 0 && currR < R && currC >= 0 && currC < C) {
                int blockerId = grid[currR][currC];
                if (blockerId != 0 && blockerId != b.id) {
                    graph.addDependency(blockerId, b.id);
                }
                currR += b.dr;
                currC += b.dc;
            }
        }

        return runEngine(corridorBeams);
    }

    // Lógica privada de Grafos (BFS + Kahn)
    private List<Integer> runEngine(List<Integer> targets) {

        // FASE BFS: Encontrar quem tem de sair
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

        if (reqCount == 0) {
            return new ArrayList<>();
        }

        // FASE KAHN: Ordenação Topológica com PriorityQueue
        PriorityQueue<Node> ready = new PriorityQueue<>();

        for (int i = 1; i <= B; i++) {
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