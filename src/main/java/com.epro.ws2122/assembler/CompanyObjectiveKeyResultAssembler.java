package com.epro.ws2122.assembler;

import com.epro.ws2122.controller.CompanyObjectiveController;
import com.epro.ws2122.controller.CompanyObjectiveKeyResultController;
import com.epro.ws2122.controller.DashboardController;
import com.epro.ws2122.domain.CompanyObjectiveKeyResult;
import com.epro.ws2122.model.CompanyObjectiveKeyResultModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

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

    /* Todo:
        - implement access of co id through corresponding cokr object and delete placeholderID
     */
    @Override
    public CompanyObjectiveKeyResultModel toModel(CompanyObjectiveKeyResult cokr) {
        var placeHolderID = 0L;
        var cokrModel = instantiateModel(cokr);
        cokrModel.add(
                linkTo(methodOn(CompanyObjectiveKeyResultController.class).cokrById(placeHolderID, cokr.getId())).withSelfRel()
                        .andAffordance(afford(methodOn(CompanyObjectiveKeyResultController.class).putCokr(placeHolderID, cokr.getId())))
                        .andAffordance(afford(methodOn(CompanyObjectiveKeyResultController.class).patchCokr(placeHolderID, cokr.getId())))
                        .andAffordance(afford(methodOn(CompanyObjectiveKeyResultController.class).deleteCokr(placeHolderID, cokr.getId()))));
        return cokrModel;
    }
}
