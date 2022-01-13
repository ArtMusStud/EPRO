package com.epro.ws2122.assembler;

import com.epro.ws2122.controller.CompanyObjectiveController;
import com.epro.ws2122.controller.DashboardController;
import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.model.CompanyObjectiveModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CompanyObjectiveAssembler
        extends RepresentationModelAssemblerSupport<CompanyObjective, CompanyObjectiveModel> {

    public CompanyObjectiveAssembler() {
        super(CompanyObjectiveController.class, CompanyObjectiveModel.class);
    }

    @Override
    protected CompanyObjectiveModel instantiateModel(CompanyObjective companyObjective) {
        return new CompanyObjectiveModel(companyObjective);
    }

    @Override
    public CompanyObjectiveModel toModel(CompanyObjective companyObjective) {
        var coModel = createModelWithId(companyObjective.getId(), companyObjective);
        coModel.add(
                linkTo(methodOn(CompanyObjectiveController.class).companyObjectives()).withRel("Company Objectives"),
                linkTo(methodOn(DashboardController.class).dashboard()).withRel("dashboard")
                        .andAffordance(afford(methodOn(CompanyObjectiveController.class).updateCompanyObjective(companyObjective.getId())))
                        .andAffordance(afford(methodOn(CompanyObjectiveController.class).deleteCompanyObjective(companyObjective.getId()))));
        return coModel;
    }
}
