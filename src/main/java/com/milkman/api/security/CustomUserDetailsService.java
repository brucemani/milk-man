package com.milkman.api.security;


import com.milkman.api.model.Customer;
import com.milkman.api.model.Role;
import com.milkman.api.services.service.CustomerService;
import com.milkman.api.services.service.RoleService;
import com.milkman.api.util.enums.Privilege;
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
//        if (customer.getIsAccountActive() == null || !customer.getIsAccountActive()) {
//            log.error("Account is not activated!");
//            throw new AccessDeniedException("Account is not activated!");
//        }
        final Role role = this.findRoleByCustomerId(customer.getCustomerId());
        final List<SimpleGrantedAuthority> authorities = role.getRoleList()
                .stream()
                .filter(Objects::nonNull)
                .map(m -> new SimpleGrantedAuthority("ROLE_".concat(m)))
                .collect(toList());
        final List<SimpleGrantedAuthority> authorityList = role.getPrivilegeList()
                .stream()
                .filter(Objects::nonNull)
                .map(Privilege::name)
                .map(SimpleGrantedAuthority::new)
                .toList();
        authorities.addAll(authorityList);
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
