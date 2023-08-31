package com.milkman.api.repository;

import com.milkman.api.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/6/2023
 * @Time: 1:08 AM
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT acc FROM ACCOUNT acc WHERE acc.customerId=?1 AND DATE(acc.createDate) BETWEEN DATE(?2) AND DATE(?3) ORDER BY acc.createDate DESC")
    List<Account> findAllByCreateDateBetweenOrderByCreateDate(@NonNull final Long id,@NonNull final LocalDate fromDate, @NonNull final LocalDate toDate);
    List<Account> findAllByCustomerIdAndCreateDateBetween(@NonNull Long customerId,@NonNull final LocalDate fromDate, @NonNull final LocalDate toDate);
}
