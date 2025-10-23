package com.example.report.query;

import java.util.Date;

public class DailyFeedingQuery {
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public DailyFeedingQuery() {
    }

    public DailyFeedingQuery(Date date) {
        this.date = date;
    }
}
