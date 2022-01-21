package com.epro.ws2122.controller;

import com.epro.ws2122.assembler.CompanyObjectiveAssembler;
import com.epro.ws2122.model.CompanyKeyResultSubresourceModel;
import com.epro.ws2122.model.CompanyObjectiveModel;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/company-objectives")
public class CompanyObjectiveController {

    private final CompanyObjectiveRepository repository;
    private final CompanyObjectiveAssembler assembler;

    public CompanyObjectiveController(CompanyObjectiveRepository repository, CompanyObjectiveAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    /*
        ToDo:
            - add link to complete aggregation at level of requested CO
            - add link to dashboard
     */
    @GetMapping( "/{id}")
    public ResponseEntity<RepresentationModel<CompanyObjectiveModel>> companyObjectiveById(@PathVariable("id") long id) {
        var companyObjectiveOptional = repository.findById(id);
        if (companyObjectiveOptional.isPresent()) {
            var companyObjective = companyObjectiveOptional.get();
            var companyObjectiveResource = new CompanyObjectiveModel(companyObjective);

            var companyKeyResultSubresourceModelList = companyObjective.getCompanyKeyResults();

            var halModelBuilder = HalModelBuilder.halModelOf(companyObjectiveResource)
                    .link(linkTo(methodOn(CompanyObjectiveController.class).companyObjectiveById(id)).withSelfRel()
                            .andAffordance(afford(methodOn(CompanyObjectiveController.class).putCompanyObjective(id)))
                            .andAffordance(afford(methodOn(CompanyObjectiveController.class).patchCompanyObjective(id)))
                            .andAffordance(afford(methodOn(CompanyObjectiveController.class).deleteCompanyObjective(id))));

            for (var subresource : companyKeyResultSubresourceModelList) {
                halModelBuilder.preview(new CompanyKeyResultSubresourceModel(id, subresource))
                        .forLink(linkTo((
                                methodOn(CompanyKeyResultController.class).cokrById(id, subresource.getId())))
                                .withRel("companyKeyResults"));
            }

            return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /*
        ToDo:
            - add collection resource of all CO KR corresponding to each requested CO
     */
    @GetMapping
    public ResponseEntity<CollectionModel<CompanyObjectiveModel>> companyObjectives() {
        var companyObjectives = repository.findAll();
        var companyObjectiveResources = assembler.toCollectionModel(companyObjectives);
        companyObjectiveResources.add(
                linkTo(methodOn(CompanyObjectiveController.class).companyObjectives()).withSelfRel()
                        .andAffordance(afford(methodOn(CompanyObjectiveController.class).newCompanyObjective())));
        return new ResponseEntity<>(companyObjectiveResources, HttpStatus.OK);
    }

    /*
        Todo:
            - implement method
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompanyObjective(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP DELETE not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PostMapping()
    public ResponseEntity<?> newCompanyObjective() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP POST not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PutMapping("/{id}")
    public ResponseEntity<?> putCompanyObjective(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PUT not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchCompanyObjective(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PATCH not implemented yet");
    }
}
