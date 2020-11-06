package com.gosu.simpleCovidStats.controller;

import com.gosu.simpleCovidStats.service.CovidDayService;
import com.gosu.simpleCovidStats.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import twitter4j.Status;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import static com.gosu.simpleCovidStats.Consts.ALL_CASES_TEMPLATE;
import static com.gosu.simpleCovidStats.Consts.ASCII_ART_LOGO;
import static com.gosu.simpleCovidStats.Consts.MAX_DAYS;
import static com.gosu.simpleCovidStats.Consts.MAX_NUMBER_ERROR;
import static com.gosu.simpleCovidStats.Consts.MIN_DAYS;

@Controller
@ResponseBody
public class CovidDayController {
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
        appendAllCasesFromTodayOrYesterday(stringBuilder, daysToTweetsMap);

        for (Integer key : keys) {
            stringBuilder.append(covidDayService.getFilledTemplate(daysToTweetsMap.get(key)));
        }
        return stringBuilder.toString();
    }

    private void appendAllCasesFromTodayOrYesterday(StringBuilder stringBuilder, TreeMap<Integer, List<Status>> daysToTweetsMap) {
        try {
            try {
                stringBuilder.append(
                        String.format(ALL_CASES_TEMPLATE,
                                covidDayService.getAllCases(daysToTweetsMap.get(0)),
                                covidDayService.getAllDeaths(daysToTweetsMap.get(0))
                        )
                );
            }catch(Exception ex){
                stringBuilder.append(
                        String.format(ALL_CASES_TEMPLATE,
                                covidDayService.getAllCases(daysToTweetsMap.get(1)),
                                covidDayService.getAllDeaths(daysToTweetsMap.get(1))
                        )
                );
            }
        } catch (Exception ex) {
            stringBuilder.append(ex.getMessage());
        }
    }
}
