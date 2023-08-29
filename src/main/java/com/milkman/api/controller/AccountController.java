package com.milkman.api.controller;

import com.milkman.api.dto.AccountRequestBuilder;
import com.milkman.api.dto.ResponseBuilder;
import com.milkman.api.model.Account;
import com.milkman.api.services.service.AccountService;
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
 * @Date: 8/25/2023
 * @Time: 12:21 AM
 */
@RestController
@RequestMapping(path = "/api/account")
@AllArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> createAccount(@RequestBody @NonNull @Valid Account account) {
        return ok(makeResponse(accountService.save(account), CREATED.getStatus(), CREATED.getMessage()));
    }


    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> updateAccount(@RequestBody @NonNull @Valid Account account) {
        return ok(makeResponse(accountService.updateById(account), UPDATE.getStatus(), UPDATE.getMessage()));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> findAllAccount(@RequestParam @NonNull Long id, @NonNull String from, @NonNull String to) {
        return ok(makeResponse(accountService.findAllAccountByDate(AccountRequestBuilder.builder().customerId(id).from(from).to(to).build()), FOUND.getStatus(), FOUND.getMessage()));
    }


}
