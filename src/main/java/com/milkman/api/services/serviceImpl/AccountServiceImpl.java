package com.milkman.api.services.serviceImpl;

import com.milkman.api.dto.AccountRequestBuilder;
import com.milkman.api.dto.ReportResponse;
import com.milkman.api.model.Account;
import com.milkman.api.repository.AccountRepository;
import com.milkman.api.services.reports.AccountReportService;
import com.milkman.api.services.service.AccountService;
import com.milkman.api.util.common.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/6/2023
 * @Time: 1:09 AM
 */
@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl extends CommonUtil implements AccountService {

    private final AccountRepository repository;
    private final AccountReportService accountReportService;

    @Override
    public Account save(@NonNull final Account obj) {
        return this.repository.save(obj);
    }

    @Override
    public Optional<Account> findById(@NonNull final Long id) {
        return this.repository.findById(id);
    }

    @Override
    public List<Account> findAll() {
        return this.repository.findAll();
    }

    @Override
    public Account updateById(@NonNull final Account obj) {
        this.findById(obj.getId()).orElseThrow(() -> new NullPointerException("Account not exist in DB."));
        return this.save(obj);
    }

    @Override
    public void deleteById(@NonNull Long id) {
        this.repository.deleteById(id);
    }

    @Override
    public List<Account> findAllAccountByDate(@NonNull final AccountRequestBuilder request) {
        return this.repository.findAllByCreateDateBetweenOrderByCreateDate(utilDateConvertor.apply(request.getFrom()), utilDateConvertor.apply(request.getTo()));
    }


    @Override
    public ReportResponse fetchReport(@NonNull final AccountRequestBuilder requestBuilder) {
        final List<Account> accountList = this.findAllAccountByDate(requestBuilder);
        requestBuilder.setAccountList(accountList);
        return accountReportService.prepareReport(requestBuilder);
    }

    public void test(){

    }
}
