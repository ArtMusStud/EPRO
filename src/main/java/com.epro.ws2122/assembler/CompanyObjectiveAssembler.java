package com.epro.ws2122.assembler;

import com.epro.ws2122.controller.CompanyObjectiveController;
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
        var coModel = instantiateModel(companyObjective);
        coModel.add(
                linkTo(methodOn(CompanyObjectiveController.class).companyObjectiveById(companyObjective.getId())).withSelfRel()
                        .andAffordance(afford(methodOn(CompanyObjectiveController.class).putCompanyObjective(companyObjective.getId())))
                        .andAffordance(afford(methodOn(CompanyObjectiveController.class).patchCompanyObjective(companyObjective.getId())))
                        .andAffordance(afford(methodOn(CompanyObjectiveController.class).deleteCompanyObjective(companyObjective.getId()))));
        return coModel;
    }
}
