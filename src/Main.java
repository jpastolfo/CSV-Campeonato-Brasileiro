import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Files;

public class Main {

    public static void main(String[] args) throws IOException {

        Path path = Path.of("src/data/campeonato-brasileiro-full.csv/");
        System.out.println(path);

        BufferedReader bufferedReader = new BufferedReader(new FileReader(String.valueOf(path)));

        String indexR;
        while ((indexR = bufferedReader.readLine()) != null) {
            String[] dados = indexR.split(",");
            System.out.println(indexR);



            }

        }

        bufferedReader.close();



    }

}
