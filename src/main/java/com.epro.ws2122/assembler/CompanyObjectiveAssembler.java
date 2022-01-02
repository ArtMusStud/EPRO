package com.epro.ws2122.assembler;

import com.epro.ws2122.controller.CompanyObjectiveController;
import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.model.CompanyObjectiveModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

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
        return createModelWithId(companyObjective.getId(), companyObjective);
    }
}
