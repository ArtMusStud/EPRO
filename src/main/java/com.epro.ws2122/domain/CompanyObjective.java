package com.epro.ws2122.domain;

import lombok.Data;

import java.util.Date;

@Data
public class CompanyObjective {

    private Long id;
    private String name;
    private Date createdAt = new Date();
}
