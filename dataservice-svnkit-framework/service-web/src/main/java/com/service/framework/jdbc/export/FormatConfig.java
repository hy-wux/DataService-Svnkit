package com.service.framework.jdbc.export;

public class FormatConfig {

    /**
     * The pattern describing the date format. Such as yyyy-MM-dd
     */
    private String datePattern;
    /**
     * The pattern describing the time format. Such as hh:mm:ss
     */
    private String timePattern;
    /**
     * The pattern describing the date and time format. Such as yyyy-MM-dd hh:mm:ss
     */
    private String datetimePattern;

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    public String getTimePattern() {
        return timePattern;
    }

    public void setTimePattern(String timePattern) {
        this.timePattern = timePattern;
    }

    public String getDatetimePattern() {
        return datetimePattern;
    }

    public void setDatetimePattern(String datetimePattern) {
        this.datetimePattern = datetimePattern;
    }
}
