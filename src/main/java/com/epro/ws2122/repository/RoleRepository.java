package com.epro.ws2122.repository;

import com.epro.ws2122.domain.userRoles.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
