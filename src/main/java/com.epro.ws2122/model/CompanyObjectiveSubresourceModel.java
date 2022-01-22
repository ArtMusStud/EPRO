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
@Getter
@Relation(collectionRelation = "companyObjectives", value = "companyObjective")
public class CompanyObjectiveSubresourceModel extends RepresentationModel<CompanyKeyResultSubresourceModel> {

    private final String name;
    //private final double overall;

    public CompanyObjectiveSubresourceModel(CompanyObjective companyObjective) {
        this.name = companyObjective.getName();
        this.add(linkTo((methodOn(CompanyObjectiveController.class)
            .findOne(companyObjective.getId()))).withSelfRel());
    }
}
