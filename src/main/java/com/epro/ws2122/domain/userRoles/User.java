package com.epro.ws2122.domain.userRoles;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "`user`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password;

    @ManyToOne
    private Role role;
}
