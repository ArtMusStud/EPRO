package com.epro.ws2122.model;

import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.domain.CompanyObjectiveKeyResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

public class CompanyObjectiveKeyResultModel extends RepresentationModel<CompanyObjectiveModel> {

    public CompanyObjectiveKeyResultModel(CompanyObjectiveKeyResult companyObjectiveKeyResult) {
    }
}
