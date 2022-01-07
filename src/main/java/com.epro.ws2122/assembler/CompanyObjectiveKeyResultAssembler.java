package com.epro.ws2122.assembler;

import com.epro.ws2122.controller.CompanyObjectiveController;
import com.epro.ws2122.controller.CompanyObjectiveKeyResultController;
import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.domain.CompanyObjectiveKeyResult;
import com.epro.ws2122.model.CompanyObjectiveKeyResultModel;
import com.epro.ws2122.model.CompanyObjectiveModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CompanyObjectiveKeyResultAssembler
        extends RepresentationModelAssemblerSupport<CompanyObjectiveKeyResult, CompanyObjectiveKeyResultModel> {

    public CompanyObjectiveKeyResultAssembler() {
        super(CompanyObjectiveKeyResultController.class, CompanyObjectiveKeyResultModel.class);
    }

    @Override
    protected CompanyObjectiveKeyResultModel instantiateModel(CompanyObjectiveKeyResult companyObjectiveKeyResult) {
        return new CompanyObjectiveKeyResultModel(companyObjectiveKeyResult);
    }

    @Override
    public CompanyObjectiveKeyResultModel toModel(CompanyObjectiveKeyResult companyObjectiveKeyResult) {
        return createModelWithId(companyObjectiveKeyResult.getId(), companyObjectiveKeyResult);
    }
}
