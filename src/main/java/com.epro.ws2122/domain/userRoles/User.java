package com.epro.ws2122.domain.userRoles;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
public class User {

    @Id
    // todo generated value
    private long id;

    private String username;
    private String password;

    @ManyToOne
    private Role role;
}
