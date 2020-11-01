package com.gosu.simpleCovidStats.systemtests;

import com.gosu.simpleCovidStats.service.TwitterService;
import org.apache.commons.lang3.time.DateUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import twitter4j.Status;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TwitterServiceTest {

    TwitterService twitterService = new TwitterService();

    @Test
    public void checkConnection() {
        List<String> listOfTweets = twitterService.getTweetsFromHomeLine();

        assertNotNull(listOfTweets);
    }

    @Test
    public void shouldReturnTweetsFromTodayOnly() {
        Map<Integer, List<Status>> map = twitterService.getDaysToTweetsMap(1);
        Date todaysDate = new Date();

        List<Status> tweetsNotFromToday = map.get(0)
                .stream()
                .filter(status -> !DateUtils.isSameDay(status.getCreatedAt(), todaysDate))
                .collect(Collectors.toList());

        assertEquals(tweetsNotFromToday, Lists.emptyList());
    }

    @Test
    public void shouldReturnTweetsFromTwoDays() {
        Map<Integer, List<Status>> map = twitterService.getDaysToTweetsMap(2);
        Date todaysDate = new Date();
        Date yesterdaysDate = DateUtils.addDays(todaysDate, -1);
        int dayOfMonth = LocalDateTime.now().getDayOfMonth();
        int yesterdayOfMonth = LocalDateTime.now().minusDays(1).getDayOfMonth();

        List<Status> tweetsNotFromToday = map.get(dayOfMonth)
                .stream()
                .filter(status -> !DateUtils.isSameDay(status.getCreatedAt(), todaysDate))
                .collect(Collectors.toList());

        List<Status> tweetsFromYesterday = map.get(yesterdayOfMonth)
                .stream()
                .filter(status -> !DateUtils.isSameDay(status.getCreatedAt(), yesterdaysDate))
                .collect(Collectors.toList());

        assertEquals(tweetsNotFromToday, Lists.emptyList());
        assertEquals(tweetsFromYesterday, Lists.emptyList());
    }
}