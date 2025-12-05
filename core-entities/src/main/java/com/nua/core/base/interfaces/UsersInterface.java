package com.nua.core.base.interfaces;

import com.nua.core.base.entities.NUAUserBase;

import java.util.List;
import java.util.Optional;

public interface UsersInterface<T extends NUAUserBase> {

    List<T> findAll();
    Optional<T> findByUser(String username);
    void save(T user);
    T findById(String id);
    void update(T user);
    void delete(T user);

}