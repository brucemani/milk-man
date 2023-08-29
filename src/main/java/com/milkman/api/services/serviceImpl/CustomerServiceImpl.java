package com.milkman.api.services.serviceImpl;

import com.milkman.api.model.Customer;
import com.milkman.api.model.Role;
import com.milkman.api.repository.CustomerRepository;
import com.milkman.api.services.service.CustomerService;
import com.milkman.api.services.service.RoleService;
import com.milkman.api.util.common.CommonUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.milkman.api.util.enums.Privilege.ALL;
import static java.util.Objects.isNull;
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
@Transactional
public class CustomerServiceImpl extends CommonUtil implements CustomerService {

    private final CustomerRepository repository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    private void ifNotCreateRole(@NonNull Customer customer) {
        final Role role = this.roleService.findRoleByCustomerId(customer.getCustomerId()).orElseGet(Role::new);
        if (isNull(role.getRoleId())) {
            final Role buildRole = Role.builder().roleList(Set.of("USER")).privilegeList(List.of(ALL)).customerId(customer.getCustomerId()).build();
            final Role save = this.roleService.save(buildRole);
            customer.setRoleId(save.getRoleId());
        }
    }

    @Override
    public Customer save(Customer obj) {
        if (this.isEmailAlreadyRegister(obj.getCustomerEmail())) {
            log.error("%s given email already register.".formatted(obj.getCustomerEmail()));
            throw new RuntimeException("%s given email already register.".formatted(obj.getCustomerEmail()));
        }
        obj.setCustomerPassword(this.passwordEncoder.encode(obj.getCustomerPassword()));
        final Customer customer = this.repository.save(obj);
        this.ifNotCreateRole(customer);
        return customer;
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
    public Optional<Customer> findCustomerByEmail(@NonNull String email) {
        return this.repository.findByCustomerEmail(email);
    }

    @Override
    public Boolean isEmailAlreadyRegister(String email) {
        return repository.countCustomersByCustomerEmail(email) > 0;
    }

    @Override
    public void updatePassword(@NonNull Long userId, @NonNull String newPassword) {
        final Customer customer = this.findById(userId).orElseThrow(() -> new NullPointerException("User not exist!"));
        customer.setCustomerPassword(passwordEncoder.encode(newPassword));
        this.updateById(customer);
        log.info("Password updated.");
    }

    @Override
    public String readUserProfile(@NonNull Long customerId) {
        final Customer findCustomer = this.findById(customerId).orElseThrow(() -> new NullPointerException("Customer doesn't exist!"));
        if (findCustomer.getProfile() != null) {
            return imageToBase64.apply(findCustomer.getProfile());
        }
        return null;
    }

    @Override
    public void updateUserProfile(@NonNull Customer customer) {
        requireNonNull(customer.getCustomerId(), "Customer id shouldn't null");
        requireNonNull(customer.getProfile(), "Profile image shouldn't null");
        this.repository.updateCustomerProfileByCustomerId(customer.getProfile(),customer.getCustomerId());
    }
}
