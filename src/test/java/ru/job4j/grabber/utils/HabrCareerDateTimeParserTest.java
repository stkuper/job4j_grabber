package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.*;

class HabrCareerDateTimeParserTest {
    private HabrCareerDateTimeParser dateTimeParser = new HabrCareerDateTimeParser();

    @Test
    public void testDate1() {
        assertThat(dateTimeParser.parse("2023-11-24T20:58:35+03:00"))
                .isEqualTo(LocalDateTime.of(2023, Month.NOVEMBER, 24, 20, 58, 35));
    }

    @Test
    public void testDate2() {
        assertThat(dateTimeParser.parse("2022-01-07T10:28:55+03:00"))
                .isEqualTo(LocalDateTime.of(2022, Month.JANUARY, 7, 10, 28, 55));
    }

    @Test
    public void testDate3() {
        assertThat(dateTimeParser.parse("2024-07-21T22:32:07+03:00"))
                .isEqualTo(LocalDateTime.of(2024, Month.JULY, 21, 22, 32, 7));
    }
}