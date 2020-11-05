package com.gosu.simpleCovidStats.service;

import com.gosu.simpleCovidStats.Consts;
import com.gosu.simpleCovidStats.model.CovidDay;
import org.springframework.stereotype.Component;
import twitter4j.Status;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gosu.simpleCovidStats.Consts.COVID_DAY_TEMPLATE;
import static com.gosu.simpleCovidStats.model.CovidDay.*;

@Component
public class CovidDayService {
    public static final String NUMBER_OF_CASES_EXCEPTION = "Couldn't find number of cases tweet";
    public static final String DEATHS_EXCEPTION = "Couldn't find deaths tweet";
    public static final String NUMBER_OF_TESTS_EXCEPTION = "Couldn't find number of tests tweet";
    public static final String NUMBER_OF_CASES_PATTERN = "^[M|m]amy\\s(.*)\\snow.*#koronawirus";
    public static final String DIRECT_DEATHS_PATTERN = "COVID-19\\szmarł.\\s(.*)\\sos.*natomiast";
    public static final String INDIRECT_DEATHS_PATTERN = ".*innymi\\sschorzeniami\\szmarł.\\s(.*)\\sos";
    public static final String NUMBER_OF_TESTS_PATTERN = "W ciągu doby.*\\s(\\d{1,6},?\\d)";
    public static final String NUMBER_OF_ALL_CASES_PATTERN = "Liczba.*wirusem:\\s(.*)\\/\\d";
    public static final String NUMBER_OF_ALL_DEATHS_PATTERN = "Liczba.*\\/(.*)\\s\\(wsz";
    public static final String NUMBER_OF_ALL_CASES_ERROR = "Nie znaleziono danych dla liczby wszystkich zarażeń\n\n";
    public static final String CANT_FIND_DATA_FOR_DAY_ERROR = "Nie znaleziono danych dla tego dnia";

    public CovidDay getACovidDay(List<Status> tweetsOfTheDay) throws Exception {
        return new CovidDayBuilder()
                .withNumberOfCases(getNumberOfCases(tweetsOfTheDay))
                .withDirectDeaths(getNumberOfDirectDeaths(tweetsOfTheDay))
                .withIndirectDeaths(getNumberOfIndirectDeaths(tweetsOfTheDay))
                .withNumberOfTests(getNumberOfTests(tweetsOfTheDay))
                .withDate(tweetsOfTheDay.get(0).getCreatedAt())
                .build();
    }

    public Integer getAllCases(List<Status> tweetsOfTheDay) throws Exception {
        for (Status status : tweetsOfTheDay) {
            Matcher matcher = Pattern.compile(NUMBER_OF_ALL_CASES_PATTERN).matcher(status.getText());
            if (matcher.find()) {
                String trimmedNumberOfCases = matcher.group(1).replaceAll("\\s", "");
                return Integer.valueOf(trimmedNumberOfCases);
            }
        }

        throw new Exception(NUMBER_OF_ALL_CASES_ERROR);
    }

    public Integer getAllDeaths(List<Status> tweetsOfTheDay) throws Exception {
        for (Status status : tweetsOfTheDay) {
            Matcher matcher = Pattern.compile(NUMBER_OF_ALL_DEATHS_PATTERN).matcher(status.getText());
            if (matcher.find()) {
                String trimmedNumberOfDeaths = matcher.group(1).replaceAll("\\s", "");
                return Integer.valueOf(trimmedNumberOfDeaths);
            }
        }

        throw new Exception(NUMBER_OF_ALL_CASES_ERROR);
    }

    private Integer getNumberOfCases(List<Status> tweetsOfTheDay) throws Exception {
        for (Status status : tweetsOfTheDay) {
            Matcher matcher = Pattern.compile(NUMBER_OF_CASES_PATTERN).matcher(status.getText());
            if (matcher.find()) {
                String trimmedNumberOfCases = matcher.group(1).replaceAll("\\s", "");
                return Integer.valueOf(trimmedNumberOfCases);
            }
        }

        throw new Exception(NUMBER_OF_CASES_EXCEPTION);
    }

    private Integer getNumberOfDirectDeaths(List<Status> tweetsOfTheDay) throws Exception {
        for (Status status : tweetsOfTheDay) {
            Matcher matcher = Pattern.compile(DIRECT_DEATHS_PATTERN).matcher(status.getText());
            if (matcher.find()) {
                String trimmedNumberOfDirectDeaths = matcher.group(1).replaceAll("\\s", "");
                return Integer.valueOf(trimmedNumberOfDirectDeaths);
            }
        }

        throw new Exception(DEATHS_EXCEPTION);
    }

    private Integer getNumberOfIndirectDeaths(List<Status> tweetsOfTheDay) throws Exception {
        for (Status status : tweetsOfTheDay) {
            Matcher matcher = Pattern.compile(INDIRECT_DEATHS_PATTERN).matcher(status.getText());
            if (matcher.find()) {
                String trimmedNumberOfInDirectDeaths = matcher.group(1).replaceAll("\\s", "");
                return Integer.valueOf(trimmedNumberOfInDirectDeaths);
            }
        }

        throw new Exception(DEATHS_EXCEPTION);
    }

    private Float getNumberOfTests(List<Status> tweetsOfTheDay) throws Exception {
        for (Status status : tweetsOfTheDay) {
            Matcher matcher = Pattern.compile(NUMBER_OF_TESTS_PATTERN).matcher(status.getText());
            if (matcher.find()) {
                String numberOfTests = matcher.group(1).replaceAll(",", ".").trim();
                return Float.valueOf(numberOfTests);
            }
        }

        throw new Exception(NUMBER_OF_TESTS_EXCEPTION);
    }

    public String getFilledTemplate(List<Status> tweetsOfTheDay) {
        try {
            CovidDay covidDay = getACovidDay(tweetsOfTheDay);

            return String.format(COVID_DAY_TEMPLATE,
                    Consts.DATA_FORMAT.format(covidDay.getDate()),
                    covidDay.getNumberOfCases(),
                    covidDay.getDeaths(),
                    covidDay.getDiedByCorrelation(),
                    covidDay.getNumberOfTests());
        } catch (Exception ex) {
            String dateInProperFormat = Consts.DATA_FORMAT.format(new Date());
            return getExceptionMessage(dateInProperFormat);
        }
    }

    String getExceptionMessage(String date) {
        return date + "\n------------------------------------\n" + CANT_FIND_DATA_FOR_DAY_ERROR + "\n\n\n";
    }
}
