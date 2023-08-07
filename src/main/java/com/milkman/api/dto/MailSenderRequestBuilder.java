package com.milkman.api.dto;

import com.milkman.api.util.enums.MailTemplate;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/1/2023
 * @Time: 9:30 PM
 */
@Data
@Builder
public class MailSenderRequestBuilder {
    private String from;
    private String to;
    private String subject;
    private String body;
    private MailTemplate template;
    private Map<String,String> params;
}
