import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

    // Classe para leitura ultrarrápida (substitui o Scanner)
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    String line = br.readLine();
                    if (line == null) return null; // Fim de ficheiro
                    st = new StringTokenizer(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }

    public static void main(String[] args) {
        FastReader sc = new FastReader();

        String firstToken = sc.next();
        if (firstToken == null) return; // Segurança caso a entrada seja vazia

        int T = Integer.parseInt(firstToken);

        for (int t = 0; t < T; t++) {

            int R = sc.nextInt();
            int C = sc.nextInt();
            int N = sc.nextInt();
            int L = sc.nextInt();
            int B = sc.nextInt();

            EldrinSystem.BeamData[] beams = new EldrinSystem.BeamData[B + 1]; // Índice 1 até B

            for (int i = 1; i <= B; i++) {
                int r = sc.nextInt();
                int c = sc.nextInt();
                int l = sc.nextInt();
                char dir = sc.next().charAt(0);
                beams[i] = new EldrinSystem.BeamData(i, r, c, l, dir);
            }

            // Criamos um novo sistema para cada caso de teste (evita poluição de variáveis antigas)
            EldrinSystem system = new EldrinSystem(R, C, N, L, B, beams);
            List<Integer> result = system.solveProblem();

            // A main apenas lida com o output
            if (result == null) {
                System.out.println("Disaster");
            } else if (result.isEmpty()) {
                System.out.println("False alarm");
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < result.size(); i++) {
                    sb.append(result.get(i)).append(i == result.size() - 1 ? "" : " ");
                }
                System.out.println(sb.toString());
            }
        }
    }
}