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

/**
 * A {@link RestController} for processing http client requests sent to the templated uri path <br><b>/company-objectives/{id}</b>.
 * <p>
 * The path contains variable {@code id}:
 * <ul>
 * <li>{@code id} relates to the id of the requested company objective.</li>
 * </ul><p>
 * Client requests that will be processed are:
 * <ul>
 * <li>{@link #findOne(long) GET} for single resources</li>
 * <li>{@link #findAll() GET} for collection resources</li>
 * <li>{@link #update(CompanyObjective, long) PATCH}</li>
 * <li>{@link #replace(CompanyObjective, long) PUT}</li>
 * <li>{@link #create(CompanyObjective) POST}</li>
 * <li>{@link #delete(long) DELETE}</li>
 * </ul>
 */
@RestController
@RequestMapping("/company-objectives")
public class CompanyObjectiveController {

    /**
     * Repository from which to retrieve entities of type {@link com.epro.ws2122.domain.CompanyObjective CompanyObjective}.
     */
    private final CompanyObjectiveRepository repository;

    public CompanyObjectiveController(CompanyObjectiveRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns a company objective, depending on whether the uri path leads to an obtainable resource, along with an HTTP status code.
     * <p>
     * The data of a company objective resource that is being returned is defined in the DTO class {@link CompanyObjectiveModel}.
     * <p>
     * Returned HTTP status codes:
     * <ul>
     *     <li>{@code 200} if the resource can be obtained.
     *     <li>{@code 404} if the uri path doesn't lead to an existing resource.
     * </ul>
     * @param id id of the company objective.
     * @return a company objective resource or null, and an HTTP status code.
     */
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
                            .andAffordance(afford(methodOn(CompanyObjectiveController.class).update(null, id)))
                            .andAffordance(afford(methodOn(CompanyObjectiveController.class).delete(id))));

            for (var subresource : companyKeyResultSubresourceModelList) {
                var companyKeyResults = new CompanyKeyResultSubresourceModel(id, subresource);
                halModelBuilder.embed(companyKeyResults);
            }

            return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Returns multiple company objectives, depending on whether the uri path leads to obtainable resources, along with an HTTP status code.
     * <p>
     * The properties of each of the returned company objective resources (if any) are defined in the DTO {@link CompanyObjectiveModel}.
     * <p>
     * Returned HTTP status codes:
     * <ul>
     *     <li>{@code 200} if the collection resource can be obtained.
     * </ul>
     * @return company objective collection resource or an empty list, and an HTTP status code.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<CompanyObjectiveModel>> findAll() {
        var companyObjectiveModels = StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .map(companyObjective -> new CompanyObjectiveModel(companyObjective).add(
                        linkTo((methodOn(CompanyObjectiveController.class)
                                .findOne(companyObjective.getId()))).withSelfRel()
                                .andAffordance(afford(methodOn(CompanyObjectiveController.class).replace(null, companyObjective.getId())))
                                .andAffordance(afford(methodOn(CompanyObjectiveController.class).update(null, companyObjective.getId())))
                                .andAffordance(afford(methodOn(CompanyObjectiveController.class).delete(companyObjective.getId())))))
                .collect(Collectors.toList());

        var companyObjectiveResources = CollectionModel.of(
                companyObjectiveModels,
                linkTo(methodOn(CompanyObjectiveController.class).findAll()).withSelfRel()
                        .andAffordance(afford(methodOn(CompanyObjectiveController.class).create(null))));

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
    public ResponseEntity<?> create(@RequestBody CompanyObjective companyObjectiveDTO) {
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
    public ResponseEntity<?> update(@RequestBody CompanyObjective companyObjectiveDTO, @PathVariable long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PATCH not implemented yet");
    }
}
