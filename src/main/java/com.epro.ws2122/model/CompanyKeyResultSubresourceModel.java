package com.epro.ws2122.model;

import com.epro.ws2122.controller.CompanyKeyResultController;
import com.epro.ws2122.domain.CompanyKeyResult;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * DTO for class {@link com.epro.ws2122.domain.CompanyKeyResult CompanyKeyResult} that is embedded as a related resource
 * in the response body of HTTP GET requests.
 * <p>
 * The properties of {@link CompanyKeyResultSubresourceModel} are a subset of the properties of
 * {@link com.epro.ws2122.domain.CompanyKeyResult CompanyKeyResult}. As a sub resource this class
 * contains even less properties than {@link CompanyKeyResultModel}.
 * <p>
 * Contrary to {@link CompanyKeyResultModel} a self link is already added through its {@link #CompanyKeyResultSubresourceModel constructor}.
 */
@Getter
@Relation(collectionRelation = "companyKeyResults", value = "companyKeyResult")
public class CompanyKeyResultSubresourceModel extends RepresentationModel<CompanyKeyResultSubresourceModel> {

    private final String name;
    private final double overall;

    public CompanyKeyResultSubresourceModel(long companyObjectiveID, CompanyKeyResult companyKeyResult) {
        name = companyKeyResult.getName();
        overall = companyKeyResult.getCurrent() / companyKeyResult.getGoal();
        this.add(linkTo((methodOn(CompanyKeyResultController.class)
                .findOne(companyObjectiveID, companyKeyResult.getId()))).withSelfRel());
    }
}

