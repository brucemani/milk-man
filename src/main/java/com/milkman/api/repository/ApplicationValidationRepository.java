package com.milkman.api.repository;

import com.milkman.api.model.ApplicationValidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 8/2/2023
 * @Time: 9:21 PM
 */
@Repository
public interface ApplicationValidationRepository extends JpaRepository<ApplicationValidation, Long> {
    Optional<ApplicationValidation> findByMobileNumberAndOtp(@NonNull final String mobileNumber, @NonNull final Long otp);

    Optional<ApplicationValidation> findByToken(@NonNull final String token);

}
