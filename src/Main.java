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

        Entry<String, Integer> vencedor = getTimeComMaisVitorias(fullDataPath);
        System.out.println("O time que mais venceu jogos no ano 2008 foi " +
                vencedor.getKey().replace("\"", "") +
                ", com " + vencedor.getValue() + " vitórias.");

        Entry<String, Integer> estado = getEstadoComMenosJogos(fullDataPath);
        System.out.println("O estado com menos jogos entre 2003 e 2022 foi " +
                estado.getKey().replace("\"", "") + ", com " + estado.getValue() + " jogos.");

        Entry<String, Integer> golsPorJogador = getJogadorComMaisGols(goalsDataPath);
        System.out.println("O jogador que mais fez gols é o: " +
                golsPorJogador.getKey().replace("\"", "") + ", com " + golsPorJogador.getValue() + " gols");

        Entry<String, Integer> penalty = getJogadorComMaisGolsDePenalty(goalsDataPath);
        System.out.println("O jogador que mais fez gols de Penalty é o: " +
                penalty.getKey().replace("\"", "") + ", com " + penalty.getValue() + " gols");

        Entry<String, Integer> golContra = getJogadorComMaisGolsContra(goalsDataPath);
        System.out.println("O jogador que mais fez gols Contra é o: " +
                golContra.getKey().replace("\"", "") + ", com " + golContra.getValue() + " gols contra");

        Entry<String, Integer> cartaoAmarelo = getJogadorComMaisCartaoAmarelo(cartoesDataPath);
        System.out.println("O jogador que mais fez gols Contra é o: " +
                cartaoAmarelo.getKey().replace("\"", "") + ", com " + cartaoAmarelo.getValue() + " cartões amarelo");

        Entry<String, Integer> cartaoVermelho = getJogadorComMaisCartaoVermelho(cartoesDataPath);
        System.out.println("O jogador que mais fez gols Contra é o: " +
                cartaoVermelho.getKey().replace("\"", "") + ", com " + cartaoVermelho.getValue() + " cartões vermelho");

        Entry<String, Integer> maiorPlacar = getPlacarComMaisGols(fullDataPath);
        System.out.println("O placar da partida com mais gols foi: " +
                maiorPlacar.getKey());


    }

    private static Entry<String, Integer> getEstadoComMenosJogos(Path fullDataPath) throws IOException {
        Map<String, Integer> jogosPorEstado = new HashMap<>();
        Files.lines(fullDataPath)
                .skip(1)
                .filter(lines -> validaEntreAnos(lines, 2003, 2022))
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    jogosPorEstado.put(dadosLinha[14], jogosPorEstado.getOrDefault(dadosLinha[14], 0) + 1);
                });
        return Collections.min(jogosPorEstado.entrySet(),
                        Entry.comparingByValue());
    }

    private static Entry<String, Integer> getTimeComMaisVitorias(Path fullDataPath) throws IOException {
        Map<String, Integer> vitoriasPorTime = new HashMap<>();
        Files.lines(fullDataPath)
                .skip(1)
                .filter(lines -> validaAno(lines, 2008))
                .filter(Main::validaEquipe)
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    vitoriasPorTime.put(dadosLinha[10], vitoriasPorTime.getOrDefault(dadosLinha[10], 0) + 1);
                });
        return Collections.max(vitoriasPorTime.entrySet(),
                        Entry.comparingByValue());
    }

    private static Entry<String, Integer> getJogadorComMaisGols(Path goalsDataPath) throws IOException {
        Map<String, Integer> jogadorComMaisGols = new HashMap<>();
        Files.lines(goalsDataPath).skip(1)
                .filter(Main::validaJogador)
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    jogadorComMaisGols.put(dadosLinha[3], jogadorComMaisGols.getOrDefault(dadosLinha[3], 0) + 1);
                });
        return Collections.max(jogadorComMaisGols.entrySet(),
                        Entry.comparingByValue());
    }

    private static Entry<String, Integer> getJogadorComMaisGolsDePenalty(Path goalsDataPath) throws IOException {
        Map<String, Integer> jogadorComMaisGolsDePenalty = new HashMap<>();
        Files.lines(goalsDataPath).skip(1)
                .filter(Main::validaPenalty)
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    String jogador = dadosLinha[3].replace("\"", "");
                    jogadorComMaisGolsDePenalty.put(jogador, jogadorComMaisGolsDePenalty.getOrDefault(jogador, 0) + 1);
                });
        return Collections.max(jogadorComMaisGolsDePenalty.entrySet(),
                        Entry.comparingByValue());
    }

    private static Entry<String, Integer> getJogadorComMaisGolsContra(Path goalsDataPath) throws IOException {
        Map<String, Integer> jogadorComMaisGolsContra = new HashMap<>();
        Files.lines(goalsDataPath).skip(1)
                .filter(Main::validaGolContra)
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    String jogador = dadosLinha[3].replace("\"", "");
                    jogadorComMaisGolsContra.put(jogador, jogadorComMaisGolsContra.getOrDefault(jogador, 0) + 1);
                });
        return Collections.max(jogadorComMaisGolsContra.entrySet(),
                        Entry.comparingByValue());
    }

    private static Entry<String, Integer> getJogadorComMaisCartaoAmarelo(Path cartoesDataPath) throws IOException {
        Map<String, Integer> jogadorComMaisCartaoAmarelo = new HashMap<>();
        Files.lines(cartoesDataPath).skip(1)
                .filter(Main::validaCartaoAmarelo)
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    String jogador = dadosLinha[4].replace("\"", "");
                    jogadorComMaisCartaoAmarelo.put(jogador, jogadorComMaisCartaoAmarelo.getOrDefault(jogador, 0) + 1);
                });
        return Collections.max(jogadorComMaisCartaoAmarelo.entrySet(),
                        Entry.comparingByValue());
    }

    private static Entry<String, Integer> getJogadorComMaisCartaoVermelho(Path cartoesDataPath) throws IOException {
        Map<String, Integer> jogadorComMaisCartaoVermelho = new HashMap<>();
        Files.lines(cartoesDataPath).skip(1)
                .filter(Main::validaCartaoVermelho)
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    String jogador = dadosLinha[4].replace("\"", "");
                    jogadorComMaisCartaoVermelho.put(jogador, jogadorComMaisCartaoVermelho.getOrDefault(jogador, 0) + 1);
                });

        return Collections.max(jogadorComMaisCartaoVermelho.entrySet(),
                Entry.comparingByValue());
    }

    private static Entry<String, Integer> getPlacarComMaisGols(Path fullDataPath) throws IOException {
        Map<String, Integer> placarComMaisGols = new HashMap<>();
        Files.lines(fullDataPath).skip(1)
                .forEach(lines -> {
                    String[] dadosLinha = lines.split(",");
                    String placarVisitante = dadosLinha[12].replace("\"", "");
                    String placarMandante =  dadosLinha[13].replace("\"", "");
                    placarComMaisGols.put(placarVisitante + " x " + placarMandante,
                            Integer.parseInt(placarMandante)+Integer.parseInt(placarVisitante));
                });
        return Collections.max(placarComMaisGols.entrySet(),
                        Entry.comparingByValue());
    }

    private static boolean validaCartaoVermelho(String lines) {
        int colunaCartao = 3;
        String[] dadosLinha = lines.split(",");
        return dadosLinha[colunaCartao].equals("\"Vermelho\"");
    }

    private static boolean validaCartaoAmarelo(String lines) {
        int colunaCartao = 3;
        String[] dadosLinha = lines.split(",");
        return dadosLinha[colunaCartao].equals("\"Amarelo\"");
    }

    private static boolean validaGolContra(String lines) {
        int colunaTipoGol = 5;
        String[] dadosLinha = lines.split(",");
        return dadosLinha[colunaTipoGol].equals("\"Gol Contra\"");
    }

    private static boolean validaPenalty(String lines) {
        int colunaTipoGol = 5;
        String[] dadosLinha = lines.split(",");
        return dadosLinha[colunaTipoGol].equals("\"Penalty\"");
    }


    private static boolean validaJogador(String lines) {
        int colunaJogador = 3;
        String[] dadosLinha = lines.split(",");
        return (!dadosLinha[colunaJogador].equals("\"-\""));
    }

    private static boolean validaEquipe(String lines) {
        int colunaTime = 10;
        String[] dadosLinha = lines.split(",");
        return (!dadosLinha[colunaTime].equals("\"-\""));
    }

    private static boolean validaAno(String lines, int anoVerificador) {
        int colunaData = 2;
        String[] dadosLinha = lines.split(",");
        LocalDate data = LocalDate.parse(dadosLinha[colunaData].replace("\"", ""),
                DateTimeFormatter.ofPattern("d/M/yyyy"));
        return data.getYear() == anoVerificador;
    }

    private static boolean validaEntreAnos(String lines, int anoInicio, int anoFinal) {
        int colunaData = 2;
        String[] dadosLinha = lines.split(",");
        LocalDate data = LocalDate.parse(dadosLinha[colunaData].replace("\"", ""),
                DateTimeFormatter.ofPattern("d/M/yyyy"));
        return (data.getYear() >= anoInicio || data.getYear() <= anoFinal);
    }

}


