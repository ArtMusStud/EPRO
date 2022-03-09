package com.epro.ws2122.repository;

import com.epro.ws2122.domain.userRoles.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String name);
}
