package com.epro.ws2122.assembler;

import com.epro.ws2122.controller.CompanyObjectiveKeyResultController;
import com.epro.ws2122.controller.DashboardController;
import com.epro.ws2122.domain.CompanyObjectiveKeyResult;
import com.epro.ws2122.model.CompanyObjectiveKeyResultModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        var cokrModel = createModelWithId(companyObjectiveKeyResult.getId(), companyObjectiveKeyResult, 0L);
        cokrModel.add(
                linkTo(methodOn(DashboardController.class).dashboard())
                        .withRel("dashboard"));
        return cokrModel;
    }
}
