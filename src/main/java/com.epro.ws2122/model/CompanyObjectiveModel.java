package com.epro.ws2122.model;

import com.epro.ws2122.domain.CompanyObjective;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
public class CompanyObjectiveModel extends RepresentationModel<CompanyObjectiveModel> {

    private String name;
    private Date createdAt;
    private double overall;

    public CompanyObjectiveModel(CompanyObjective companyObjective) {
        this.name = companyObjective.getName();
        this.createdAt = companyObjective.getCreatedAt();
        this.overall = companyObjective.getOverall();
    }
}
