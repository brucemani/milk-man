package com.milkman.api.services.serviceImpl;

import com.milkman.api.model.Role;
import com.milkman.api.repository.RoleRepository;
import com.milkman.api.services.service.RoleService;
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
 * @Date: 8/24/2023
 * @Time: 11:02 PM
 */
@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role save(@NonNull Role role) {
        return this.roleRepository.save(role);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return this.roleRepository.findById(id);
    }

    @Override
    public List<Role> findAll() {
        return this.roleRepository.findAll();
    }

    @Override
    public Role updateById(Role obj) {
        return this.roleRepository.save(obj);
    }

    @Override
    public void deleteById(Long roleId) {
        this.roleRepository.findById(roleId);
    }

    @Override
    public Optional<Role> findRoleByCustomerId(@NonNull Long customerId) {
        return this.roleRepository.findByCustomerId(customerId);
    }
}
