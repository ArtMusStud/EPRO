package com.epro.ws2122.model;

import com.epro.ws2122.domain.BusinessUnitKeyResult;
import com.epro.ws2122.domain.BusinessUnitObjective;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Relation(collectionRelation = "businessUnitKeyResults", value = "businessUnitKeyResult")
public class BusinessUnitKeyResultModel extends RepresentationModel<BusinessUnitKeyResultModel> {

    private final String name;
    private final double current;
    private final double goal;
    private final double confidence;

    public BusinessUnitKeyResultModel(BusinessUnitKeyResult bukr) {
        this.name = bukr.getName();
        this.current = bukr.getCurrent();
        this.goal = bukr.getGoal();
        this.confidence = bukr.getConfidence();
    }
}
