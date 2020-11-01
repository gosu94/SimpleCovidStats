package com.gosu.simpleCovidStats.service;

import com.gosu.simpleCovidStats.model.CovidDay;
import org.springframework.stereotype.Component;
import twitter4j.Status;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gosu.simpleCovidStats.model.CovidDay.*;

@Component
public class CovidDayService {
    public static final String NUMBER_OF_CASES_EXCEPTION = "Couldn't find number of cases tweet";
    public static final String DEATHS_EXCEPTION = "Couldn't find deaths tweet";
    public static final String NUMBER_OF_CASES_PATTERN = "^[M|m]amy\\s(.*)\\snowych.*#koronawirus";
    public static final String DIRECT_DEATHS_PATTERN = "COVID-19\\szmarł.\\s(.*)\\sos.*natomiast";
    public static final String INDIRECT_DEATHS_PATTERN = ".*innymi\\sschorzeniami\\szmarł.\\s(.*)\\sos";

    public CovidDay getACovidDay(List<Status> tweetsOfTheDay) throws Exception{
        return new CovidDayBuilder()
                .withNumberOfCases(getNumberOfCases(tweetsOfTheDay))
                .withDirectDeaths(getNumberOfDirectDeaths(tweetsOfTheDay))
                .withIndirectDeaths(getNumberOfIndirectDeaths(tweetsOfTheDay))
                .withDate(tweetsOfTheDay.get(0).getCreatedAt())
                .build();
    }

    private Integer getNumberOfCases(List<Status> tweetsOfTheDay) throws Exception {
        for (Status status : tweetsOfTheDay) {
            Matcher matcher = Pattern.compile(NUMBER_OF_CASES_PATTERN).matcher(status.getText());
            if (matcher.find()) {
                String trimmedNumberOfCases = matcher.group(1).replaceAll("\\s","");
                return Integer.valueOf(trimmedNumberOfCases);
            }
        }

        throw new Exception(NUMBER_OF_CASES_EXCEPTION);
    }

    private Integer getNumberOfDirectDeaths(List<Status> tweetsOfTheDay) throws Exception {
        for (Status status : tweetsOfTheDay) {
            Matcher matcher = Pattern.compile(DIRECT_DEATHS_PATTERN).matcher(status.getText());
            if (matcher.find()) {
                String trimmedNumberOfDirectDeaths = matcher.group(1).replaceAll("\\s","");
                return Integer.valueOf(trimmedNumberOfDirectDeaths);
            }
        }

        throw new Exception(DEATHS_EXCEPTION);
    }

    private Integer getNumberOfIndirectDeaths(List<Status> tweetsOfTheDay) throws Exception {
        for (Status status : tweetsOfTheDay) {
            Matcher matcher = Pattern.compile(INDIRECT_DEATHS_PATTERN).matcher(status.getText());
            if (matcher.find()) {
                String trimmedNumberOfInDirectDeaths = matcher.group(1).replaceAll("\\s","");
                return Integer.valueOf(trimmedNumberOfInDirectDeaths);
            }
        }

        throw new Exception(DEATHS_EXCEPTION);
    }
}
