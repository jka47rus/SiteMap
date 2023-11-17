import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;


public class Main {
    public static final String URL = "https://lenta.ru/";

    public static void main(String[] args) {

        ForkJoinPool pool = new ForkJoinPool();
        Set<String> result = pool.invoke(new Recursive(URL, 0));
        pool.shutdown();
        try {
            Files.write(Paths.get("data/file.txt"), result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
