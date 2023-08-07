package com.milkman.api.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/1/2023
 * @Time: 9:31 PM
 */
@Data
@Builder
public class MailSenderResponseBuilder {
    private String receiver;
    private String message;
    private Integer status;
    private String timeStamp;
}
