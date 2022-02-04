package com.epro.ws2122.controller;

import com.epro.ws2122.dto.BusinessUnitKeyResult;
import com.epro.ws2122.dto.CompanyKeyResult;
import com.epro.ws2122.model.BusinessUnitKeyResultModel;
import com.epro.ws2122.model.BusinessUnitObjectiveSubresourceModel;
import com.epro.ws2122.model.CompanyKeyResultSubresourceModel;
import com.epro.ws2122.repository.BusinessUnitKeyResultRepository;
import com.epro.ws2122.repository.BusinessUnitObjectiveRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/business-unit-objectives/{buoId}/business-unit-key-results")
public class BusinessUnitKeyResultController {

    private final BusinessUnitKeyResultRepository bukrRepository;
    private final BusinessUnitObjectiveRepository buoRepository;

    public BusinessUnitKeyResultController(BusinessUnitKeyResultRepository bukrRepository, BusinessUnitObjectiveRepository buoRepository) {
        this.bukrRepository = bukrRepository;
        this.buoRepository = buoRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepresentationModel<BusinessUnitKeyResultModel>> findOne(
            @PathVariable long buoId, @PathVariable("id") long id) {
        var buoOptional = buoRepository.findById(buoId);
        var bukrOptional = bukrRepository.findById(id);
        if (buoOptional.isPresent() && bukrOptional.isPresent()) {
            var buo = buoOptional.get();
            var bukr = bukrOptional.get();
            var bukrResource = new BusinessUnitKeyResultModel(bukr);
            var assignedCompanyKeyResult = bukr.getCompanyKeyResult();
            var halModelBuilder = HalModelBuilder.halModelOf(bukrResource)
                    .embed(new BusinessUnitObjectiveSubresourceModel(buo))
                    .embed(new CompanyKeyResultSubresourceModel(assignedCompanyKeyResult.getCompanyObjective().getId(), assignedCompanyKeyResult))
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
        var buoModels = StreamSupport
                .stream(bukrRepository.findAll().spliterator(), false)
                .map(bukr -> new BusinessUnitKeyResultModel(bukr)
                        .add(linkTo(methodOn(BusinessUnitKeyResultController.class)
                                .findOne(buoId, bukr.getId())).withSelfRel()))
                .collect(Collectors.toList());

        var buoResource = CollectionModel.of(buoModels);
        return new ResponseEntity<>(buoResource, HttpStatus.OK);
    }
    /*
     Todo:
         - implement method
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long buoId, @PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP DELETE not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody BusinessUnitKeyResult buoDTO, @PathVariable long buoId) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP POST not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PutMapping("/{id}")
    public ResponseEntity<?> replace(@RequestBody BusinessUnitKeyResult buoDTO, @PathVariable long buoId, @PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PUT not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody BusinessUnitKeyResult buoDTO, @PathVariable long buoId, @PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PATCH not implemented yet");
    }
}
