import java.util.Set;
import java.util.concurrent.ForkJoinPool;


public class Main {

    public static void main(String[] args) {
        String URL = "https://skillbox.ru/";
//        String URL = "https://lenta.ru/";

        new ForkJoinPool().invoke(new Recursive(URL, 0));


    }
}
