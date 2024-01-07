package com.milkman.api.controller;

import com.milkman.api.dto.MailSenderRequestBuilder;
import com.milkman.api.dto.ResponseBuilder;
import com.milkman.api.dto.UrlRequestBuilder;
import com.milkman.api.mail.MailSenderService;
import com.milkman.api.model.Customer;
import com.milkman.api.services.service.CustomerService;
import com.milkman.api.util.common.CommonUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static com.milkman.api.util.enums.MailTemplate.OTP;
import static com.milkman.api.util.enums.ResponseHandler.makeResponse;
import static com.milkman.api.util.enums.Status.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
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
@Slf4j
public class CustomerController extends CommonUtil{

    private final CustomerService customerService;
    private final MailSenderService mailSenderService;


    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> create(@RequestBody @Valid final Customer customer) {
        return ok(makeResponse(customerService.save(customer), CREATED.getStatus(), CREATED.getMessage()));
    }

    @GetMapping(path = "/all", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> findAllCustomer() {
        return ok(makeResponse(customerService.findAll(), FOUND.getStatus(), FOUND.getMessage()));
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> findCustomer(@RequestParam @NonNull final Long id) {
        final Customer customer = customerService.findById(id).orElseThrow(() -> new NullPointerException("%s Given customer not exist in DB!".formatted(id)));
        return ok(makeResponse(customer, FOUND.getStatus(), FOUND.getMessage()));
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> updateCustomer(@RequestBody @Valid final Customer customer) {
        return ok(makeResponse(customerService.updateById(customer), UPDATE.getStatus(), UPDATE.getMessage()));
    }

    @PutMapping(path = "/profile/{id}", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> updateProfile(@PathVariable("id") final Long id, @RequestParam("file") @NonNull final MultipartFile profile) {
        try {
            customerService.updateUserProfile(Customer.builder().customerId(id).profile(profile.getBytes()).build());
            return ok(makeResponse(null, UPDATE.getStatus(), UPDATE.getMessage()));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    @DeleteMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> deleteCustomer(@RequestParam @NonNull final Long id) {
        customerService.deleteById(id);
        return ok(makeResponse(null, DELETE.getStatus(), DELETE.getMessage()));
    }

    @GetMapping(path = "/profile/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> readProfile(@PathVariable("id") final Long id) {
        return ok(makeResponse(Map.of("profile",this.customerService.readUserProfile(id)), FOUND.getStatus(), FOUND.getMessage()));
    }

    @PostMapping(path = "/test", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBuilder> send(@RequestBody final MailSenderRequestBuilder builder) {
        builder.setTemplate(OTP);
        return ok(makeResponse(mailSenderService.sendMail(builder), FOUND.getStatus(), FOUND.getMessage()));
    }

    @PostMapping(path = "/build", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buildUrl(@RequestBody @NonNull @Valid final UrlRequestBuilder request) {
        return ok(makeResponse(Map.of("url", urlBuilder.apply(request)), FOUND.getStatus(), FOUND.getMessage()));
//        System.out.println(commonUtil.);
//        return ok("success");
    }
}
