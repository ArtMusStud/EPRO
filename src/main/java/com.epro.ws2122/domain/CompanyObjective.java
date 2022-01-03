package com.epro.ws2122.domain;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity
public class CompanyObjective {

    @Id
    private Long id;
    private String name;
    private Date createdAt = new Date();
    private double overall;
}
