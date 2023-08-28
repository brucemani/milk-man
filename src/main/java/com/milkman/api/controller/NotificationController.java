package com.milkman.api.controller;

import com.milkman.api.dto.ResponseBuilder;
import com.milkman.api.model.Notification;
import com.milkman.api.services.service.NotificationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import static com.milkman.api.util.enums.ResponseHandler.makeResponse;
import static com.milkman.api.util.enums.Status.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/28/2023
 * @Time: 10:36 PM
 */
@RestController
@RequestMapping(path = "/api/notification")
@AllArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> createNotification(@RequestBody @NonNull @Valid Notification notification) {
        return ok(makeResponse(this.notificationService.save(notification), CREATED.getStatus(), CREATED.getMessage()));
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> findNotification(@PathVariable("id") Long id) {
        return ok(makeResponse(this.notificationService.findById(id), FOUND.getStatus(), FOUND.getMessage()));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> findAllUnReadNotification(@RequestParam("id") Long id) {
        return ok(makeResponse(this.notificationService.findUnSeenNotification(id), FOUND.getStatus(), FOUND.getMessage()));
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> updateNotification(@RequestBody @NonNull @Valid Notification notification) {
        return ok(makeResponse(this.notificationService.updateById(notification), UPDATE.getStatus(), UPDATE.getMessage()));
    }

    @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> deleteNotification(@PathVariable("id") @NonNull Long id) {
        this.notificationService.deleteById(id);
        return ok(makeResponse(null, DELETE.getStatus(), DELETE.getMessage()));
    }

}
