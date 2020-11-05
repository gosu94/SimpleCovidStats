package com.gosu.simpleCovidStats;

import com.gosu.simpleCovidStats.model.CovidDay;
import com.gosu.simpleCovidStats.service.CovidDayService;
import org.junit.jupiter.api.Test;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CovidDayServiceTest {
    private static final String ALL_CASES_TWEET = "Mamy 21 629 nowych i potwierdzonych przypadków" +
            " zakażenia #koronawirus z województw: mazowieckiego (3416), wielkopolskiego (3082)," +
            " kujawsko-pomorskiego (1954), małopolskiego (1914), śląskiego (1761), łódzkiego (1554)," +
            " lubelskiego (1455), podkarpackiego (1274), pomorskiego (1049),";
    private static final Integer ALL_CASES_INT = 21629;
    public static final String NUMBER_OF_CASES_TWEET_EXCEPTION = "Couldn't find number of cases tweet";
    public static final String DEATHS_TWEET_EXCEPTION = "Couldn't find deaths tweet";
    private static final String DEATHS_TWEET = "Z powodu COVID-19 zmarło 35 osób, natomiast" +
            " z powodu współistnienia COVID-19 z innymi schorzeniami zmarło 167 osób";
    private static final String DEATHS_TWEET2 = "Z powodu COVID-19 zmarły 35 osoby, natomiast" +
            " z powodu współistnienia COVID-19 z innymi schorzeniami zmarły 167 osoby";
    private static final Integer DIRECT_DEATHS = 35;
    private static final Integer INDIRECT_DEATHS = 167;

    private CovidDayService covidDayService = new CovidDayService();

    @Test
    public void shouldReturnCovidDay() throws Exception {
        Status allCasesTweet = mock(Status.class);
        Status deathsTweet = mock(Status.class);

        List<Status> listOfTweets = new ArrayList<>();
        Date today = new Date();
        listOfTweets.add(allCasesTweet);
        listOfTweets.add(deathsTweet);

        when(allCasesTweet.getText()).thenReturn(ALL_CASES_TWEET);
        when(deathsTweet.getText()).thenReturn(DEATHS_TWEET);
        when(allCasesTweet.getCreatedAt()).thenReturn(today);

        CovidDay covidDay = covidDayService.getACovidDay(listOfTweets);

        assertEquals(covidDay.getNumberOfCases(), ALL_CASES_INT);
        assertEquals(covidDay.getDeaths(), DIRECT_DEATHS);
        assertEquals(covidDay.getDiedByCorrelation(), INDIRECT_DEATHS);
        assertEquals(covidDay.getDate(), today);
    }

    @Test
    public void shouldReturnCovidDayWithDifferentFormattedTweet() throws Exception {
        Status allCasesTweet = mock(Status.class);
        Status deathsTweet = mock(Status.class);

        List<Status> listOfTweets = new ArrayList<>();
        Date today = new Date();
        listOfTweets.add(allCasesTweet);
        listOfTweets.add(deathsTweet);

        when(allCasesTweet.getText()).thenReturn(ALL_CASES_TWEET);
        when(deathsTweet.getText()).thenReturn(DEATHS_TWEET2);
        when(allCasesTweet.getCreatedAt()).thenReturn(today);

        CovidDay covidDay = covidDayService.getACovidDay(listOfTweets);

        assertEquals(covidDay.getNumberOfCases(), ALL_CASES_INT);
        assertEquals(covidDay.getDeaths(), DIRECT_DEATHS);
        assertEquals(covidDay.getDiedByCorrelation(), INDIRECT_DEATHS);
        assertEquals(covidDay.getDate(), today);
    }

    @Test
    public void shouldThrowExceptionWhenCannotMatchCasesTweet() {
        Status allCasesTweet = mock(Status.class);
        Status deathsTweet = mock(Status.class);

        List<Status> listOfTweets = new ArrayList<>();
        listOfTweets.add(allCasesTweet);
        listOfTweets.add(deathsTweet);

        when(allCasesTweet.getText()).thenReturn("WRONG");
        when(deathsTweet.getText()).thenReturn(DEATHS_TWEET);

        Exception exception = assertThrows(Exception.class, () -> covidDayService.getACovidDay(listOfTweets));

        assertEquals(exception.getMessage(), NUMBER_OF_CASES_TWEET_EXCEPTION);
    }

    @Test
    public void shouldThrowExceptionWhenCannotMatchDeathsTweet() {
        Status allCasesTweet = mock(Status.class);
        Status deathsTweet = mock(Status.class);

        List<Status> listOfTweets = new ArrayList<>();
        listOfTweets.add(allCasesTweet);
        listOfTweets.add(deathsTweet);

        when(allCasesTweet.getText()).thenReturn(ALL_CASES_TWEET);
        when(deathsTweet.getText()).thenReturn("WRONG");

        Exception exception = assertThrows(Exception.class, () -> covidDayService.getACovidDay(listOfTweets));

        assertEquals(exception.getMessage(), DEATHS_TWEET_EXCEPTION);
    }
}