package com.milkman.api.controller;

import com.milkman.api.dto.MailSenderRequestBuilder;
import com.milkman.api.dto.ResponseBuilder;
import com.milkman.api.dto.UrlRequestBuilder;
import com.milkman.api.mail.MailSenderService;
import com.milkman.api.model.Customer;
import com.milkman.api.services.service.CustomerService;
import com.milkman.api.util.common.CommonUtil;
import com.milkman.api.util.enums.Status;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.milkman.api.util.enums.MailTemplate.OTP;
import static com.milkman.api.util.enums.ResponseHandler.makeResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 7/30/2023
 * @Time: 1:25 PM
 */
@RestController
@RequestMapping(path = "/api/customer")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final MailSenderService mailSenderService;
    private final CommonUtil commonUtil;


    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> create(@RequestBody @Valid Customer customer) {
        return ok(makeResponse(customerService.save(customer), Status.CREATED.getStatus(), Status.CREATED.getMessage()));
    }

    @GetMapping(path = "/all", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> findAllCustomer() {
        return ok(makeResponse(customerService.findAll(), Status.FOUND.getStatus(), Status.FOUND.getMessage()));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> findCustomer(@RequestParam @NonNull Long id) {
        final Customer customer = customerService.findById(id).orElseThrow(() -> new NullPointerException("%s Given customer not exist in DB!".formatted(id)));
        return ok(makeResponse(customer, Status.FOUND.getStatus(), Status.FOUND.getMessage()));
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> updateCustomer(@RequestBody @Valid Customer customer) {
        return ok(makeResponse(customerService.updateById(customer), Status.UPDATE.getStatus(), Status.UPDATE.getMessage()));
    }

    @DeleteMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> deleteCustomer(@RequestParam @NonNull Long id) {
        customerService.deleteById(id);
        return ok(makeResponse(null, Status.DELETE.getStatus(), Status.DELETE.getMessage()));
    }

    @PostMapping(path = "/test", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> send(@RequestBody MailSenderRequestBuilder builder) {
        builder.setTemplate(OTP);
        return ok(makeResponse(mailSenderService.sendMail(builder), Status.SUCCESS.getStatus(), Status.SUCCESS.getMessage()));
    }

    @PostMapping(path = "/build", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buildUrl(@RequestBody @NonNull @Valid UrlRequestBuilder request) {
        return ok(makeResponse(Map.of("url",commonUtil.urlBuilder.apply(request)), Status.SUCCESS.getStatus(), Status.SUCCESS.getMessage()));
//        System.out.println(commonUtil.);
//        return ok("success");
    }
}
