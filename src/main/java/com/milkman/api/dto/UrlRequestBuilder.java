package com.milkman.api.dto;

import com.milkman.api.util.enums.UrlTemplate;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/2/2023
 * @Time: 10:45 PM
 */
@Data
@Builder
public class UrlRequestBuilder {
    private UrlTemplate template;
    private Map<String, String> params;
}
