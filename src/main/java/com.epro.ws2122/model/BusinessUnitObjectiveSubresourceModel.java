package com.epro.ws2122.model;

import com.epro.ws2122.controller.BusinessUnitObjectiveController;
import com.epro.ws2122.domain.BusinessUnitObjective;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Collection;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Relation(collectionRelation = "businessUnitObjectives", value = "businessUnitObjective")
public class BusinessUnitObjectiveSubresourceModel extends RepresentationModel<BusinessUnitObjectiveSubresourceModel> {

    private final String name;

    public BusinessUnitObjectiveSubresourceModel(BusinessUnitObjective businessUnitObjective) {
        this.name = businessUnitObjective.getName();
        this.add(linkTo((methodOn(BusinessUnitObjectiveController.class)
                .findOne(businessUnitObjective.getId()))).withSelfRel());
    }
}
