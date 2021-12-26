package com.epro.ws2122.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
public class CompanyObjectiveModel extends RepresentationModel<CompanyObjectiveModel> {
    private Long id;
    private String name;
    private Date createdAt;
}
