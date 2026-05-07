import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// 4. CLASSE MAIN (Apenas interface: lê e imprime)
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Verifica se há input para ler
        if (!sc.hasNextInt()) {
            sc.close();
            return;
        }

        // Lê o número total de casos de teste
        int T = sc.nextInt();

        // Instancia o sistema uma única vez
        EldrinSystem system = new EldrinSystem();

        // Ciclo for tradicional para os casos de teste
        for (int t = 0; t < T; t++) {

            // Leitura dos parâmetros do caso de teste atual
            int R = sc.nextInt();
            int C = sc.nextInt();
            int N = sc.nextInt();
            int L = sc.nextInt();
            int B = sc.nextInt();

            // Extrair os dados brutos e empacotá-los no DTO (Data Transfer Object)
            List<EldrinSystem.BeamData> beams = new ArrayList<>();
            for (int i = 1; i <= B; i++) {
                int r = sc.nextInt();
                int c = sc.nextInt();
                int l = sc.nextInt();
                char dir = sc.next().charAt(0);
                beams.add(new EldrinSystem.BeamData(i, r, c, l, dir));
            }

            // O Sistema trata da simulação e das contas complexas
            List<Integer> result = system.solveProblem(R, C, N, L, beams);

            // A Main trata de imprimir com base na resposta recebida
            if (result == null) {
                System.out.println("Disaster");
            } else if (result.isEmpty()) {
                System.out.println("False alarm");
            } else {
                for (int i = 0; i < result.size(); i++) {
                    // Imprime o ID. Se não for o último, adiciona um espaço.
                    System.out.print(result.get(i) + (i == result.size() - 1 ? "" : " "));
                }
                System.out.println(); // Nova linha após a resposta
            }
        }

        sc.close();
    }
}