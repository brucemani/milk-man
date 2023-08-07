package com.milkman.api.util.enums;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/1/2023
 * @Time: 9:38 PM
 */
public enum MailTemplate {
    ACTIVATE_REQUEST("Hi $username,\n\tCongratulation your registration successful!. Click this links to activate your account url: $url","username,url"),
    ACTIVATE_SUCCESS("Hi $username,\n\tYour account was activated successfully.","username"),
    OTP("Hi $username,\n\tYour milk man app OTP is $otp","username,otp");
    public final String template;
    public final String params;

    MailTemplate(String template, String params) {
        this.template = template;
        this.params = params;
    }

    public String getTemplate() {
        return template;
    }

    public String getParams() {
        return params;
    }

    @Override
    public String toString() {
        return "MailTemplate{" +
                "template='" + template + '\'' +
                ", params='" + params + '\'' +
                '}';
    }
}
