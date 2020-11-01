package com.gosu.simpleCovidStats.service;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class TwitterService {

    private static final String MZ_TWITTER_ACCOUNT = "MZ_GOV_PL";
    public static final int MAX_NUMBER_OF_TWEETS = 300;
    private static Twitter twitter;

    public TwitterService() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        twitter = new TwitterFactory(cb.build()).getInstance();
    }

    public List<String> getTweetsFromHomeLine() {
        List<Status> statuses = null;

        try {
            statuses = twitter.getHomeTimeline();
            List<String> tweets = new ArrayList<>();

            for (Status status : statuses) {
                tweets.add(status.getUser().getName() + " : " + status.getText());
            }
            return tweets;
        } catch (TwitterException e) {
            System.out.println("Cannot retrieve timeline with error : " + e.getErrorMessage());
        }
        return null;
    }

    /**
     * @param days number of days back from today that will be included in query
     * @return map of days to Tweets posted by MZ for given day
     */
    public TreeMap<Integer, List<Status>> getDaysToTweetsMap(int days) {
        List<Status> allTweets = getAllTweetsFromMZ();
        LocalDateTime todayAtMidnight = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);

        TreeMap<Integer, List<Status>> map = new TreeMap<>();
        for (int i = 0; i < days; i++) {
            LocalDateTime targetDay = todayAtMidnight.minusDays(i);
            Date targetDayDate = Date.from(targetDay.toInstant(ZoneOffset.UTC));

            map.put(i, allTweets
                    .stream()
                    .filter(t -> DateUtils.isSameDay(t.getCreatedAt(), targetDayDate))
                    .collect(Collectors.toList())
            );
        }
        return map;
    }

    public List<Status> getAllTweetsFromMZ() {
        try {
            return twitter.getUserTimeline(MZ_TWITTER_ACCOUNT, new Paging(1, MAX_NUMBER_OF_TWEETS));
        } catch (TwitterException e) {
            System.out.println("Cannot retrieve MZ timeline with error : " + e.getErrorMessage());
        }
        return null;
    }
}
