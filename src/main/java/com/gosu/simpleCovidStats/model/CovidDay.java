package com.gosu.simpleCovidStats.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CovidDay {
    Integer numberOfCases;
    Integer died;
    Integer diedByCorrelation;
    Float numberOfTests;
    Date date;
    Map<String, Integer> casesByProvinces;

    private CovidDay(CovidDayBuilder builder) {
        this.numberOfCases = builder.numberOfCases;
        this.died = builder.died;
        this.diedByCorrelation = builder.diedByCorrelation;
        this.numberOfTests = builder.numberOfTests;
        this.date = builder.date;
        this.casesByProvinces = builder.casesByProvinces;
    }

    public Integer getNumberOfCases() {
        return numberOfCases;
    }

    public Integer getDeaths() {
        return died;
    }

    public Integer getDiedByCorrelation() {
        return diedByCorrelation;
    }

    public Map<String, Integer> getCasesByProvinces() {
        return casesByProvinces;
    }

    public Float getNumberOfTests() {
        return numberOfTests;
    }

    public Date getDate() {
        return date;
    }

    public static class CovidDayBuilder {
        Integer numberOfCases;
        Integer died;
        Integer diedByCorrelation;
        Float numberOfTests;
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

        public CovidDayBuilder withNumberOfTests(Float numberOfTests) {
            this.numberOfTests = numberOfTests;
            return this;
        }

        public CovidDayBuilder withDate(Date date) {
            this.date = date;
            return this;
        }

        public CovidDayBuilder withNumberOfCasesByProvince(Map<String, Integer> casesByProvinces) {
            this.casesByProvinces = new HashMap<>(casesByProvinces);
            return this;
        }

        public CovidDay build() {
            return new CovidDay(this);
        }
    }
}
