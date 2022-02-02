package com.epro.ws2122.controller;

import com.epro.ws2122.dto.CompanyKeyResult;
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

/**
 * A {@link RestController} for processing http client requests sent to the templated uri path <br><b>/company-objectives/{coId}/company-key-results/{id}</b>.
 * <p>
 * The path contains variables {@code coId} and {@code id}:
 * <ul>
 * <li>{@code coId} is mandatory and refers to the id of the company objective resource from which company key result resources are being requested.</li>
 * <li>{@code id} relates to the id of the requested company key result.</li>
 * </ul><p>
 * Client requests that will be processed are:
 * <ul>
 * <li>{@link #findOne(long, long) GET} for single resources</li>
 * <li>{@link #findAll(long) GET} for collection resources</li>
 * <li>{@link #update(CompanyKeyResult, long, long) PATCH}</li>
 * <li>{@link #replace(CompanyKeyResult, long, long) PUT}</li>
 * <li>{@link #create(CompanyKeyResult, long) POST}</li>
 * <li>{@link #delete(long, long) DELETE}</li>
 * </ul>
 */
@RestController
@RequestMapping("/company-objectives/{coId}/company-key-results")
public class CompanyKeyResultController {

    /**
     * Repository from which to retrieve entities of type {@link com.epro.ws2122.domain.CompanyObjective CompanyObjective}.
     */
    private final CompanyKeyResultRepository companyKeyResultRepository;
    /**
     * Repository from which to retrieve entities of type {@link com.epro.ws2122.domain.CompanyKeyResult CompanyKeyResult}.
     */
    private final CompanyObjectiveRepository companyObjectiveRepository;

    public CompanyKeyResultController(CompanyKeyResultRepository companyKeyResultRepository, CompanyObjectiveRepository companyObjectiveRepository) {
        this.companyKeyResultRepository = companyKeyResultRepository;
        this.companyObjectiveRepository = companyObjectiveRepository;
    }

    /**
     * Returns a company key result, depending on whether the uri path leads to an obtainable resource, along with an HTTP status code.
     * <p>
     * The data of a company key result resource that is being returned is defined in the DTO class {@link CompanyKeyResultModel}.
     * <p>
     * Returned HTTP status codes:
     * <ul>
     *     <li>{@code 200} if the resource can be obtained.
     *     <li>{@code 404} if the uri path doesn't lead to an existing resource.
     * </ul>
     * @param coId id of the company objective.
     * @param id id of the company key result.
     * @return a company key result resource or null, and an HTTP status code.
     */
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
                            .andAffordance(afford(methodOn(CompanyKeyResultController.class).replace(null, coId, id)))
                            .andAffordance(afford(methodOn(CompanyKeyResultController.class).update(null, coId, id)))
                            .andAffordance(afford(methodOn(CompanyKeyResultController.class).delete(coId, id))))
                    .link(linkTo(methodOn(CompanyKeyResultController.class).findAll(coId)).withRel("companyKeyResults"))
                    .link(linkTo(methodOn(DashboardController.class).dashboard()).withRel("dashboard"));

            return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Returns multiple company key results, depending on whether the uri path leads to obtainable resources, along with an HTTP status code.
     * <p>
     * The properties of each of the returned company key result resources (if any) are defined in the DTO {@link CompanyKeyResultModel}.
     * <p>
     * Returned HTTP status codes:
     * <ul>
     *     <li>{@code 200} if the collection resource can be obtained.
     *     <li>{@code 404} if {@code coId} doesn't lead to an existing resource.
     * </ul>
     * @param coId id of the company objective
     * @return company key result collection resource or an empty list, and an HTTP status code.
     */
    /*
    ToDo:
      - right now all ckrs are being returned instead of ckr by coId
      - missing collection resource BUKR and/or BUO
     */
    @GetMapping
    public ResponseEntity<CollectionModel<CompanyKeyResultModel>> findAll(@PathVariable long coId) {
        var companyKeyResultModels = StreamSupport
                .stream(companyKeyResultRepository.findAll().spliterator(), false)
                .map(companyKeyResult -> new CompanyKeyResultModel(companyKeyResult).add(
                        linkTo((methodOn(CompanyKeyResultController.class)
                                .findOne(coId, companyKeyResult.getId()))).withSelfRel()
                                .andAffordance(afford(methodOn(CompanyKeyResultController.class).replace(null, coId, companyKeyResult.getId())))
                                .andAffordance(afford(methodOn(CompanyKeyResultController.class).update(null, coId, companyKeyResult.getId())))
                                .andAffordance(afford(methodOn(CompanyKeyResultController.class).delete(coId, companyKeyResult.getId())))))
                .collect(Collectors.toList());

        var companyKeyResultResources = CollectionModel.of(
                companyKeyResultModels,
                linkTo(methodOn(CompanyKeyResultController.class).findAll(coId)).withSelfRel()
                        .andAffordance(afford(methodOn(CompanyKeyResultController.class).create(null, coId))),
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
    public ResponseEntity<?> create(@RequestBody CompanyKeyResult companyKeyResultDTO, @PathVariable long coId) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP POST not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PutMapping("/{id}")
    public ResponseEntity<?> replace(@RequestBody CompanyKeyResult companyKeyResultDTO, @PathVariable long coId, @PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PUT not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody CompanyKeyResult companyKeyResultDTO, @PathVariable long coId, @PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PATCH not implemented yet");
    }
}
