package com.milkman.api.services.serviceImpl;

import com.milkman.api.model.Customer;
import com.milkman.api.repository.CustomerRepository;
import com.milkman.api.services.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 7/30/2023
 * @Time: 12:41 PM
 */
@Service
@Slf4j
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Override
    public Customer save(Customer obj) {
        if (this.isEmailAlreadyRegister(obj.getCustomerEmail())) {
            log.error("%s given email already register.".formatted(obj.getCustomerEmail()));
            throw new RuntimeException("%s given email already register.".formatted(obj.getCustomerEmail()));
        }
        return repository.save(obj);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Customer> findAll() {
        return repository.findAll();
    }

    @Override
    public Customer updateById(Customer obj) {
        requireNonNull(obj, "Customer object shouldn't null");
        requireNonNull(obj.getCustomerId(), "Customer ID shouldn't null");
        this.findById(obj.getCustomerId()).orElseThrow(() -> new NullPointerException("%s Given customer not exist in DB!".formatted(obj.getCustomerId())));
        return repository.save(obj);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
        log.info("%s Given customer has been deleted.");
    }

    @Override
    public Boolean isEmailAlreadyRegister(String email) {
        return repository.countCustomersByCustomerEmail(email) > 0;
    }
}
