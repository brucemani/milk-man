package com.milkman.api.repository;

import com.milkman.api.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/6/2023
 * @Time: 1:08 AM
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByCreateDateBetweenOrderByCreateDate(@NonNull final Date fromDate, @NonNull final Date toDate);
    List<Account> findAllByCustomerIdAndCreateDateBetween(@NonNull Long customerId,@NonNull final Date fromDate, @NonNull final Date toDate);
}
