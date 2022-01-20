package com.epro.ws2122.controller;

import com.epro.ws2122.assembler.CompanyKeyResultAssembler;
import com.epro.ws2122.model.CompanyKeyResultModel;
import com.epro.ws2122.repository.CompanyKeyResultRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/company-objectives/{coId}/company-key-results")
public class CompanyKeyResultController {

    private final CompanyKeyResultRepository repository;
    private final CompanyKeyResultAssembler assembler;

    public CompanyKeyResultController(CompanyKeyResultRepository repository, CompanyKeyResultAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    /*
    ToDo:
      - list of all cokrs should have common CO with id 'coId'
      - missing collection resource BUKR and/or BUO
    */
    @GetMapping("/{id}")
    public ResponseEntity<RepresentationModel<CompanyKeyResultModel>> cokrById(
            @PathVariable long coId, @PathVariable("id") long id) {
        var cokr = repository.findById(0L);
        if (cokr.isPresent()) {
            var cokrResource = assembler.toModel(cokr.get());
            cokrResource.add(
                    linkTo(methodOn(CompanyObjectiveController.class).companyObjectiveById(coId)).withRel("companyObjective"),
                    linkTo(methodOn(DashboardController.class).dashboard()).withRel("dashboard"));
            return new ResponseEntity<>(cokrResource, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /*
    ToDo:
      - list of all cokrs should have common CO with id 'coId'
      - missing single resource CO
      - missing collection resource BUKR and/or BUO
     */
    @GetMapping
    public ResponseEntity<CollectionModel<CompanyKeyResultModel>> cokr(@PathVariable long coId) {
        var cokrAll = repository.findAll();
        var cokrResources = assembler.toCollectionModel(cokrAll);
        cokrResources.add(
                linkTo(methodOn(CompanyKeyResultController.class).cokr(coId)).withSelfRel()
                        .andAffordance(afford(methodOn(CompanyKeyResultController.class).newCokr(coId))),
                linkTo(methodOn(CompanyObjectiveController.class).companyObjectiveById(coId)).withRel("companyObjective"),
                linkTo(methodOn(DashboardController.class).dashboard()).withRel("dashboard"));
        return new ResponseEntity<>(cokrResources, HttpStatus.OK);
    }

    /*
    Todo:
        - implement method
    */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCokr(@PathVariable long coId, @PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP DELETE not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PostMapping()
    public ResponseEntity<?> newCokr(@PathVariable long coId) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP POST not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PutMapping("/{id}")
    public ResponseEntity<?> putCokr(@PathVariable long coId, @PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PUT not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchCokr(@PathVariable long coId, @PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PATCH not implemented yet");
    }
}
