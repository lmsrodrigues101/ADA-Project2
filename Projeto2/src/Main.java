import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // Mudar para Buffer

        if (!sc.hasNextInt()) {
            sc.close();
            return;
        }

        int T = sc.nextInt();

        EldrinSystem system = new EldrinSystem();

        for (int t = 0; t < T; t++) {

            int R = sc.nextInt(); // Rows
            int C = sc.nextInt(); // Cols
            int N = sc.nextInt();
            int L = sc.nextInt();
            int B = sc.nextInt(); // Beams

            List<EldrinSystem.BeamData> beams = new ArrayList<>();
            for (int i = 1; i <= B; i++) {
                int r = sc.nextInt(); // começa na célula na linha r
                int c = sc.nextInt(); // e na coluna c
                int l = sc.nextInt(); // com uma length de l
                char dir = sc.next().charAt(0); // no sentido dir (Norte, Sul, Este, Oeste)
                beams.add(new EldrinSystem.BeamData(i, r, c, l, dir));
            }

            List<Integer> result = system.solveProblem(R, C, N, L, beams);

            // Acho que a resposta é enviada pelo sistema e a main só imprime (??)
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

        sc.close();
    }
}