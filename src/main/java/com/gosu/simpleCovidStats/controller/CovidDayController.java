package com.gosu.simpleCovidStats.controller;

import com.gosu.simpleCovidStats.Consts;
import com.gosu.simpleCovidStats.model.CovidDay;
import com.gosu.simpleCovidStats.service.CovidDayService;
import com.gosu.simpleCovidStats.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import twitter4j.Status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

@Controller
@ResponseBody
public class CovidDayController {
    private static final String ASCII_ART_LOGO = "   _____________\n" +
            "  / __/ ___/ __/\n" +
            " _\\ \\/ /___\\ \\  \n" +
            "/___/\\___/___/  " +
            "Simple Covid19 Stats\n";
    public static final int MAX_DAYS = 7;
    public static final int MIN_DAYS = 1;
    public static final String MAX_NUMBER_ERROR = "Wybierz liczbe dni z przedziału " + MIN_DAYS + " - " + MAX_DAYS + ".";
    public static final String CANT_FIND_DATA_FOR_DAY_ERROR = "Nie znaleziono danych dla tego dnia";

    @Autowired
    CovidDayService covidDayService;
    @Autowired
    TwitterService twitterService;

    @RequestMapping(value = "days/{days}", produces = "application/json; charset=UTF-8")
    public String covidday(@PathVariable("days") Integer days) {
        TreeMap<Integer, List<Status>> daysToTweetsMap = twitterService.getDaysToTweetsMap(days);
        StringBuilder stringBuilder = new StringBuilder(ASCII_ART_LOGO + "\n\n");

        if (days > MAX_DAYS || days < MIN_DAYS) return stringBuilder.append(MAX_NUMBER_ERROR).toString();

        Set<Integer> keys = daysToTweetsMap.keySet();
        for (Integer reverseKey : keys) {
            try {
                CovidDay covidDay = covidDayService.getACovidDay(daysToTweetsMap.get(reverseKey));
                stringBuilder.append(covidDay.getFilledTemplate());
            } catch (Exception ex) {
                String dateInProperFormat = Consts.DATA_FORMAT.format(daysToTweetsMap.get(reverseKey).get(0).getCreatedAt());
                stringBuilder.append(getExceptionMessage(dateInProperFormat));
            }
        }
        return stringBuilder.toString();
    }

    String getExceptionMessage(String date) {
        return date + "\n------------------------------------\n" + CANT_FIND_DATA_FOR_DAY_ERROR + "\n\n\n";
    }
}
