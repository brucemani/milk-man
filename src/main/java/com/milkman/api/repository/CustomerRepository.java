package com.milkman.api.repository;

import com.milkman.api.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
    Integer countCustomersByCustomerEmail(String email);

    Optional<Customer> findByCustomerEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE CUSTOMER SET profile=?1 WHERE customerId=?2")
    void updateCustomerProfileByCustomerId(final byte[] arr,final Long customerId);
}
