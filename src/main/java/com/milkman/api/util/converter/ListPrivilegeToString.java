package com.milkman.api.util.converter;

import com.milkman.api.util.enums.Privilege;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.join;
import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/24/2023
 * @Time: 10:55 PM
 */
@Converter
@Slf4j
public class ListPrivilegeToString implements AttributeConverter<List<Privilege>,String> {
    @Override
    public String convertToDatabaseColumn(List<Privilege> privileges) {
        return !isNull(privileges) ? join(",", privileges.stream().map(Privilege::name).collect(toSet())) : "";
    }

    @Override
    public List<Privilege> convertToEntityAttribute(String str) {
        return !isNull(str) ? stream(str.split(",")).map(Privilege::valueOf).toList() : new ArrayList<>();
    }
}
