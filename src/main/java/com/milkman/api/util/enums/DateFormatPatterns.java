package com.milkman.api.util.enums;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/1/2023
 * @Time: 11:10 PM
 */
public enum DateFormatPatterns {

    LOCAL_DATE("dd-MM-yyyy"),
    RESPONSE_DATE_PATTERN("dd-MM-yyyy hh:mm:ss a"),
    QUERY_DATE_TIME("yyyy-MM-dd HH:mm:ss");

    private final String pattern;

    DateFormatPatterns(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return "DateFormatPatterns{" +
                "pattern='" + pattern + '\'' +
                '}';
    }
}
