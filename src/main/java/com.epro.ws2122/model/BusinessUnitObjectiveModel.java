package com.epro.ws2122.model;

import com.epro.ws2122.domain.BusinessUnitObjective;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Relation(collectionRelation = "businessUnitObjectives", value = "businessUnitObjective")
public class BusinessUnitObjectiveModel extends RepresentationModel<BusinessUnitObjectiveModel> {

    private final String name;

    public BusinessUnitObjectiveModel(BusinessUnitObjective buo) {
        this.name = buo.getName();
    }
}
