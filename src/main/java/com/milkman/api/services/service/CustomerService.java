package com.milkman.api.services.service;

import com.milkman.api.model.Customer;

import java.util.Optional;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 7/30/2023
 * @Time: 12:41 PM
 */
public interface CustomerService extends CommonService<Customer,Long>{
    Boolean isEmailAlreadyRegister(String email);
    Optional<Customer> findCustomerByEmail(String email);
    void updatePassword(Long userId, String newPassword);
    String readUserProfile(Long customerId);
    void updateUserProfile(Customer customer);
}
