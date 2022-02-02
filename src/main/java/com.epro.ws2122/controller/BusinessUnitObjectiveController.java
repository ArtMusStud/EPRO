package com.epro.ws2122.controller;

import com.epro.ws2122.model.BusinessUnitKeyResultSubresourceModel;
import com.epro.ws2122.model.BusinessUnitObjectiveModel;
import com.epro.ws2122.model.CompanyKeyResultSubresourceModel;
import com.epro.ws2122.repository.BusinessUnitObjectiveRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/business-unit-objectives")
public class BusinessUnitObjectiveController {

    private final BusinessUnitObjectiveRepository repository;

    public BusinessUnitObjectiveController(BusinessUnitObjectiveRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepresentationModel<BusinessUnitObjectiveModel>> findOne(@PathVariable("id") long id) {
        var businessUnitObjectiveOptional = repository.findById(id);
        if (businessUnitObjectiveOptional.isPresent()) {
            var businessUnitObjective = businessUnitObjectiveOptional.get();
            var businessUnitObjectiveResource = new BusinessUnitObjectiveModel(businessUnitObjective);
            var halModelBuilder = HalModelBuilder.halModelOf(businessUnitObjectiveResource)
                    .link(linkTo(methodOn(BusinessUnitObjectiveController.class).findOne(id)).withSelfRel());

            for (var subresource : businessUnitObjective.getBusinessUnitKeyResults()) {
                var businessUnitKeyResult = new BusinessUnitKeyResultSubresourceModel(id, subresource);
                halModelBuilder.embed(businessUnitKeyResult);
            }

            return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<BusinessUnitObjectiveModel>>findAll() {
        var businessUnitObjectiveModels = StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .map(businessUnitObjective -> new BusinessUnitObjectiveModel(businessUnitObjective)
                        .add(linkTo(methodOn(BusinessUnitObjectiveController.class)
                                .findOne(businessUnitObjective.getId())).withSelfRel()))
                .collect(Collectors.toList());

        var businessUnitObjectiveResource = CollectionModel.of(
                businessUnitObjectiveModels,
                linkTo(methodOn(BusinessUnitObjectiveController.class).findAll()).withSelfRel());

        return new ResponseEntity<>(businessUnitObjectiveResource, HttpStatus.OK);
    }
}
