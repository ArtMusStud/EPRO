package com.epro.ws2122.model;

import com.epro.ws2122.domain.CompanyObjective;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyObjectiveModel extends RepresentationModel<CompanyObjectiveModel> {

    private String name;
    @JsonFormat(shape=JsonFormat.Shape.NUMBER)
    private LocalDate startDate;
//    private double overall;

    public CompanyObjectiveModel(CompanyObjective companyObjective) {
        this.name = companyObjective.getName();
        this.startDate = companyObjective.getStartDate();
//        this.overall = companyObjective.getOverall();
    }
}
