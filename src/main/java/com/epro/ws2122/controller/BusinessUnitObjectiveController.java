package com.epro.ws2122.controller;

import com.epro.ws2122.dto.BusinessUnitObjective;
import com.epro.ws2122.dto.CompanyObjective;
import com.epro.ws2122.model.BusinessUnitKeyResultSubresourceModel;
import com.epro.ws2122.model.BusinessUnitObjectiveModel;
import com.epro.ws2122.model.CompanyKeyResultModel;
import com.epro.ws2122.model.CompanyObjectiveModel;
import com.epro.ws2122.repository.BusinessUnitObjectiveRepository;
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
 * A {@link RestController} for processing http client requests sent to the templated uri path <br><b>/business-unit-objectives/{id}</b>.
 * <p>
 * The path contains the variable {@code id}:
 * <ul>
 * <li>{@code id} relates to the id of the requested business unit objective.</li>
 * </ul><p>
 * Client requests that will be processed are:
 * <ul>
 * <li>{@link #findOne(long) GET} for a single resource</li>
 * <li>{@link #findAll() GET} for a collection resource</li>
 * <li>{@link #update(BusinessUnitObjective, long) PATCH}</li>
 * <li>{@link #replace(BusinessUnitObjective, long) PUT}</li>
 * <li>{@link #create(BusinessUnitObjective) POST}</li>
 * <li>{@link #delete(long) DELETE}</li>
 * </ul>
 */
@RestController
@RequestMapping("/business-unit-objectives")
public class BusinessUnitObjectiveController {

    /**
     * Repository from which to retrieve entities of type {@link com.epro.ws2122.domain.BusinessUnitObjective BusinessUnitObjective}.
     */
    private final BusinessUnitObjectiveRepository repository;

    public BusinessUnitObjectiveController(BusinessUnitObjectiveRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns a business unit objective, depending on whether the uri path leads to an obtainable resource, along with an HTTP status code.
     * <p>
     * The data of a returned business unit objective resource is defined in the DTO class {@link CompanyObjectiveModel}.
     * <p>
     * HTTP status codes returned:
     * <ul>
     *     <li>{@code 200} if the resource can be obtained.
     *     <li>{@code 404} if the uri path doesn't lead to an existing resource.
     * </ul>
     * @param id id of the business unit objective.
     * @return a business unit objective resource or null, and an HTTP status code.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RepresentationModel<BusinessUnitObjectiveModel>> findOne(@PathVariable("id") long id) {
        var buoOptional = repository.findById(id);
        if (buoOptional.isPresent()) {
            var buo = buoOptional.get();
            var buoResource = new BusinessUnitObjectiveModel(buo);
            var halModelBuilder = HalModelBuilder.halModelOf(buoResource)
                    .link(linkTo(methodOn(BusinessUnitObjectiveController.class).findOne(id)).withSelfRel()
                            .andAffordance(afford(methodOn(BusinessUnitObjectiveController.class).replace(null, id)))
                            .andAffordance(afford(methodOn(BusinessUnitObjectiveController.class).update(null, id)))
                            .andAffordance(afford(methodOn(BusinessUnitObjectiveController.class).delete(id))));

            for (var subresource : buo.getBusinessUnitKeyResults()) {
                var bukr = new BusinessUnitKeyResultSubresourceModel(id, subresource);
                halModelBuilder.embed(bukr);
            }

            return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Returns a collection resource of multiple business unit objectives, depending on whether the uri path leads to obtainable resources, along with an HTTP status code.
     * <p>
     * The properties of each of the returned business unit objective resources are defined in the DTO {@link BusinessUnitObjective}.
     * <p>
     * HTTP status codes returned:
     * <ul>
     *     <li>{@code 200} if the collection resource can be obtained.
     * </ul>
     * @return business unit objective collection resource or an empty list, and an HTTP status code.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<BusinessUnitObjectiveModel>>findAll() {
        var buoModels = StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .map(buo -> new BusinessUnitObjectiveModel(buo)
                        .add(linkTo(methodOn(BusinessUnitObjectiveController.class)
                                .findOne(buo.getId())).withSelfRel()
                                .andAffordance(afford(methodOn(BusinessUnitObjectiveController.class).replace(null, buo.getId())))
                                .andAffordance(afford(methodOn(BusinessUnitObjectiveController.class).update(null, buo.getId())))
                                .andAffordance(afford(methodOn(BusinessUnitObjectiveController.class).delete(buo.getId())))))
                .collect(Collectors.toList());

        var buoResource = CollectionModel.of(
                buoModels,
                linkTo(methodOn(BusinessUnitObjectiveController.class).findAll()).withSelfRel()
                        .andAffordance(afford(methodOn(BusinessUnitObjectiveController.class).create(null))));

        return new ResponseEntity<>(buoResource, HttpStatus.OK);
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
    public ResponseEntity<?> create(@RequestBody BusinessUnitObjective buoDTO) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP POST not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PutMapping("/{id}")
    public ResponseEntity<?> replace(@RequestBody BusinessUnitObjective buoDTO, @PathVariable long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PUT not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody BusinessUnitObjective buoDTO, @PathVariable long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PATCH not implemented yet");
    }
}
