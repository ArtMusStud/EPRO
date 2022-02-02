package com.epro.ws2122.controller;

import com.epro.ws2122.model.BusinessUnitObjectiveModel;
import com.epro.ws2122.repository.BusinessUnitObjectiveRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


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
            return new ResponseEntity<>(new BusinessUnitObjectiveModel(businessUnitObjectiveOptional.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<BusinessUnitObjectiveModel>>findAll() {
        var businessUnitObjectiveModels = StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .map(businessUnitObjective -> new BusinessUnitObjectiveModel(businessUnitObjective))
                .collect(Collectors.toList());

        var businessUnitObjectiveResources = CollectionModel.of(businessUnitObjectiveModels);
        return new ResponseEntity<>(businessUnitObjectiveResources, HttpStatus.OK);
    }
}
