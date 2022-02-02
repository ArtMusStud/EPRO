package com.epro.ws2122.model;

import com.epro.ws2122.domain.CompanyKeyResult;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * DTO for class {@link com.epro.ws2122.domain.CompanyKeyResult CompanyKeyResult} that is directly requested by a client per HTTP GET.
 * <p>
 * The properties of {@link CompanyKeyResultModel} are a subset of the properties of {@link com.epro.ws2122.domain.CompanyKeyResult CompanyKeyResult}.
 * <p>
 * When a {@link com.epro.ws2122.domain.CompanyKeyResult CompanyKeyResult} resource is not requested directly but still needs
 * to be embedded as a related resource in the response body, {@link CompanyKeyResultSubresourceModel} is used.
 */
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
