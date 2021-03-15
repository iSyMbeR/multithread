import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Otodom {
    public static void main(String[] args) throws IOException {

        ExecutorService executorService = Executors.newFixedThreadPool(30);

        URL oracle = new URL("https://www.otodom.pl/sprzedaz/mieszkanie/bialystok/?search%5Bregion_id%5D=10&search%5Bsubregion_id%5D=434&search%5Bcity_id%5D=204");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        String inputLine;
        StringBuilder stringBuilder = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            stringBuilder.append(inputLine);
            stringBuilder.append(System.lineSeparator());
        }
        in.close();
        Set<String> listOfLinks = new HashSet<>();
        String content = stringBuilder.toString();

        for (int i = 0; i < content.length(); i++) {
            i = content.indexOf("https://www.otodom.pl/pl/oferta/", i);
            if (i < 0) {
                break;
            }
            String link = content.substring(i).split(".html")[0];
            listOfLinks.add(link);
        }
        listOfLinks.forEach(System.out::println);

        for (int i = 0; i < listOfLinks.size(); i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    readWebsite(listOfLinks.toArray()[finalI].toString(), finalI + ".html");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
        executorService.shutdown();
    }


    public static void readWebsite(String link, String fileName) throws IOException {
        URL oracle = new URL(link);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));

        String inputLine;
        StringBuilder stringBuilder = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            stringBuilder.append(inputLine);
            stringBuilder.append(System.lineSeparator());
        }
        in.close();

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, false));
        bufferedWriter.write(stringBuilder.toString());
    }
}
