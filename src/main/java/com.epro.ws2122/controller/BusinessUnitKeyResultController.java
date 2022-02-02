package com.epro.ws2122.controller;

import com.epro.ws2122.model.BusinessUnitKeyResultModel;
import com.epro.ws2122.model.BusinessUnitObjectiveModel;
import com.epro.ws2122.model.CompanyObjectiveSubresourceModel;
import com.epro.ws2122.repository.BusinessUnitKeyResultRepository;
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
@RequestMapping("/business-unit-objectives/{buoId}/business-unit-key-results")
public class BusinessUnitKeyResultController {

    private final BusinessUnitKeyResultRepository businessUnitKeyResultRepository;
    private final BusinessUnitObjectiveRepository businessUnitObjectiveRepository;

    public BusinessUnitKeyResultController(BusinessUnitKeyResultRepository businessUnitKeyResultRepository, BusinessUnitObjectiveRepository businessUnitObjectiveRepository) {
        this.businessUnitKeyResultRepository = businessUnitKeyResultRepository;
        this.businessUnitObjectiveRepository = businessUnitObjectiveRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepresentationModel<BusinessUnitKeyResultModel>> findOne(
            @PathVariable long buoId, @PathVariable("id") long id) {
        var businessUnitObjectiveOptional = businessUnitObjectiveRepository.findById(buoId);
        var businessUnitKeyResultOptional = businessUnitKeyResultRepository.findById(id);
        if (businessUnitObjectiveOptional.isPresent() && businessUnitKeyResultOptional.isPresent()) {
            //var businessUnitObjective = businessUnitObjectiveOptional.get();
            var businessUnitKeyResult = businessUnitKeyResultOptional.get();

            var businessUnitKeyResultResource = new BusinessUnitKeyResultModel(businessUnitKeyResult);

            var halModelBuilder = HalModelBuilder.halModelOf(businessUnitKeyResultResource)
                    .link(linkTo(methodOn(BusinessUnitKeyResultController.class).findOne(buoId, id)).withSelfRel());

            return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
    ToDo:
            - right now all bukrs are being returned instead of bukr by buoId
     */
    @GetMapping
    public ResponseEntity<CollectionModel<BusinessUnitKeyResultModel>>findAll(@PathVariable long buoId) {
        var businessUnitObjectiveModels = StreamSupport
                .stream(businessUnitKeyResultRepository.findAll().spliterator(), false)
                .map(businessUnitKeyResult -> new BusinessUnitKeyResultModel(businessUnitKeyResult)
                        .add(linkTo(methodOn(BusinessUnitKeyResultController.class)
                                .findOne(buoId, businessUnitKeyResult.getId())).withSelfRel()))
                .collect(Collectors.toList());

        var businessUnitObjectiveResource = CollectionModel.of(businessUnitObjectiveModels);
        return new ResponseEntity<>(businessUnitObjectiveResource, HttpStatus.OK);
    }
}
