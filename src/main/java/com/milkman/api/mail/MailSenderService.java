package com.milkman.api.mail;

import com.milkman.api.dto.MailSenderRequestBuilder;
import com.milkman.api.dto.MailSenderResponseBuilder;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/1/2023
 * @Time: 9:28 PM
 */
public interface MailSenderService {
    MailSenderResponseBuilder sendMail(MailSenderRequestBuilder request);
}
