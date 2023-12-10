package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;
import ru.job4j.grabber.utils.Post;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HabrCareerParse implements Parse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";
    public static final int PAGE_LIMIT = 5;
    private final DateTimeParser dateTimeParser;
    private int id = 0;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private String retrieveDescription(String link) {
        Connection connection = Jsoup.connect(link);
        Document document = null;
        try {
            document = connection.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element descriptionElement = Objects.requireNonNull(document)
                .select(".vacancy-description__text").first();
        return descriptionElement.text();
    }

    @Override
    public List<Post> list(String link) {
        List<Post> postList = new ArrayList<>();
        for (int pageNumber = 1; pageNumber <= PAGE_LIMIT; pageNumber++) {
            String fullLink = "%s%s%d%s".formatted(link, PREFIX, pageNumber, SUFFIX);
            Connection connection = Jsoup.connect(fullLink);
            Document document = null;
            try {
                document = connection.get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements rows = Objects.requireNonNull(document).select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                Element dateElements = row.select(".vacancy-card__date").first();
                Element dateElement = dateElements.child(0);
                String vacancyName = titleElement.text();
                String linkVac = String.format("%s%s", link, linkElement.attr("href"));
                String description = this.retrieveDescription(linkVac);
                String datetime = dateElement.attr("datetime");
                LocalDateTime date = dateTimeParser.parse(datetime);
                postList.add(getPost(vacancyName, linkVac, description, date));
            });
        }
        return postList;
    }

    private Post getPost(String title, String link, String description, LocalDateTime created) {
        id++;
        return new Post(id, title, link, description, created);
    }

    public static void main(String[] args) throws IOException {
        Parse parser = new HabrCareerParse(new HabrCareerDateTimeParser());
        parser.list(SOURCE_LINK).forEach(post -> System.out.printf("%s%n", post));
    }
}