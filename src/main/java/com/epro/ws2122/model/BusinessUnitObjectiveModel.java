package com.epro.ws2122.model;

import com.epro.ws2122.domain.BusinessUnitObjective;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Getter
@Relation(collectionRelation = "businessUnitObjectives", value = "businessUnitObjective")
public class BusinessUnitObjectiveModel extends RepresentationModel<BusinessUnitObjectiveModel> {

    private final String name;
    @JsonFormat(shape=JsonFormat.Shape.NUMBER)
    private final LocalDate startDate;

    public BusinessUnitObjectiveModel(BusinessUnitObjective buo) {
        this.name = buo.getName();
        this.startDate = buo.getStartDate();
    }
}
