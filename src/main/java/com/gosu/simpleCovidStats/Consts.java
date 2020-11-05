package com.gosu.simpleCovidStats;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Consts {
    public static final String ASCII_ART_LOGO = "   _____________\n" +
            "  / __/ ___/ __/\n" +
            " _\\ \\/ /___\\ \\  \n" +
            "/___/\\___/___/  " +
            "Simple Covid19 Stats\n";
    public static final String COVID_DAY_TEMPLATE = "%s\n" +
            "------------------------------------" + "\n" +
            "Wykryte przypadki: %d\n" +
            "Zgony: %d\n" +
            "Zgony z powodu chorób współistniejących: %d\n" +
            "Wykonanych testów: %.1f tys." + "\n\n\n";
    public static final String ALL_CASES_TEMPLATE = "Liczba wszystkich zakażeń: %s\nLiczba wszystkich zgonów: %s\n\n\n";
    public static SimpleDateFormat DATA_FORMAT =
            new SimpleDateFormat("EEEE d MMMM yyyy", Locale.forLanguageTag("pl-PL"));
    public static final int MAX_DAYS = 14;
    public static final int MIN_DAYS = 1;
    public static final String MZ_TWITTER_ACCOUNT = "MZ_GOV_                        PL";
    public static final int NUMBER_OF_TWEETS_PER_DAY = 40;
    public static final String MAX_NUMBER_ERROR = "Wybierz liczbe dni z przedziału " + MIN_DAYS + " - " + MAX_DAYS + ".";
}
