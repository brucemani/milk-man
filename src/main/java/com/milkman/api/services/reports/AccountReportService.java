package com.milkman.api.services.reports;

import com.milkman.api.dto.AccountRequestBuilder;
import com.milkman.api.dto.ReportResponse;
import com.milkman.api.dto.ReportRows;
import com.milkman.api.model.Account;
import com.milkman.api.util.common.CommonUtil;
import com.milkman.api.util.enums.Report;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static com.milkman.api.util.enums.DateFormatPatterns.LOCAL_DATE;
import static com.milkman.api.util.enums.Report.*;
import static java.lang.String.valueOf;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.Collectors.groupingBy;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/6/2023
 * @Time: 2:02 AM
 */
@Service
@AllArgsConstructor
@Slf4j
public class AccountReportService extends CommonUtil {


    private String key(@NonNull final Date date, @NonNull final Report report) {
        final LocalDate localDate = dateToLocalDate.apply(date);
        if (report.name().equalsIgnoreCase(DAY_WISE.name())) {
            return localDate.format(ofPattern(LOCAL_DATE.getPattern()));
        } else if (report.name().equalsIgnoreCase(MONTH_WISE.name())) {
            return localDate.getMonthValue() + "-" + localDate.getYear();
        } else if (report.name().equalsIgnoreCase(YEAR_WISE.name())) {
            return valueOf(localDate.getYear());
        } else {
            log.error("Invalid Report type!");
            throw new IllegalArgumentException("Invalid Report type!");
        }
    }

    private void reportGrouping(@NonNull final List<Account> accountList, @NonNull final Map<String, List<ReportRows>> result, @NonNull final Report report) {
        accountList
                .stream()
                .filter(Objects::nonNull)
                .collect(groupingBy(k -> key(k.getCreateDate(), report)))
                .forEach((key, list) -> {
                    final List<ReportRows> collect = list.stream().map(map -> ReportRows
                            .builder()
                            .date(new SimpleDateFormat(LOCAL_DATE.getPattern()).format(map.getCreateDate()))
                            .morningLt(map.getAmLiter() == null ? 0d : map.getAmLiter())
                            .eveningLt(map.getPmLiter() == null ? 0d : map.getPmLiter())
                            .totalLt((map.getAmLiter() == null ? 0d : map.getAmLiter()) + (map.getPmLiter() == null ? 0d : map.getPmLiter()))
                            .build()).toList();
                    result.put(key, collect);
                });
    }

    public ReportResponse prepareReport(@NonNull final AccountRequestBuilder request) {
        final List<Account> accountList = request.getAccountList();
        final Map<String, List<ReportRows>> reportRowsMap = new HashMap<>();
        if (DAY_WISE.name().equalsIgnoreCase(request.getReportBy().name())) {
            reportGrouping(accountList, reportRowsMap, request.getReportBy());
            return ReportResponse.builder().reportBy(DAY_WISE).reportRowList(reportRowsMap).build();
        } else if (MONTH_WISE.name().equalsIgnoreCase(request.getReportBy().name())) {
            reportGrouping(accountList, reportRowsMap, request.getReportBy());
            return ReportResponse.builder().reportBy(MONTH_WISE).reportRowList(reportRowsMap).build();
        } else if (YEAR_WISE.name().equalsIgnoreCase(request.getReportBy().name())) {
            reportGrouping(accountList, reportRowsMap, request.getReportBy());
            return ReportResponse.builder().reportBy(YEAR_WISE).reportRowList(reportRowsMap).build();
        } else {
            log.error("Invalid report requesting type!");
            throw new IllegalArgumentException("Invalid report requesting type!");
        }
    }
}
