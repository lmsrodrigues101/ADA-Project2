import system.EldrinSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

    public static final String MSG_DISASTER = "Disaster";
    public static final String MSG_FLS_ALARM = "False alarm";

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String line = br.readLine();
        if (line == null || line.trim().isEmpty()) return;

        int T = Integer.parseInt(line.trim());

        for (int t = 0; t < T; t++) {

            // Lê R e C (na mesma linha)
            StringTokenizer st = new StringTokenizer(br.readLine());
            int R = Integer.parseInt(st.nextToken());
            int C = Integer.parseInt(st.nextToken());

            // Lê N e L (na mesma linha)
            st = new StringTokenizer(br.readLine());
            int N = Integer.parseInt(st.nextToken());
            int L = Integer.parseInt(st.nextToken());

            // Lê B (sozinho numa linha)
            int B = Integer.parseInt(br.readLine().trim());

            EldrinSystem.BeamData[] beams = new EldrinSystem.BeamData[B + 1];

            // informações do raio
            for (int i = 1; i <= B; i++) {
                st = new StringTokenizer(br.readLine());
                int r = Integer.parseInt(st.nextToken());
                int c = Integer.parseInt(st.nextToken());
                int l = Integer.parseInt(st.nextToken());
                char dir = st.nextToken().charAt(0);

                beams[i] = new EldrinSystem.BeamData(i, r, c, l, dir);
            }

            // novo sistema para cada teste
            EldrinSystem system = new EldrinSystem(R, C, N, L, B, beams);
            List<Integer> result = system.solveProblem();


            if (result == null) {
                System.out.println(MSG_DISASTER);
            } else if (result.isEmpty()) {
                System.out.println(MSG_FLS_ALARM);
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