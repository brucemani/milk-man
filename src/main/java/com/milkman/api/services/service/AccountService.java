package com.milkman.api.services.service;

import com.milkman.api.dto.AccountRequestBuilder;
import com.milkman.api.dto.ReportResponse;
import com.milkman.api.model.Account;

import java.util.List;
import java.util.Optional;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/6/2023
 * @Time: 1:09 AM
 */
public interface AccountService extends CommonService<Account,Long> {

    List<Account> findAllAccountByDate(AccountRequestBuilder request);

    ReportResponse fetchReport(AccountRequestBuilder requestBuilder);

    Account hasAlreadyEntry(AccountRequestBuilder requestBuilder);
}
