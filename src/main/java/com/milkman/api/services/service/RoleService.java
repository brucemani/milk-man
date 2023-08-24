package com.milkman.api.services.service;

import com.milkman.api.model.Role;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * @Author: kchid
 * @Project: pom.xml
 * @Date: 8/24/2023
 * @Time: 10:39 PM
 */
public interface RoleService extends CommonService<Role,Long>{
    Optional<Role> findRoleByCustomerId(@NonNull Long customerId);
}
