import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (!scanner.hasNextInt()) return;
        int T = scanner.nextInt();

        for (int t = 0; t < T; t++) {
            int R = scanner.nextInt();
            int C = scanner.nextInt();
            int N = scanner.nextInt();
            int L = scanner.nextInt();
            int B = scanner.nextInt();

            Graph graph = new Graph(B);
            int[][] grid = new int[R][C];
            List<Integer> targets = new ArrayList<>();
            boolean[] isTarget = new boolean[B + 1];

            int[] br = new int[B + 1];
            int[] bc = new int[B + 1];
            int[] bl = new int[B + 1];
            char[] bd = new char[B + 1];

            // LER RAIOS E PREENCHER A GRELHA
            for (int i = 1; i <= B; i++) {
                br[i] = scanner.nextInt();
                bc[i] = scanner.nextInt();
                bl[i] = scanner.nextInt();
                bd[i] = scanner.next().charAt(0);

                int dr = 0, dc = 0;
                if (bd[i] == 'N') dr = -1;
                else if (bd[i] == 'S') dr = 1;
                else if (bd[i] == 'E') dc = 1;
                else if (bd[i] == 'W') dc = -1;

                // Desenhar o raio físico (o espaço que ele ocupa parado)
                for (int k = 0; k < bl[i]; k++) {
                    int currR = br[i] + k * dr;
                    int currC = bc[i] + k * dc;
                    grid[currR][currC] = i; // Grava o ID do raio na célula

                    // Se o raio ocupa o corredor mágico a limpar, ele é um alvo
                    if (currC >= L && currC < L + N) {
                        if (!isTarget[i]) {
                            isTarget[i] = true;
                            targets.add(i);
                        }
                    }
                }
            }

            // SIMULAR A VIAGEM DOS RAIOS (VARRENDO O MAPA TODO À FRENTE DELES)
            for (int i = 1; i <= B; i++) {
                int dr = 0, dc = 0;
                if (bd[i] == 'N') dr = -1;
                else if (bd[i] == 'S') dr = 1;
                else if (bd[i] == 'E') dc = 1;
                else if (bd[i] == 'W') dc = -1;

                // A viagem começa exatamanente 1 célula à frente do fim do próprio raio
                int currR = br[i] + bl[i] * dr;
                int currC = bc[i] + bl[i] * dc;

                // Vai andando para sempre até sair dos limites da grelha
                while (currR >= 0 && currR < R && currC >= 0 && currC < C) {
                    int blockerId = grid[currR][currC];

                    // Se a célula tiver o corpo de um raio (diferente dele próprio)
                    if (blockerId != 0 && blockerId != i) {
                        // O 'blockerId' bloqueia o raio 'i'. Usamos o addEdge aqui!
                        graph.addEdge(blockerId, i);
                    }

                    // Avança 1 passo
                    currR += dr;
                    currC += dc;
                }
            }

            // EXECUTAR O SISTEMA E IMPRIMIR RESULTADOS (Apenas a Main faz prints)
            EldrinSystem system = new EldrinSystem(graph);
            system.findRequiredBeams(targets);
            List<Integer> result = system.solve();

            if (result == null) {
                System.out.println("Disaster");
            } else if (result.isEmpty()) {
                System.out.println("False alarm");
            } else {
                for (int i = 0; i < result.size(); i++) {
                    System.out.print(result.get(i) + (i == result.size() - 1 ? "" : " "));
                }
                System.out.println();
            }
        }
        scanner.close();
    }
}
