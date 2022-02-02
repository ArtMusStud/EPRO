package com.epro.ws2122.model;

import com.epro.ws2122.domain.CompanyObjective;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

/**
 * DTO for class {@link com.epro.ws2122.domain.CompanyObjective CompanyObjective} that is directly requested by a client per HTTP GET.
 * <p>
 * The properties of {@link CompanyObjectiveModel} are a subset of the properties of {@link com.epro.ws2122.domain.CompanyKeyResult CompanyKeyResult}.
 * <p>
 * When a {@link com.epro.ws2122.domain.CompanyObjective CompanyObjective} resource is not requested directly but still needs
 * to be embedded as a related resource in the response body, {@link CompanyObjectiveSubresourceModel} is used.
 */
@Getter
@Relation(collectionRelation = "companyObjectives", value = "companyObjective")
public class CompanyObjectiveModel extends RepresentationModel<CompanyObjectiveModel> {

    private final String name;
    @JsonFormat(shape=JsonFormat.Shape.NUMBER)
    private final LocalDate startDate;
//    private double overall;

    public CompanyObjectiveModel(CompanyObjective companyObjective) {
        this.name = companyObjective.getName();
        this.startDate = companyObjective.getStartDate();
//        this.overall = companyObjective.getOverall();
    }
}
