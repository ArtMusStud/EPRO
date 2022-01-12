package com.epro.ws2122.controller;

import com.epro.ws2122.assembler.CompanyObjectiveKeyResultAssembler;
import com.epro.ws2122.model.CompanyObjectiveKeyResultModel;
import com.epro.ws2122.model.CompanyObjectiveModel;
import com.epro.ws2122.repository.CompanyObjectiveKeyResultRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/company-objectives/{coId}/company-objectives-key-results")
@ExposesResourceFor(CompanyObjectiveKeyResultModel.class)
public class CompanyObjectiveKeyResultController {

    private final CompanyObjectiveKeyResultRepository repository;
    private final CompanyObjectiveKeyResultAssembler assembler;

    public CompanyObjectiveKeyResultController(CompanyObjectiveKeyResultRepository repository, CompanyObjectiveKeyResultAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepresentationModel<CompanyObjectiveKeyResultModel>> cokrById(
            @PathVariable long coId, @PathVariable("id") long id) {
        var cokr = repository.findById(0L);
        if (cokr.isPresent()) {
            var cokrResource = assembler.toModel(cokr.get());
            return new ResponseEntity<>(cokrResource, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /* ToDo list of all cokrs should have common co of id 'coId'
     */
    @GetMapping
    public ResponseEntity<CollectionModel<CompanyObjectiveKeyResultModel>> cokr(@PathVariable long coId) {
        var cokrAll = repository.findAll();
        var cokrResources = assembler.toCollectionModel(cokrAll);
        cokrResources.add(
                linkTo(methodOn(CompanyObjectiveKeyResultController.class).cokr(coId))
                        .withRel("self"));
        return new ResponseEntity<>(cokrResources, HttpStatus.OK);
    }
}
