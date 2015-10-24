package com.travelguide.viewmodel;

import java.util.Calendar;
import java.util.Date;

import static com.travelguide.helpers.DateUtils.formatDate;
import static com.travelguide.helpers.DateUtils.formatMonthName;
import static com.travelguide.helpers.DateUtils.formatSeasonName;

public class TripDateViewModel {

    private final int monthNumber;
    private final String monthName;
    private final String seasonName;
    private final String formattedDate;
    private final Date parsedDate;

    public TripDateViewModel(int year, int monthOfYear, int dayOfMonth) {
        this.monthNumber = monthOfYear;
        this.monthName = formatMonthName(monthOfYear);
        this.seasonName = formatSeasonName(monthOfYear);
        this.formattedDate = formatDate(year, monthOfYear, dayOfMonth);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        this.parsedDate = calendar.getTime();
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public String getMonthName() {
        return monthName;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public Date getParsedDate() {
        return parsedDate;
    }

    @Override
    public String toString() {
        return "TripDateViewModel{" +
                "monthNumber=" + monthNumber +
                ", monthName='" + monthName + '\'' +
                ", seasonName='" + seasonName + '\'' +
                ", formattedDate='" + formattedDate + '\'' +
                ", parsedDate=" + parsedDate +
                '}';
    }
}
