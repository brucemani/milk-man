package com.milkman.api.services.serviceImpl;

import com.milkman.api.model.Notification;
import com.milkman.api.repository.NotificationRepository;
import com.milkman.api.services.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/28/2023
 * @Time: 10:23 PM
 */
@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    @Override
    public Notification save(@NonNull Notification obj) {
        return this.repository.save(obj);
    }

    @Override
    public Optional<Notification> findById(@NonNull Long id) {
        return this.repository.findById(id);
    }

    @Override
    public List<Notification> findAll() {
        return this.repository.findAll();
    }

    @Override
    public Notification updateById(@NonNull Notification obj) {
        return this.repository.save(obj);
    }

    @Override
    public void deleteById(@NonNull Long id) {
        this.repository.deleteById(id);
    }

    @Override
    public List<Notification> findUnSeenNotification(Long customerId) {
        return this.repository.findAllByCustomerIdAndIsRead(customerId,true);
    }
}
