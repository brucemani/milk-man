package com.milkman.api.repository;

import com.milkman.api.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/28/2023
 * @Time: 10:20 PM
 */
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findAllByCustomerIdAndIsRead(@NonNull Long customerId,@NonNull Boolean isRead);
}
