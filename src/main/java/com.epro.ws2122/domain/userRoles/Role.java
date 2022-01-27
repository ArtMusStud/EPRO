package com.epro.ws2122.domain.userRoles;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Role {

    @Id
    // todo generated value
    private long id;

    private String name;
}
