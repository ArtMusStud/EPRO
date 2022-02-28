package com.epro.ws2122.model;

import com.epro.ws2122.controller.CompanyKeyResultController;
import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.repository.BusinessUnitKeyResultRepository;
import com.epro.ws2122.repository.CompanyKeyResultRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Relation(collectionRelation = "companyObjectives", value = "companyObjective")
public class CompanyObjectiveAggregateModel extends RepresentationModel<CompanyObjectiveAggregateModel> {

        private final String name;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private final LocalDate startDate;
        private List<CompanyKeyResultAggregateModel> companyKeyResults;

        public CompanyObjectiveAggregateModel(CompanyObjective co,
                                              CompanyKeyResultRepository ckrRepository,
                                              BusinessUnitKeyResultRepository bukrRepository) {
            this.name = co.getName();
            this.startDate = co.getStartDate();

            companyKeyResults = ckrRepository.findAllByCompanyObjective(co).stream()
                    .map(ckr -> new CompanyKeyResultAggregateModel(ckr, bukrRepository)
                        .add(linkTo((methodOn(CompanyKeyResultController.class)
                            .findOne(co.getId(), ckr.getId()))).withSelfRel()))
                    .collect(Collectors.toList());
        }
}
