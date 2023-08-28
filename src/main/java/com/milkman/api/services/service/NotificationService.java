package com.milkman.api.services.service;

import com.milkman.api.model.Notification;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/28/2023
 * @Time: 10:21 PM
 */
public interface NotificationService extends CommonService<Notification,Long>{

    List<Notification> findUnSeenNotification(@NotNull Long customerId);
}
