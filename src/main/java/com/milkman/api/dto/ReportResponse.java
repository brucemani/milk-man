package com.milkman.api.dto;

import com.milkman.api.util.enums.Report;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/6/2023
 * @Time: 1:53 AM
 */
@Data
@Builder
public class ReportResponse {
    private Report reportBy;
    private Map<String, List<ReportRows>> reportRowList;
}
