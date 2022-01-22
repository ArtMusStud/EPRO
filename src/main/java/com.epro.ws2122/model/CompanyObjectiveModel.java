package com.epro.ws2122.model;

import com.epro.ws2122.domain.CompanyObjective;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Getter
@Relation(collectionRelation = "companyObjectives", value = "companyObjective")
public class CompanyObjectiveModel extends RepresentationModel<CompanyObjectiveModel> {

    private final String name;
    @JsonFormat(shape=JsonFormat.Shape.NUMBER)
    private final LocalDate startDate;
//    private double overall;

    public CompanyObjectiveModel(CompanyObjective companyObjective) {
        this.name = companyObjective.getName();
        this.startDate = companyObjective.getStartDate();
//        this.overall = companyObjective.getOverall();
    }
}
