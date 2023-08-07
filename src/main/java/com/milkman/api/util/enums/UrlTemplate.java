package com.milkman.api.util.enums;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/2/2023
 * @Time: 10:49 PM
 */
public enum UrlTemplate {
    ACTIVATE_ACCOUNT("/validation?");

    private final String template;

    UrlTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
