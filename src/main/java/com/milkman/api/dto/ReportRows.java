package com.milkman.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/6/2023
 * @Time: 1:55 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportRows {
    private String key;
    private Double morningLt;
    private Double eveningLt;
    private Double totalLt;
    private String date;
}
