package com.milkman.api.repository;

import com.milkman.api.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 7/30/2023
 * @Time: 12:39 PM
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Integer countCustomersByCustomerEmail(String email);
}
