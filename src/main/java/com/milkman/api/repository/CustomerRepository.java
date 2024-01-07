package com.milkman.api.repository;

import com.milkman.api.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 7/30/2023
 * @Time: 12:39 PM
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Integer countCustomersByCustomerEmail(@NonNull final String email);

    Optional<Customer> findByCustomerEmail(@NonNull final String email);

    Customer updateCustomerByProfileAndCustomerId(@NonNull final byte[] arr, @NonNull final Long customerId);
}
