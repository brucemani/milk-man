package com.milkman.api.mail;

import com.milkman.api.dto.MailSenderRequestBuilder;
import com.milkman.api.dto.MailSenderResponseBuilder;
import com.milkman.api.util.common.TemplateProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.function.Function;

import static com.milkman.api.util.enums.DateFormatPatterns.RESPONSE_DATE_PATTERN;
import static java.time.ZonedDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.http.HttpStatus.OK;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/1/2023
 * @Time: 10:18 PM
 */
@Service
@Slf4j
public class MailSenderServiceImpl implements MailSenderService {

    @Value("${app.mail}")
    private String sender;
    @Autowired
    private TemplateProcessor templateProcessor;
    @Autowired
    private JavaMailSender mailSender;

    private final Function<MailSenderRequestBuilder, SimpleMailMessage> mailBuilder = (req) -> {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(req.getTo());
        mailMessage.setFrom(req.getFrom());
        mailMessage.setText(req.getBody());
        mailMessage.setSubject(req.getSubject());
        return mailMessage;
    };

    private final Function<MailSenderRequestBuilder, MailSenderResponseBuilder> send = (req) -> {
        final SimpleMailMessage mailMessage = this.mailBuilder.apply(req);
        this.mailSender.send(mailMessage);
        return MailSenderResponseBuilder
                .builder()
                .receiver(req.getTo())
                .timeStamp(now().format(ofPattern(RESPONSE_DATE_PATTERN.getPattern())))
                .status(OK.value())
                .message("Mail send successful.")
                .build();
    };

    @Override
    public MailSenderResponseBuilder sendMail(MailSenderRequestBuilder request) {
        final String content = templateProcessor.apply(request.getTemplate(), request.getParams());
        request.setFrom(sender);
        request.setBody(content);
        return send.apply(request);
    }
}
