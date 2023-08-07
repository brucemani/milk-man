package com.milkman.api.services.service;

import java.util.List;
import java.util.Optional;

/**
 * @Author: kchid
 * @Project: milky-man
 * @Date: 7/30/2023
 * @Time: 12:43 PM
 */
public interface CommonService<T,ID> {
    T save(T obj);
    Optional<T> findById(ID id);
    List<T> findAll();
    T updateById(T obj);
    void deleteById(ID id);
}
