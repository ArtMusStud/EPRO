package com.epro.ws2122.controller;

import com.epro.ws2122.dto.CompanyObjective;
import com.epro.ws2122.model.CompanyKeyResultSubresourceModel;
import com.epro.ws2122.model.CompanyObjectiveModel;
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
@RequestMapping("/company-objectives")
public class CompanyObjectiveController {

    private final CompanyObjectiveRepository repository;

    public CompanyObjectiveController(CompanyObjectiveRepository repository) {
        this.repository = repository;
    }

    /*
        ToDo:
            - add link to complete aggregation at level of requested CO
            - add link to dashboard
     */
    @GetMapping("/{id}")
    public ResponseEntity<RepresentationModel<CompanyObjectiveModel>> findOne(@PathVariable("id") long id) {
        var companyObjectiveOptional = repository.findById(id);
        if (companyObjectiveOptional.isPresent()) {
            var companyObjective = companyObjectiveOptional.get();
            var companyObjectiveResource = new CompanyObjectiveModel(companyObjective);

            var companyKeyResultSubresourceModelList = companyObjective.getCompanyKeyResults();

            var halModelBuilder = HalModelBuilder.halModelOf(companyObjectiveResource)
                    .link(linkTo(methodOn(CompanyObjectiveController.class).findOne(id)).withSelfRel()
                            .andAffordance(afford(methodOn(CompanyObjectiveController.class).replace(null, id)))
                            .andAffordance(afford(methodOn(CompanyObjectiveController.class).update(id)))
                            .andAffordance(afford(methodOn(CompanyObjectiveController.class).delete(id))));

            for (var subresource : companyKeyResultSubresourceModelList) {
                var companyKeyResults = new CompanyKeyResultSubresourceModel(id, subresource);
                halModelBuilder.embed(companyKeyResults);
            }

            return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<CompanyObjectiveModel>> findAll() {
        var companyObjectiveModels = StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .map(companyObjective -> new CompanyObjectiveModel(companyObjective).add(
                        linkTo((methodOn(CompanyObjectiveController.class)
                                .findOne(companyObjective.getId()))).withSelfRel()
                                .andAffordance(afford(methodOn(CompanyObjectiveController.class).replace(null, companyObjective.getId())))
                                .andAffordance(afford(methodOn(CompanyObjectiveController.class).update(companyObjective.getId())))
                                .andAffordance(afford(methodOn(CompanyObjectiveController.class).delete(companyObjective.getId())))))
                .collect(Collectors.toList());

        var companyObjectiveResources = CollectionModel.of(
                companyObjectiveModels,
                linkTo(methodOn(CompanyObjectiveController.class).findAll()).withSelfRel()
                        .andAffordance(afford(methodOn(CompanyObjectiveController.class).create())));

        return new ResponseEntity<>(companyObjectiveResources, HttpStatus.OK);
    }

    /*
        Todo:
            - implement method
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP DELETE not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PostMapping()
    public ResponseEntity<?> create() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP POST not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PutMapping("/{id}")
    public ResponseEntity<?> replace(@RequestBody CompanyObjective companyObjectiveDTO, @PathVariable long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PUT not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PATCH not implemented yet");
    }
}
