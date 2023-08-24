package com.milkman.api.util.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.join;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/24/2023
 * @Time: 10:44 PM
 */
@Converter
@Slf4j
public class ListToStringConvertor implements AttributeConverter<Set<String>, String> {
    @Override
    public String convertToDatabaseColumn(Set<String> list) {
        return !isNull(list) ? join(",", list) : "";
    }

    @Override
    public Set<String> convertToEntityAttribute(String str) {
        return !isNull(str) ? Set.of(str.split(",")) : new HashSet<>();
    }
}
