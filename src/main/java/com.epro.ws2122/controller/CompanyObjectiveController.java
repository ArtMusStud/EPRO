package com.epro.ws2122.controller;

import com.epro.ws2122.assembler.CompanyObjectiveAssembler;
import com.epro.ws2122.model.CompanyObjectiveModel;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/company-objectives")
public class CompanyObjectiveController {

    private final CompanyObjectiveRepository repository;
    private final CompanyObjectiveAssembler assembler;

    public CompanyObjectiveController(CompanyObjectiveRepository repository, CompanyObjectiveAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    /* ToDo add remaining resources and links to response:
        collection resource of all CO KR corresponding to requested CO
        link to complete aggregation at level of requested CO
        link to dashboard
     */
    @GetMapping( "/{id}")
    public ResponseEntity<RepresentationModel<CompanyObjectiveModel>> companyObjectiveById(@PathVariable("id") long id) {
        var companyObjective =  repository.findById(id);
        if (companyObjective.isPresent()) {
            var companyObjectiveResource = assembler.toModel(companyObjective.get());
            companyObjectiveResource.add(
                    linkTo(methodOn(CompanyObjectiveController.class).companyObjectives())
                            .withRel("Company Objectives"));
            return new ResponseEntity<>(companyObjectiveResource, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /* ToDo add collection resource of all CO KR corresponding to each requested CO
     */
    @GetMapping
    public ResponseEntity<CollectionModel<CompanyObjectiveModel>> companyObjectives() {
        var companyObjectives = repository.findAll();
        var companyObjectiveResources = assembler.toCollectionModel(companyObjectives);
        companyObjectiveResources.add(
                linkTo(methodOn(CompanyObjectiveController.class).companyObjectives())
                        .withRel("self"));
        return new ResponseEntity<>(companyObjectiveResources, HttpStatus.OK);
    }
}
