package com.epro.ws2122.controller;

import com.epro.ws2122.assembler.CompanyObjectiveAssembler;
import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.model.CompanyObjectiveModel;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyObjectiveController {

    private final CompanyObjectiveRepository repository;
    private final CompanyObjectiveAssembler assembler;

    public CompanyObjectiveController(CompanyObjectiveRepository repository, CompanyObjectiveAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/company-objectives/{id}")
    public ResponseEntity<EntityModel<CompanyObjectiveModel>> companyObjectiveById(@PathVariable("id") Long id) {
        var companyObjective =  repository.findById(id);
        if (companyObjective.isPresent()) {
            var companyObjectiveResource = assembler.toModel(companyObjective.get());
            return new ResponseEntity<>(companyObjectiveResource, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/company-objectives")
    public Iterable<CompanyObjective> companyObjectives() {
        return companyObjectiveRepository.findAll();
    }
}
