package com.milkman.api.util.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import static com.milkman.api.util.enums.DateFormatPatterns.QUERY_DATE_TIME;
import static java.time.LocalDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/2/2023
 * @Time: 9:15 PM
 */
@Converter
@Slf4j
public class LocalDateTimeConverter implements AttributeConverter<String, LocalDateTime> {
    @Override
    public LocalDateTime convertToDatabaseColumn(String date) {
        return parse(date, ofPattern(QUERY_DATE_TIME.getPattern()));
    }

    @Override
    public String convertToEntityAttribute(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(ofPattern(QUERY_DATE_TIME.getPattern())) : null;
    }
}
