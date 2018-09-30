package com.senpure.base.struct;

import java.util.Date;


public class PatternDate {
    private String pattern="yyyy-MM-dd HH:mm:ss";
    private String dateStr;
    private Date date;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
