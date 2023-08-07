package com.milkman.api.util.common;

import com.milkman.api.util.enums.MailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/1/2023
 * @Time: 9:48 PM
 */
@Component
@Slf4j
public class TemplateProcessor implements BiFunction<MailTemplate, Map<String, String>, String> {
    @Override
    public String apply(MailTemplate mailTemplate, Map<String, String> params) {
        requireNonNull(mailTemplate, "Mail template shouldn't be null or empty.");
        requireNonNull(params, "Param value shouldn't be null or empty.");
        final String param = mailTemplate.getParams();
        final String[] content = {mailTemplate.getTemplate()};
        requireNonNull(param, "Error occur during the fetch template param.");
        requireNonNull(content[0], "Error occur during the fetch template content.");
        final String[] split = param.split(",");
        stream(split)
                .filter(Objects::nonNull)
                .filter(params::containsKey)
                .forEachOrdered(k -> content[0] = content[0].replace("$"+k, params.get(k)));
        return content[0];
    }
}
