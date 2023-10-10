import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;

public class Main {

    public static void main(String[] args) throws IOException {
        Path fullDataPath = Path.of("src/data/campeonato-brasileiro-full.csv/");
        Path cartoesDataPath = Path.of("src/data/campeonato-brasileiro-cartoes.csv");
        Path statisticsDataPath = Path.of("src/data/campeonato-brasileiro-estatisticas-full.csv");
        Path goalsDataPath = Path.of("src/data/campeonato-brasileiro-gols.csv");

        System.out.println("\n======= PROJETO FINAL =======");
        System.out.println("=== CAMPEONATO BRASILEIRO ===\n");

        Entry<String, Integer> vencedor = getTeamWithMostWins(fullDataPath);
        System.out.println("O time que mais venceu jogos no ano 2008 foi " +
                vencedor.getKey().replace("\"", "") +
                ", com " + vencedor.getValue() + " vitórias.");

        Entry<String, Integer> estado = getStateWithLessGames(fullDataPath);
        System.out.println("O estado com menos jogos entre 2003 e 2022 foi " +
                estado.getKey().replace("\"", "") + ", com " + estado.getValue() + " jogos.");

        Entry<String, Integer> golsPorJogador = getPlayerWithMostGols(goalsDataPath);
        System.out.println("O jogador que mais fez gols é o: " +
                golsPorJogador.getKey().replace("\"", "") + ", com " + golsPorJogador.getValue() + " gols");

        Entry<String, Integer> penalty = getPlayerWithMostPenaltyGols(goalsDataPath);
        System.out.println("O jogador que mais fez gols de Penalty é o: " +
                penalty.getKey().replace("\"", "") + ", com " + penalty.getValue() + " gols");

        Entry<String, Integer> golContra = getPlayerWithMostCounterGols(goalsDataPath);
        System.out.println("O jogador que mais fez gols Contra é o: " +
                golContra.getKey().replace("\"", "") + ", com " + golContra.getValue() + " gols contra");

        Entry<String, Integer> cartaoAmarelo = getPlayerWithMostYellowCards(cartoesDataPath);
        System.out.println("O jogador que mais tomou cartão amarelo é o: " +
                cartaoAmarelo.getKey().replace("\"", "") + ", com " + cartaoAmarelo.getValue() + " cartões amarelo");

        Entry<String, Integer> cartaoVermelho = getPlayerWithMostRedCards(cartoesDataPath);
        System.out.println("O jogador que mais tomou cartão vermelho é o: " +
                cartaoVermelho.getKey().replace("\"", "") + ", com " + cartaoVermelho.getValue() + " cartões vermelho");

      /*  Entry<String, Integer> maiorPlacar = getScoreMatchWithMostGoals(fullDataPath);
        System.out.println("O placar da partida com mais gols foi: " +
                maiorPlacar.getKey().replace("\"", "") + ", X " + maiorPlacar.getValue() + " gols");

       */


    }

    private static Entry<String, Integer> getStateWithLessGames(Path fullDataPath) throws IOException {
        Map<String, Integer> jogosPorEstado = new HashMap<>();
        Files.lines(fullDataPath)
                .skip(1)
                .filter(lines -> isBetweenYears(lines, 2003, 2022))
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    jogosPorEstado.put(dadosLinha[14], jogosPorEstado.getOrDefault(dadosLinha[14], 0) + 1);
                });
        Entry<String, Integer> minEntry =
                Collections.min(jogosPorEstado.entrySet(),
                        Entry.comparingByValue());
        return minEntry;
    }

    private static Entry<String, Integer> getTeamWithMostWins(Path fullDataPath) throws IOException {
        Map<String, Integer> vitoriasPorTime = new HashMap<>();
        Files.lines(fullDataPath)
                .skip(1)
                .filter(lines -> isInYear(lines, 2008))
                .filter(Main::isTeamValid)
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    vitoriasPorTime.put(dadosLinha[10], vitoriasPorTime.getOrDefault(dadosLinha[10], 0) + 1);
                });
        Entry<String, Integer> maxEntry =
                Collections.max(vitoriasPorTime.entrySet(),
                        Entry.comparingByValue());
        return maxEntry;
    }

    private static Entry<String, Integer> getPlayerWithMostGols(Path goalsDataPath) throws IOException {
        Map<String, Integer> jogadorComMaisGols = new HashMap<>();
        Files.lines(goalsDataPath).skip(1)
                .filter(Main::isPlayerValid)
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    jogadorComMaisGols.put(dadosLinha[3], jogadorComMaisGols.getOrDefault(dadosLinha[3], 0) + 1);
                });
        Entry<String, Integer> maxEntry =
                Collections.max(jogadorComMaisGols.entrySet(),
                        Entry.comparingByValue());
        return maxEntry;
    }

    private static Entry<String, Integer> getPlayerWithMostPenaltyGols(Path goalsDataPath) throws IOException {
        Map<String, Integer> jogadorComMaisGolsDePenalty = new HashMap<>();
        Files.lines(goalsDataPath).skip(1)
                .filter(lines -> isPenaltyValid(lines))
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    String jogador = dadosLinha[3].replace("\"", "");
                    jogadorComMaisGolsDePenalty.put(jogador, jogadorComMaisGolsDePenalty.getOrDefault(jogador, 0) + 1);
                });
        Entry<String, Integer> maxEntry =
                Collections.max(jogadorComMaisGolsDePenalty.entrySet(),
                        Entry.comparingByValue());
        return maxEntry;
    }

    private static Entry<String, Integer> getPlayerWithMostCounterGols(Path goalsDataPath) throws IOException {
        Map<String, Integer> jogadorComMaisGolsContra = new HashMap<>();
        Files.lines(goalsDataPath).skip(1)
                .filter(lines -> isConterGoalValid(lines))
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    String jogador = dadosLinha[3].replace("\"", "");
                    jogadorComMaisGolsContra.put(jogador, jogadorComMaisGolsContra.getOrDefault(jogador, 0) + 1);
                });
        Entry<String, Integer> maxEntry =
                Collections.max(jogadorComMaisGolsContra.entrySet(),
                        Entry.comparingByValue());
        return maxEntry;
    }

    private static Entry<String, Integer> getPlayerWithMostYellowCards(Path cartoesDataPath) throws IOException {
        Map<String, Integer> jogadorComMaisCartaoAmarelo = new HashMap<>();
        Files.lines(cartoesDataPath).skip(1)
                .filter(lines -> isYellowCardValid(lines))
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    String jogador = dadosLinha[4].replace("\"", "");
                    jogadorComMaisCartaoAmarelo.put(jogador, jogadorComMaisCartaoAmarelo.getOrDefault(jogador, 0) + 1);
                });
        Entry<String, Integer> maxEntry =
                Collections.max(jogadorComMaisCartaoAmarelo.entrySet(),
                        Entry.comparingByValue());
        return maxEntry;
    }

    private static Entry<String, Integer> getPlayerWithMostRedCards(Path cartoesDataPath) throws IOException {
        Map<String, Integer> jogadorComMaisCartaoVermelho = new HashMap<>();
        Files.lines(cartoesDataPath).skip(1)
                .filter(lines -> isRedCardValid(lines))
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    String jogador = dadosLinha[4].replace("\"", "");
                    jogadorComMaisCartaoVermelho.put(jogador, jogadorComMaisCartaoVermelho.getOrDefault(jogador, 0) + 1);
                });
        Entry<String, Integer> maxEntry =
                Collections.max(jogadorComMaisCartaoVermelho.entrySet(),
                        Entry.comparingByValue());
        return maxEntry;
    }

  /*  private static Entry<String, Integer> getScoreMatchWithMostGoals(Path fullDataPath) throws IOException {
        Map<String, Integer> placarComMaisGols = new HashMap<>();
        Files.lines(fullDataPath).skip(1)
                .filter(lines -> isScoreMatchValid(lines))
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    String placarVisitante = dadosLinha[12].replace("\"", "");
                    String placarMandante = dadosLinha[13].replace("\"", "");
                    placarComMaisGols.put(placarVisitante, placarComMaisGols.getOrDefault(jogador, 0) + 1);
                });
        Entry<String, Integer> maxEntry =
                Collections.max(placarComMaisGols.entrySet(),
                        Entry.comparingByValue());
        return maxEntry;
    }

   */

   /* private static boolean isScoreMatchValid(String lines) {
        int columnGoalType = 3;
        String[] lineData = lines.split(",");
        return lineData[columnGoalType].equals("\"Vermelho\"");
    }

    */

    private static boolean isRedCardValid(String lines) {
        int columnGoalType = 3;
        String[] lineData = lines.split(",");
        return lineData[columnGoalType].equals("\"Vermelho\"");
    }

    private static boolean isYellowCardValid(String lines) {
        int columnGoalType = 3;
        String[] lineData = lines.split(",");
        return lineData[columnGoalType].equals("\"Amarelo\"");
    }

    private static boolean isConterGoalValid(String lines) {
        int columnGoalType = 5;
        String[] lineData = lines.split(",");
        return lineData[columnGoalType].equals("\"Gol Contra\"");
    }

    private static boolean isPenaltyValid(String lines) {
        int columnGoalType = 5;
        String[] lineData = lines.split(",");
        return lineData[columnGoalType].equals("\"Penalty\"");
    }


    private static boolean isPlayerValid(String lines) {
        int columnPlayer = 3;
        String[] lineData = lines.split(",");
        return (!lineData[columnPlayer].equals("\"-\""));
    }

    private static boolean isTeamValid(String lines) {
        int columnTeam = 10;
        String[] lineData = lines.split(",");
        return (!lineData[columnTeam].equals("\"-\""));
    }

    private static boolean isInYear(String lines, int yearToCheck) {
        int columnDate = 2;
        String[] lineData = lines.split(",");
        LocalDate data = LocalDate.parse(lineData[columnDate].replace("\"", ""),
                DateTimeFormatter.ofPattern("d/M/yyyy"));
        return data.getYear() == yearToCheck;
    }

    private static boolean isBetweenYears(String lines, int startingYear, int endingYear) {
        int columnDate = 2;
        String[] lineData = lines.split(",");
        LocalDate data = LocalDate.parse(lineData[columnDate].replace("\"", ""),
                DateTimeFormatter.ofPattern("d/M/yyyy"));
        return (data.getYear() >= startingYear || data.getYear() <= endingYear);
    }

}


