package com.milkman.api.model;

import jakarta.persistence.Embeddable;
import lombok.*;


/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 7/30/2023
 * @Time: 11:57 AM
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Address {
    private String doorNumber;
    private String postCode;
    private String cityCode;
    private String area;
    private String district;
    private String taluk;
    private String state;
    private String country;
}
