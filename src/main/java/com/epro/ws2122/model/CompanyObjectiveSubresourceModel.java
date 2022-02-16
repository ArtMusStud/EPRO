package com.epro.ws2122.model;

import com.epro.ws2122.controller.CompanyObjectiveController;
import com.epro.ws2122.domain.CompanyObjective;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/*
    Todo: implement overall
 */
/**
 * DTO for class {@link com.epro.ws2122.domain.CompanyObjective CompanyObjective} that is embedded as a related resource
 * in the response body of HTTP GET requests.
 * <p>
 * The properties of {@link CompanyObjectiveSubresourceModel} are a subset of the properties of
 * {@link com.epro.ws2122.domain.CompanyObjective CompanyObjective}. As a sub resource this class
 * contains even less properties than {@link CompanyObjectiveModel}.
 * <p>
 * Contrary to {@link CompanyObjectiveModel} a self link is already added through its {@link #CompanyObjectiveSubresourceModel constructor}.
 */
@Getter
@Relation(collectionRelation = "companyObjectives", value = "companyObjective")
public class CompanyObjectiveSubresourceModel extends RepresentationModel<CompanyObjectiveSubresourceModel> {

    private final String name;
    //private final double overall;

    public CompanyObjectiveSubresourceModel(CompanyObjective co) {
        this.name = co.getName();
        this.add(linkTo((methodOn(CompanyObjectiveController.class)
            .findOne(co.getId()))).withSelfRel());
    }
}
