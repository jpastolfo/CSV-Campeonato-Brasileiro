import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {

        Path path = Path.of("src/data/campeonato-brasileiro-full.csv/");
        System.out.println(path);

        BufferedReader bufferedReader = new BufferedReader(new FileReader(String.valueOf(path)));

        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("d/M/yyyy");

        List<LocalDate> datas = new ArrayList<>();
        Map<String,Integer> timesVencedores = new HashMap<>();
        timesVencedores.put("Bla",1);

        String linha;
        while ((linha = bufferedReader.readLine()) != null) {
            String[] dadosLinha = linha.split(",");

            if (dadosLinha[0].replace("\"","").equals("ID")) {
                System.out.println("ENTROU AQUI");
                continue;
            }

            for (int col = 0; col < dadosLinha.length; col++) {
                if (col == 2) {
                    //System.out.println(LocalDate.parse(dadosLinha[col].replace("\"",""),formatoData));
                    datas.add(LocalDate.parse(dadosLinha[col].replace("\"",""),formatoData));
                }
                if (col == 10) {
                    String vencedor = dadosLinha[col].replace("\"","");
                    timesVencedores.put(vencedor,
                            timesVencedores.getOrDefault(vencedor,0) + 1);
                }

            }

        }

        // TIMES VENCEDORES
        List<String> times = new ArrayList<>(timesVencedores.keySet());
        Collections.sort(times);
        System.out.println(times); // NAO FUNCIONA, FAZ O SORTING POR ORGEM ALFABÉTICA

        // DATAS
        //System.out.println(datas);
        datas.stream().
                filter(p -> p.isAfter(LocalDate.of(2022,11,9))).
                filter(p -> p.isBefore(LocalDate.of(2022,11,13))).
                forEach(System.out::println);




        bufferedReader.close();



    }

}
