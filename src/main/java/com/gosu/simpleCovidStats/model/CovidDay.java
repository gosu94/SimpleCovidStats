package com.gosu.simpleCovidStats.model;

import com.gosu.simpleCovidStats.Consts;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CovidDay {
    Integer numberOfCases;
    Integer died;
    Integer diedByCorrelation;
    Date date;
    Map<String, Integer> casesByProvinces;

    private CovidDay(CovidDayBuilder builder) {
        this.numberOfCases = builder.numberOfCases;
        this.died = builder.died;
        this.diedByCorrelation = builder.diedByCorrelation;
        this.date = builder.date;
        this.casesByProvinces = builder.casesByProvinces;
    }

    public Integer getNumberOfCases() {
        return numberOfCases;
    }

    public Integer getDied() {
        return died;
    }

    public Integer getDiedByCorrelation() {
        return diedByCorrelation;
    }

    public Map<String, Integer> getCasesByProvinces() {
        return casesByProvinces;
    }

    public Date getDate() {
        return date;
    }

    public String getFilledTemplate() {
        String dateInProperFormat = Consts.DATA_FORMAT.format(date);

        return dateInProperFormat + "\n" +
                "------------------------------------" + "\n" +
                "Wykryte przypadki: " + numberOfCases + "\n" +
                "Zgony: " + died + "\n" +
                "Zgony z powodu chorób współistniejących: " + diedByCorrelation + "\n\n\n";
    }

    public static class CovidDayBuilder {

        Integer numberOfCases;
        Integer died;
        Integer diedByCorrelation;
        Date date;
        Map<String, Integer> casesByProvinces;

        public CovidDayBuilder withNumberOfCases(Integer numberOfCases) {
            this.numberOfCases = numberOfCases;
            return this;
        }

        public CovidDayBuilder withDirectDeaths(Integer died) {
            this.died = died;
            return this;
        }

        public CovidDayBuilder withIndirectDeaths(Integer diedByCorrelation) {
            this.diedByCorrelation = diedByCorrelation;
            return this;
        }

        public CovidDayBuilder withDate(Date date) {
            this.date = date;
            return this;
        }

        public CovidDayBuilder withNumberOfCases(Map<String, Integer> casesByProvinces) {
            this.casesByProvinces = new HashMap<>(casesByProvinces);
            return this;
        }

        public CovidDay build() {
            return new CovidDay(this);
        }
    }
}
