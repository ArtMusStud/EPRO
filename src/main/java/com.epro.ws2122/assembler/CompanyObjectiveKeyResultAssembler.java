package com.epro.ws2122.assembler;

import com.epro.ws2122.controller.CompanyObjectiveKeyResultController;
import com.epro.ws2122.domain.CompanyObjectiveKeyResult;
import com.epro.ws2122.model.CompanyObjectiveKeyResultModel;
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

    /* Todo: implement access of co id through corresponding cokr object and delete placeholder argument '0L'
     */
    @Override
    public CompanyObjectiveKeyResultModel toModel(CompanyObjectiveKeyResult companyObjectiveKeyResult) {
        return createModelWithId(companyObjectiveKeyResult.getId(), companyObjectiveKeyResult, 0L);
    }
}
