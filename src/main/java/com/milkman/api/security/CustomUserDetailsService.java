package com.milkman.api.security;


import com.milkman.api.model.Customer;
import com.milkman.api.model.Role;
import com.milkman.api.repository.CustomerRepository;
import com.milkman.api.repository.RoleRepository;
import com.milkman.api.services.service.CustomerService;
import com.milkman.api.services.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Component
@AllArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerService customerService;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Customer customer = this.findCustomerByEmail(email);
        final Role role = this.findRoleByCustomerId(customer.getCustomerId());
        final List<SimpleGrantedAuthority> authorities = role.getRoleList()
                .stream()
                .filter(Objects::nonNull)
                .map(m -> new SimpleGrantedAuthority("ROLE_".concat(m)))
                .collect(toList());
        return new User(customer.getCustomerEmail(), customer.getCustomerPassword(), authorities);
    }

    private Customer findCustomerByEmail(final String email) {
        return this.customerService
                .findCustomerByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not exist!"));
    }

    private Role findRoleByCustomerId(final Long customerId) {
        return this.roleService
                .findRoleByCustomerId(customerId)
                .orElseGet(Role::new);
    }
}
