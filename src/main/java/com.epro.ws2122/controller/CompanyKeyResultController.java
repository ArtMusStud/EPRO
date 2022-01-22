package com.epro.ws2122.controller;

import com.epro.ws2122.assembler.CompanyKeyResultAssembler;
import com.epro.ws2122.model.CompanyKeyResultModel;
import com.epro.ws2122.model.CompanyObjectiveSubresourceModel;
import com.epro.ws2122.repository.CompanyKeyResultRepository;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/company-objectives/{coId}/company-key-results")
public class CompanyKeyResultController {

    private final CompanyKeyResultRepository companyKeyResultRepository;
    private final CompanyObjectiveRepository companyObjectiveRepository;
    private final CompanyKeyResultAssembler assembler;

    public CompanyKeyResultController(CompanyKeyResultRepository companyKeyResultRepository, CompanyObjectiveRepository companyObjectiveRepository, CompanyKeyResultAssembler assembler) {
        this.companyKeyResultRepository = companyKeyResultRepository;
        this.companyObjectiveRepository = companyObjectiveRepository;
        this.assembler = assembler;
    }

    /*
    ToDo:
      - add missing collection resource BUKR and/or BUO
    */
    @GetMapping("/{id}")
    public ResponseEntity<RepresentationModel<CompanyKeyResultModel>> findOne(
            @PathVariable long coId, @PathVariable("id") long id) {
        var companyObjectiveOptional = companyObjectiveRepository.findById(coId);
        var companyKeyResultOptional = companyKeyResultRepository.findById(id);
        if (companyKeyResultOptional.isPresent() && companyObjectiveOptional.isPresent()) {
            var companyKeyResult = companyKeyResultOptional.get();
            var companyObjective = companyObjectiveOptional.get();
            var companyKeyResultResource = new CompanyKeyResultModel(companyKeyResult);

            var halModelBuilder = HalModelBuilder.halModelOf(companyKeyResultResource)
                    .embed(new CompanyObjectiveSubresourceModel(companyObjective))
                    .link(linkTo(methodOn(CompanyKeyResultController.class).findOne(coId, id)).withSelfRel()
                            .andAffordance(afford(methodOn(CompanyKeyResultController.class).replace(coId, id)))
                            .andAffordance(afford(methodOn(CompanyKeyResultController.class).update(coId, id)))
                            .andAffordance(afford(methodOn(CompanyKeyResultController.class).delete(coId, id))))
                    .link(linkTo(methodOn(CompanyKeyResultController.class).findAll(coId)).withRel("companyKeyResults"))
                    .link(linkTo(methodOn(DashboardController.class).dashboard()).withRel("dashboard"));

            return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /*
    ToDo:
      - missing collection resource BUKR and/or BUO
     */
    @GetMapping
    public ResponseEntity<CollectionModel<CompanyKeyResultModel>> findAll(@PathVariable long coId) {
        var companyKeyResultModels = StreamSupport
                .stream(companyKeyResultRepository.findAll().spliterator(), false)
                .map(companyKeyResult -> new CompanyKeyResultModel(companyKeyResult).add(
                        linkTo((methodOn(CompanyKeyResultController.class)
                                .findOne(coId, companyKeyResult.getId()))).withSelfRel()
                                .andAffordance(afford(methodOn(CompanyKeyResultController.class).replace(coId, companyKeyResult.getId())))
                                .andAffordance(afford(methodOn(CompanyKeyResultController.class).update(coId, companyKeyResult.getId())))
                                .andAffordance(afford(methodOn(CompanyKeyResultController.class).delete(coId, companyKeyResult.getId())))))
                .collect(Collectors.toList());

        var companyKeyResultResources = CollectionModel.of(
                companyKeyResultModels,
                linkTo(methodOn(CompanyKeyResultController.class).findAll(coId)).withSelfRel()
                        .andAffordance(afford(methodOn(CompanyKeyResultController.class).create(coId))),
                linkTo(methodOn(CompanyObjectiveController.class).findOne(coId)).withRel("companyObjective"),
                linkTo(methodOn(CompanyKeyResultController.class).findAll(coId)).withRel("companyKeyResults"),
                linkTo(methodOn(DashboardController.class).dashboard()).withRel("dashboard"));

        return new ResponseEntity<>(companyKeyResultResources, HttpStatus.OK);
    }

    /*
    Todo:
        - implement method
    */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long coId, @PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP DELETE not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PostMapping()
    public ResponseEntity<?> create(@PathVariable long coId) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP POST not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PutMapping("/{id}")
    public ResponseEntity<?> replace(@PathVariable long coId, @PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PUT not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long coId, @PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PATCH not implemented yet");
    }
}
