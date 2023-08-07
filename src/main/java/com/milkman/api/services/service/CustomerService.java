package com.milkman.api.services.service;

import com.milkman.api.model.Customer;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 7/30/2023
 * @Time: 12:41 PM
 */
public interface CustomerService extends CommonService<Customer,Long>{
    Boolean isEmailAlreadyRegister(String email);
}
