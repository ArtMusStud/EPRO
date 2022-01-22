package com.epro.ws2122.assembler;

import com.epro.ws2122.controller.CompanyKeyResultController;
import com.epro.ws2122.domain.CompanyKeyResult;
import com.epro.ws2122.model.CompanyKeyResultModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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

    /* Todo:
        - implement access of co id through corresponding cokr object and delete placeholderID
     */
    @Override
    public CompanyKeyResultModel toModel(CompanyKeyResult companyKeyResult) {
        var placeHolderID = 0L;
        var companyKeyResultModel = instantiateModel(companyKeyResult);
        companyKeyResultModel.add(
                linkTo(methodOn(CompanyKeyResultController.class).findOne(placeHolderID, companyKeyResult.getId())).withSelfRel()
                        .andAffordance(afford(methodOn(CompanyKeyResultController.class).replace(placeHolderID, companyKeyResult.getId())))
                        .andAffordance(afford(methodOn(CompanyKeyResultController.class).update(placeHolderID, companyKeyResult.getId())))
                        .andAffordance(afford(methodOn(CompanyKeyResultController.class).delete(placeHolderID, companyKeyResult.getId()))));
        return companyKeyResultModel;
    }
}
