package com.epro.ws2122.assembler;

import com.epro.ws2122.controller.CompanyKeyResultController;
import com.epro.ws2122.controller.DashboardController;
import com.epro.ws2122.domain.CompanyKeyResult;
import com.epro.ws2122.model.CompanyKeyResultModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CompanyKeyResultAssembler
        extends RepresentationModelAssemblerSupport<CompanyKeyResult, CompanyKeyResultModel> {

    public CompanyKeyResultAssembler() {
        super(CompanyKeyResultController.class, CompanyKeyResultModel.class);
    }

    @Override
    protected CompanyKeyResultModel instantiateModel(CompanyKeyResult companyKeyResult) {
        return new CompanyKeyResultModel(companyKeyResult);
    }

    /* Todo: implement access of co id through corresponding cokr object and delete placeholder argument '0L'
     */
    @Override
    public CompanyKeyResultModel toModel(CompanyKeyResult companyKeyResult) {
        var cokrModel = createModelWithId(companyKeyResult.getId(), companyKeyResult, 0L);
        cokrModel.add(
                linkTo(methodOn(DashboardController.class).dashboard())
                        .withRel("dashboard"));
        return cokrModel;
    }
}
