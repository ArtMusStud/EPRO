package com.epro.ws2122.model;

import com.epro.ws2122.controller.CompanyKeyResultController;
import com.epro.ws2122.domain.CompanyKeyResult;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
public class CompanyKeyResultSubresourceModel extends RepresentationModel<CompanyKeyResultSubresourceModel> {
    private final String name;
    private final double overall;

    public CompanyKeyResultSubresourceModel(long companyObjectiveID, CompanyKeyResult companyKeyResult) {
        name = companyKeyResult.getName();
        overall = companyKeyResult.getCurrent() / companyKeyResult.getGoal();
        this.add(linkTo((methodOn(CompanyKeyResultController.class)
                .cokrById(companyObjectiveID, companyKeyResult.getId()))).withSelfRel());
    }
}

