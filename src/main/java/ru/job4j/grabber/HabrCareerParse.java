package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";
    public static final int PAGE_LIMIT = 5;

    private String retrieveDescription(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Element descriptionElement = document.select(".vacancy-description__text").first();
        return descriptionElement.text();
    }

    public static void main(String[] args) throws IOException {
        HabrCareerParse habrCareerParse = new HabrCareerParse();
        for (int pageNumber = 1; pageNumber <= PAGE_LIMIT; pageNumber++) {
            System.out.printf("Подключаюсь к %d странице%n", pageNumber);
            String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
            Connection connection = Jsoup.connect(fullLink);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element dateElements = row.select(".vacancy-card__date").first();
                Element linkElement = titleElement.child(0);
                Element dateElement = dateElements.child(0);
                String vacancyName = titleElement.text();
                String datetime = dateElement.attr("datetime");
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                LocalDateTime date = new HabrCareerDateTimeParser().parse(datetime);
                System.out.printf("%s %s Вакансия выставлена: %3$tFT%3$tT%n",
                        vacancyName, link, date);
                try {
                    System.out.println(habrCareerParse.retrieveDescription(link));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}