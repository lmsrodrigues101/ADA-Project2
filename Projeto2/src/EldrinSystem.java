
import topologicalSort.Graph;
import topologicalSort.Node;

import java.util.*;

public class EldrinSystem {


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
                case NORTH:
                    this.dr = -1;
                    this.dc = 0;
                    break;
                case SOUTH:
                    this.dr = 1;
                    this.dc = 0;
                    break;
                case EAST:
                    this.dr = 0;
                    this.dc = 1;
                    break;
                case WEST:
                    this.dr = 0;
                    this.dc = -1;
                    break;
                default:
                    this.dr = 0;
                    this.dc = 0;
            }
        }
    }

    // Constantes para as direções
    public static final char NORTH = 'N';
    public static final char SOUTH = 'S';
    public static final char EAST = 'E';
    public static final char WEST = 'W';

    // Estado do caso de teste
    private int R, C, N, L, B;
    private BeamData[] beams;
    private Graph graph;
    private int[][] grid;
    private List<Integer> corridorBeams;

    public EldrinSystem(int R, int C, int N, int L, int B, BeamData[] beams) {
        this.R = R;
        this.C = C;
        this.N = N;
        this.L = L;
        this.B = B;
        this.beams = beams;
        this.graph = new Graph(B);
        this.grid = new int[R][C];
        this.corridorBeams = new ArrayList<>();
    }

    public List<Integer> solveProblem() {
        // 1: desenha os raios na grelha e anota quem esta diretamente no corredor alvo
        buildGridAndFindTargets();

        // 2: regista no grafo colisoes, quem bloqueia quem
        buildDependencyGraph();

        // 3: usa BFS para descobrir quantos raios têm de sair
        int reqCount = findRequiredBeams();

        // e ninguem precisa de sair, o caminho ja estava livre (False alarm).
        if (reqCount == 0) {
            return new ArrayList<>();
        }

        //  4: usa Topological Sort para ditar a ordem de fuga, e deteta ciclos (Disaster).
        return executeKahnAlgorithm(reqCount);
    }

    private void buildGridAndFindTargets() {
        boolean[] inCorridor = new boolean[B + 1];
        for (int i = 1; i <= B; i++) {
            BeamData b = beams[i];
            for (int k = 0; k < b.l; k++) {
                int currR = b.r + k * b.dr;
                int currC = b.c + k * b.dc;
                grid[currR][currC] = b.id;

                if (currC >= L && currC < L + N) {
                    if (!inCorridor[b.id]) {
                        inCorridor[b.id] = true;
                        corridorBeams.add(b.id);
                    }
                }
            }
        }
    }

    private void buildDependencyGraph() {
        for (int i = 1; i <= B; i++) {
            BeamData b = beams[i];

            int currR = b.r + b.l * b.dr;
            int currC = b.c + b.l * b.dc;

            while (currR >= 0 && currR < R && currC >= 0 && currC < C) {
                int blockerId = grid[currR][currC];
                if (blockerId != 0 && blockerId != b.id) {
                    graph.addDependency(blockerId, b.id);
                }
                currR += b.dr;
                currC += b.dc;
            }
        }
    }

    private int findRequiredBeams() {
        Queue<Node> q = new LinkedList<>();
        int reqCount = 0;

        for (int id : corridorBeams) {
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
        return reqCount;
    }

    private List<Integer> executeKahnAlgorithm(int reqCount) {
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

        List<Integer> escapeOrder = new ArrayList<>();

        while (!ready.isEmpty()) {
            Node curr = ready.poll();
            escapeOrder.add(curr.id);

            for (Node next : curr.blocks) {
                if (next.isRequired) {
                    next.inDegree--;
                    if (next.inDegree == 0) {
                        ready.add(next);
                    }
                }
            }
        }

        return (escapeOrder.size() == reqCount) ? escapeOrder : null;
    }
}
