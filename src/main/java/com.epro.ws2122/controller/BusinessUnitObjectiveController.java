package com.epro.ws2122.controller;

import com.epro.ws2122.dto.BusinessUnitObjective;
import com.epro.ws2122.model.BusinessUnitKeyResultSubresourceModel;
import com.epro.ws2122.model.BusinessUnitObjectiveModel;
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


@RestController
@RequestMapping("/business-unit-objectives")
public class BusinessUnitObjectiveController {

    private final BusinessUnitObjectiveRepository repository;

    public BusinessUnitObjectiveController(BusinessUnitObjectiveRepository repository) {
        this.repository = repository;
    }

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
