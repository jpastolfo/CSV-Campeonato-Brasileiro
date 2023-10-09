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

    private static boolean isPlayerValid(String lines){
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


   /* private static Entry<String, Integer> getTeamWithMostWins(Path fullDataPath) throws IOException {
        Map<String,Integer> vitoriasPorTime = new HashMap<>();
        Files.lines(fullDataPath)
                .skip(1)
                .filter(lines -> isInYear(lines,2008))
                .filter(Main::isTeamValid)
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    vitoriasPorTime.put(dadosLinha[10],vitoriasPorTime.getOrDefault(dadosLinha[10],0)+1);
                });
        Entry<String,Integer> maxEntry =
                Collections.max(vitoriasPorTime.entrySet(),
                        Entry.comparingByValue());
        return maxEntry;

    */

    }


