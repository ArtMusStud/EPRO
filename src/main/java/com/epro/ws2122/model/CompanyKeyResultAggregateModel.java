package com.epro.ws2122.model;

import com.epro.ws2122.controller.BusinessUnitKeyResultController;
import com.epro.ws2122.controller.BusinessUnitObjectiveController;
import com.epro.ws2122.domain.CompanyKeyResult;
import com.epro.ws2122.repository.BusinessUnitKeyResultRepository;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Relation(collectionRelation = "companyKeyResults", value = "companyKeyResult")
public class CompanyKeyResultAggregateModel extends RepresentationModel<CompanyKeyResultAggregateModel> {

    private final String name;
    private final double current;
    private final double goal;
    private final double confidence;
    private final List<BusinessUnitKeyResultModel> businessUnitKeyResults;

    public CompanyKeyResultAggregateModel(CompanyKeyResult ckr, BusinessUnitKeyResultRepository bukrRepository) {
        name = ckr.getName();
        current = ckr.getCurrent();
        goal = ckr.getGoal();
        confidence = ckr.getConfidence();

        businessUnitKeyResults = bukrRepository.findAllByCompanyKeyResult(ckr).stream()
                .map(bukr -> new BusinessUnitKeyResultModel(bukr)
                        .add(linkTo((methodOn(BusinessUnitKeyResultController.class)
                                .findOne(bukr.getBusinessUnitObjective().getId(), bukr.getId()))).withSelfRel())
                        .add(linkTo(methodOn(BusinessUnitObjectiveController.class)
                                .findOne(bukr.getBusinessUnitObjective().getId())).withRel("businessUnitObjective")))
                .collect(Collectors.toList());
    }
}
