package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";

    public static void main(String[] args) throws IOException {
        int pageNumber = 1;
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
            Date date;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(datetime);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            System.out.printf("%s %s Вакансия выставлена: %tF%n", vacancyName, link, date);
        });
    }
}