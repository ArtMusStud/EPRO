package com.epro.ws2122.controller;

import com.epro.ws2122.model.CompanyObjectiveAggregateModel;
import com.epro.ws2122.repository.BusinessUnitKeyResultRepository;
import com.epro.ws2122.repository.BusinessUnitObjectiveRepository;
import com.epro.ws2122.repository.CompanyKeyResultRepository;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final CompanyObjectiveRepository coRepository;
    private final CompanyKeyResultRepository ckrRepository;
    private final BusinessUnitObjectiveRepository buoRepository;
    private final BusinessUnitKeyResultRepository bukrRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {

        var coModels = StreamSupport
                .stream(coRepository.findAll().spliterator(), false)
                .map(co -> new CompanyObjectiveAggregateModel(co, ckrRepository, bukrRepository)
                    .add(linkTo((methodOn(CompanyObjectiveController.class)
                        .findOne(co.getId()))).withSelfRel()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(CollectionModel.of(coModels), HttpStatus.OK);
    }
}
