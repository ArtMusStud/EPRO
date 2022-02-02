package com.epro.ws2122.model;

import com.epro.ws2122.controller.BusinessUnitKeyResultController;
import com.epro.ws2122.domain.BusinessUnitKeyResult;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Relation(collectionRelation = "businessUnitKeyResults", value = "businessUnitKeyResult")
public class BusinessUnitKeyResultSubresourceModel extends RepresentationModel<BusinessUnitKeyResultSubresourceModel> {

    private final String name;

    public BusinessUnitKeyResultSubresourceModel(long businessUnitObjectiveID, BusinessUnitKeyResult businessUnitKeyResult) {
        this.name = businessUnitKeyResult.getName();
        this.add(linkTo((methodOn(BusinessUnitKeyResultController.class)
                .findOne(businessUnitObjectiveID, businessUnitKeyResult.getId()))).withSelfRel());
    }
}