import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Recursive extends RecursiveTask<Set<String>> {

    private static volatile Set<String> checkURL = new HashSet<>();
    private String path;
    private int deepness;

    public Recursive(String path, int deepness) {
        this.path = path;
        this.deepness = deepness;
    }


    @Override
    protected Set<String> compute() {

        Set<String> links = new HashSet<>();
        Set<Recursive> tasks = new HashSet<>();

        String regex = "https://[a-z.]{1,}[.ru]{2,3}[^,\\s, \\#, \\?, \\., \", \', \\\\ ]+";
        String tab = "\t";
        links.add(tab.repeat(deepness) + path);
        System.out.println(tab.repeat(deepness) + path);


        try {
            Thread.sleep(250);
            Document doc = Jsoup.connect(path.trim())
                    .userAgent("Mozilla").get();     // .timeout(3000)
            Elements elements = doc.getAllElements();
            String text = elements.html();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                if (matcher.group().contains(".ru")
                        && matcher.group().startsWith(Main.URL)
                        && !checkURL.contains(matcher.group())) {
                    Recursive r = new Recursive(matcher.group(), getDeepness() + 1); //
                    r.fork();
                    tasks.add(r);
                    checkURL.add(matcher.group());

                }
            }
        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
        }

        for (Recursive task : tasks) {
            links.addAll(task.join());
        }


        return links;
    }

    public String getPath() {
        return path;
    }

    public int getDeepness() {
        return deepness;
    }

}
