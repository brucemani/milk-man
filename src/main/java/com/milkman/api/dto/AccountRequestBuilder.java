package com.milkman.api.dto;

import com.milkman.api.model.Account;
import com.milkman.api.util.enums.Report;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/6/2023
 * @Time: 1:16 AM
 */
@Data
@Builder
public class AccountRequestBuilder {
    private Long customerId;
    private String from;
    private String to;
    private Report reportBy;
    private List<Account> accountList;
}
