package com.epro.ws2122.model;

import com.epro.ws2122.domain.CompanyKeyResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Relation(collectionRelation = "companyKeyResults", value = "companyKeyResult")
public class CompanyKeyResultModel extends RepresentationModel<CompanyKeyResultModel> {

    private final String name;
    private final double current;
    private final double goal;
    private final double confidence;

    public CompanyKeyResultModel(CompanyKeyResult companyObjectiveKeyResult) {
        name = companyObjectiveKeyResult.getName();
        current = companyObjectiveKeyResult.getCurrent();
        goal = companyObjectiveKeyResult.getGoal();
        confidence = companyObjectiveKeyResult.getConfidence();
    }
}
