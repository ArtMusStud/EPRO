package com.epro.ws2122.controller;

import com.epro.ws2122.dto.BukrDTO;
import com.epro.ws2122.model.BusinessUnitKeyResultModel;
import com.epro.ws2122.model.BusinessUnitObjectiveSubresourceModel;
import com.epro.ws2122.model.CompanyKeyResultSubresourceModel;
import com.epro.ws2122.repository.BusinessUnitKeyResultRepository;
import com.epro.ws2122.repository.BusinessUnitObjectiveRepository;
import com.epro.ws2122.repository.CompanyKeyResultRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * A {@link RestController} for processing http client requests sent to the templated uri path <br><b>/business-unit-objectives/{buoId}/business-unit-key-results/{id}</b>.
 * <p>
 * The path contains the variables {@code buoId} and {@code id}:
 * <ul>
 * <li>{@code buoId} is mandatory and refers to the id of the business unit objective resource from which business unit key result resources are being requested.</li>
 * <li>{@code id} relates to the id of the requested business unit key result.</li>
 * </ul><p>
 * Client requests that will be processed are:
 * <ul>
 * <li>{@link #findOne(long, long) GET} for a single resource</li>
 * <li>{@link #findAll(long) GET} for a collection resource</li>
 * <li>{@link #update(BukrDTO, long, long) PATCH}</li>
 * <li>{@link #replace(BukrDTO, long, long) PUT}</li>
 * <li>{@link #create(BukrDTO, long) POST}</li>
 * <li>{@link #delete(long, long) DELETE}</li>
 * </ul>
 */
@RestController
@RequestMapping("/business-unit-objectives/{buoId}/business-unit-key-results")
public class BusinessUnitKeyResultController {

    /**
     * Repository from which to retrieve entities of type {@link com.epro.ws2122.domain.BusinessUnitKeyResult BusinessUnitKeyResult}.
     */
    private final BusinessUnitKeyResultRepository bukrRepository;
    /**
     * Repository from which to retrieve entities of type {@link com.epro.ws2122.domain.BusinessUnitObjective BusinessUnitObjective}.
     */
    private final BusinessUnitObjectiveRepository buoRepository;

    private final CompanyKeyResultRepository ckrRepository;

    public BusinessUnitKeyResultController(BusinessUnitKeyResultRepository bukrRepository,
                                           BusinessUnitObjectiveRepository buoRepository,
                                           CompanyKeyResultRepository ckrRepository) {
        this.bukrRepository = bukrRepository;
        this.buoRepository = buoRepository;
        this.ckrRepository = ckrRepository;
    }

    /**
     * Returns a business unit key result, depending on whether the uri path leads to an obtainable resource, along with an HTTP status code.
     * <p>
     * The data of a returned business unit key result resource is defined in the DTO class {@link BukrDTO}.
     * <p>
     * HTTP status codes returned:
     * <ul>
     *     <li>{@code 200} if the resource can be obtained.
     *     <li>{@code 404} if the uri path doesn't lead to an existing resource.
     * </ul>
     * @param buoId id of the business unit objective.
     * @param id id of the business unit key result.
     * @return a business unit key result resource or null, and an HTTP status code.
     */
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
                    .link(linkTo(methodOn(BusinessUnitKeyResultController.class).findOne(buoId, id)).withSelfRel()
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).replace(null, buoId, id)))
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).update(null, buoId, id)))
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).delete(buoId, id))))
                    .link(linkTo(methodOn(BusinessUnitKeyResultController.class).findAll(buoId)).withRel("businessUnitKeyResults"));

            return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Returns a collection resource of multiple business unit key results, depending on whether the uri path leads to obtainable resources, along with an HTTP status code.
     * <p>
     * The properties of each of the returned business unit key result resources are defined in the DTO {@link BukrDTO}.
     * <p>
     * HTTP status codes returned:
     * <ul>
     *     <li>{@code 200} if the collection resource can be obtained.
     *     <li>{@code 404} if {@code buoId} doesn't lead to an existing resource.
     * </ul>
     * @param buoId id of the company objective
     * @return business unit key result collection resource or an empty list, and an HTTP status code.
     */
    @GetMapping
    public ResponseEntity<CollectionModel<BusinessUnitKeyResultModel>>findAll(@PathVariable long buoId) {
        var buoOptional = buoRepository.findById(buoId);
        if (buoOptional.isPresent()) {
            var buo = buoOptional.get();
            var buoModels = bukrRepository
                    .findAllByBusinessUnitObjective(buo)
                    .stream()
                    .map(bukr -> new BusinessUnitKeyResultModel(bukr)
                            .add(linkTo(methodOn(BusinessUnitKeyResultController.class)
                                    .findOne(buoId, bukr.getId())).withSelfRel()
                                    .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).replace(null, buoId, bukr.getId())))
                                    .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).update(null, buoId, bukr.getId())))
                                    .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).delete(buoId, bukr.getId())))))
                    .collect(Collectors.toList());

            var buoResource = CollectionModel.of(
                    buoModels,
                    linkTo(methodOn(BusinessUnitKeyResultController.class).findAll(buoId)).withSelfRel()
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).create(null, buoId))),
                    linkTo(methodOn(BusinessUnitObjectiveController.class).findOne(buoId)).withRel("businessUnitObjective"));

            return new ResponseEntity<>(buoResource, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /*
     Todo:
         - implement method
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long buoId, @PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP DELETE not implemented yet");
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody BukrDTO bukrDTO, @PathVariable long buoId) {
        var buoOptional = buoRepository.findById(buoId);
        if (buoOptional.isPresent()) {
            var buo = buoOptional.get();
            var savedBukr = bukrRepository.save(bukrDTO.toBukrEntity());
            var bukrResource = new BusinessUnitKeyResultModel(savedBukr);
            var halModelBuilder = HalModelBuilder.halModelOf(bukrResource)
                    .embed(new BusinessUnitObjectiveSubresourceModel(buo))
                    .link(linkTo(methodOn(BusinessUnitKeyResultController.class).findOne(buoId, savedBukr.getId())).withSelfRel()
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).replace(null, buoId, savedBukr.getId())))
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).update(null, buoId, savedBukr.getId())))
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).delete(buoId, savedBukr.getId()))))
                    .link(linkTo(methodOn(BusinessUnitKeyResultController.class).findAll(buoId)).withRel("businessUnitKeyResults"));

            return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
    Todo:
        - implement method
    */
    @PutMapping("/{id}")
    public ResponseEntity<?> replace(@RequestBody BukrDTO buoDTO, @PathVariable long buoId, @PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PUT not implemented yet");
    }

    /*
    Todo:
        - implement method
    */
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody BukrDTO buoDTO, @PathVariable long buoId, @PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("HTTP PATCH not implemented yet");
    }

    @PostMapping("/{id}/link")
    public ResponseEntity<?> link(@RequestBody Map<String, Long> ckrById, @PathVariable long id, @PathVariable long buoId) {
        var buoOptional = buoRepository.findById(buoId);
        var bukrOptional = bukrRepository.findById(id);
        var ckrOptional = ckrRepository.findById(ckrById.get("id"));
        if (buoOptional.isPresent() && bukrOptional.isPresent() && ckrOptional.isPresent()) {
            var buo = buoOptional.get();
            var bukr = bukrOptional.get();
            var ckr = ckrOptional.get();

            ckr.getBusinessUnitKeyResults().add(bukr);

            var bukrResource = new BusinessUnitKeyResultModel(bukr);
            var assignedCompanyKeyResult = bukr.getCompanyKeyResult();
            System.out.println("asssigned? " + assignedCompanyKeyResult.getName());
            var halModelBuilder = HalModelBuilder.halModelOf(bukrResource)
                    .embed(new BusinessUnitObjectiveSubresourceModel(buo))
                  //  .embed(new CompanyKeyResultSubresourceModel(assignedCompanyKeyResult.getCompanyObjective().getId(), assignedCompanyKeyResult))
                    .link(linkTo(methodOn(BusinessUnitKeyResultController.class).findOne(buoId, id)).withSelfRel()
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).replace(null, buoId, id)))
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).update(null, buoId, id)))
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).delete(buoId, id))))
                    .link(linkTo(methodOn(BusinessUnitKeyResultController.class).findAll(buoId)).withRel("businessUnitKeyResults"));
            return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
