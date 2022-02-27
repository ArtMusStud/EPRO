package com.epro.ws2122.controller;

import com.epro.ws2122.dto.CoDTO;
import com.epro.ws2122.model.CompanyKeyResultModel;
import com.epro.ws2122.model.CompanyKeyResultSubresourceModel;
import com.epro.ws2122.model.CompanyObjectiveModel;
import com.epro.ws2122.model.CompanyObjectiveSubresourceModel;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import lombok.RequiredArgsConstructor;
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
 * The path contains the variable {@code id}:
 * <ul>
 * <li>{@code id} relates to the id of the requested company objective.</li>
 * </ul><p>
 * Client requests that will be processed are:
 * <ul>
 * <li>{@link #findOne(long) GET} for a single resource</li>
 * <li>{@link #findAll() GET} for a collection resource</li>
 * <li>{@link #update(CoDTO, long) PATCH}</li>
 * <li>{@link #replace(CoDTO, long) PUT}</li>
 * <li>{@link #create(CoDTO) POST}</li>
 * <li>{@link #delete(long) DELETE}</li>
 * </ul>
 */
@RestController
@RequestMapping("/company-objectives")
@RequiredArgsConstructor
public class CompanyObjectiveController {

    /**
     * Repository from which to retrieve entities of type {@link com.epro.ws2122.domain.CompanyObjective CompanyObjective}.
     */
    private final CompanyObjectiveRepository repository;

    /**
     * Returns a company objective, depending on whether the uri path leads to an obtainable resource, along with an HTTP status code.
     * <p>
     * The data of a returned company objective resource is defined in the DTO class {@link CompanyObjectiveModel}.
     * <p>
     * HTTP status codes returned:
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
     */
    @GetMapping("/{id}")
    public ResponseEntity<RepresentationModel<CompanyObjectiveModel>> findOne(@PathVariable("id") long id) {
        var coOptional = repository.findById(id);
        if (coOptional.isPresent()) {
            var co = coOptional.get();
            var coResource = new CompanyObjectiveModel(co);
            var halModelBuilder = HalModelBuilder.halModelOf(coResource)
                    .link(linkTo(methodOn(CompanyObjectiveController.class).findOne(id)).withSelfRel()
                            .andAffordance(afford(methodOn(CompanyObjectiveController.class).replace(null, id)))
                            .andAffordance(afford(methodOn(CompanyObjectiveController.class).update(null, id)))
                            .andAffordance(afford(methodOn(CompanyObjectiveController.class).delete(id))))
                    .link(linkTo(methodOn(CompanyObjectiveController.class).findAll()).withRel("companyObjectives"));

            for (var subresource : co.getCompanyKeyResults()) {
                var ckr = new CompanyKeyResultSubresourceModel(id, subresource);
                halModelBuilder.embed(ckr);
            }

            return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Returns a collection resource of multiple company objectives, depending on whether the uri path leads to obtainable resources, along with an HTTP status code.
     * <p>
     * The properties of each of the returned company objective resources are defined in the DTO {@link CompanyObjectiveModel}.
     * <p>
     * HTTP status codes returned:
     * <ul>
     *     <li>{@code 200} if the collection resource can be obtained.
     * </ul>
     * @return company objective collection resource or an empty list, and an HTTP status code.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<CompanyObjectiveModel>> findAll() {
        var coModels = StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .map(co -> new CompanyObjectiveModel(co)
                        .add(linkTo((methodOn(CompanyObjectiveController.class)
                                .findOne(co.getId()))).withSelfRel()
                                .andAffordance(afford(methodOn(CompanyObjectiveController.class).replace(null, co.getId())))
                                .andAffordance(afford(methodOn(CompanyObjectiveController.class).update(null, co.getId())))
                                .andAffordance(afford(methodOn(CompanyObjectiveController.class).delete(co.getId())))))
                .collect(Collectors.toList());

        var coResource = CollectionModel.of(
                coModels,
                linkTo(methodOn(CompanyObjectiveController.class).findAll()).withSelfRel()
                        .andAffordance(afford(methodOn(CompanyObjectiveController.class).create(null))));

        return new ResponseEntity<>(coResource, HttpStatus.OK);
    }

    /*
        Todo:
            - implement method
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP DELETE not implemented yet");
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody CoDTO coDTO) {
        var savedCo = repository.save(coDTO.toCoEntity());
        var coResource = new CompanyObjectiveModel(savedCo);
        var halModelBuilder = HalModelBuilder.halModelOf(coResource)
                .link(linkTo(methodOn(CompanyObjectiveController.class).findOne(savedCo.getId())).withSelfRel()
                        .andAffordance(afford(methodOn(CompanyObjectiveController.class).replace(null, savedCo.getId())))
                        .andAffordance(afford(methodOn(CompanyObjectiveController.class).update(null, savedCo.getId())))
                        .andAffordance(afford(methodOn(CompanyObjectiveController.class).delete(savedCo.getId()))))
                .link(linkTo(methodOn(CompanyObjectiveController.class).findAll()).withRel("companyObjectives"));

        return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.CREATED);
    }

    /*
    Todo:
        - implement method
    */
    @PutMapping("/{id}")
    public ResponseEntity<?> replace(@RequestBody CoDTO coDTO, @PathVariable long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PUT not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody CoDTO coDTO, @PathVariable long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PATCH not implemented yet");
    }
}
