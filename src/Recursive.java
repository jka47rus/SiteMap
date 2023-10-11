import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Recursive extends RecursiveTask<List<String>> {

    //    static Set<Recursive> list = new HashSet<>();
    private String path;
    private int deepness;

    public Recursive(String path, int deepness) {
        this.path = path;
        this.deepness = deepness;
    }


    @Override
    protected List<String> compute() {

        List<String> links = new ArrayList<>();

        String regex = "https://[a-z.]{1,}[.ru]{2,3}[^,\\s, \\., \", \', \\\\ ]+";
        String tab = "\t";

        try {
//                    Thread.sleep(150);
            Document doc = Jsoup.connect(path.trim())
                    .userAgent("Mozilla").timeout(3000).get();
            Elements elements = doc.getAllElements();
            String text = elements.html();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                if (matcher.group().contains(".ru")) {
                    Recursive r = new Recursive(matcher.group(), getDeepness() + 1);
                    r.fork();
                    r.join();
//                  list.add(r);
                    links.add(tab.repeat(r.getDeepness()) + r.getPath());
                }
            }
        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
        }

        links.add(path);

        links.forEach(System.out::println);


//        try {
//            Files.write(Paths.get("data/file.txt"), (Iterable<? extends CharSequence>) list);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        for (Recursive s : list) {
//            System.out.println(tab.repeat(s.getDeepness()) + s.getPath());
//        }

        return links;
    }

    public String getPath() {
        return path;
    }

    public int getDeepness() {
        return deepness;
    }

}
