package com.epro.ws2122.controller;

import com.epro.ws2122.domain.CompanyKeyResult;
import com.epro.ws2122.dto.CkrDTO;
import com.epro.ws2122.dto.KrUpdateDTO;
import com.epro.ws2122.model.CompanyKeyResultModel;
import com.epro.ws2122.model.CompanyObjectiveSubresourceModel;
import com.epro.ws2122.repository.CompanyKeyResultRepository;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import com.epro.ws2122.util.JsonPatcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * A {@link RestController} for processing http client requests sent to the templated uri path <br><b>/company-objectives/{coId}/company-key-results/{id}</b>.
 * <p>
 * The path contains the variables {@code coId} and {@code id}:
 * <ul>
 * <li>{@code coId} is mandatory and refers to the id of the company objective resource from which company key result resources are being requested.</li>
 * <li>{@code id} relates to the id of the requested company key result.</li>
 * </ul><p>
 * Client requests that will be processed are:
 * <ul>
 * <li>{@link #findOne(long, long) GET} for a single resource</li>
 * <li>{@link #findAll(long) GET} for a collection resource</li>
 * <li>{@link #update(JsonPatch, long, long) PATCH}</li>
 * <li>{@link #replace(CkrDTO, long, long) PUT}</li>
 * <li>{@link #create(CkrDTO, long) POST}</li>
 * <li>{@link #delete(long, long) DELETE}</li>
 * </ul>
 */
@RestController
@RequestMapping("/company-objectives/{coId}/company-key-results")
@RequiredArgsConstructor
public class CompanyKeyResultController {

    /**
     * Repository from which to retrieve entities of type {@link com.epro.ws2122.domain.CompanyObjective CompanyObjective}.
     */
    private final CompanyKeyResultRepository ckrRepository;
    /**
     * Repository from which to retrieve entities of type {@link com.epro.ws2122.domain.CompanyKeyResult CompanyKeyResult}.
     */
    private final CompanyObjectiveRepository coRepository;

    /**
     * Patcher for regular patch requests
     */
    private final JsonPatcher<CkrDTO> patcher;

    /**
     * Patcher for current and confidence updates with comment, that should log into history
     */
    private final JsonPatcher<KrUpdateDTO> updatePatcher;

    private final ModelMapper modelMapper;

    /**
     * Returns a company key result, depending on whether the uri path leads to an obtainable resource, along with an HTTP status code.
     * <p>
     * The data of a returned company key result resource is defined in the DTO class {@link CompanyKeyResultModel}.
     * <p>
     * HTTP status codes returned:
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
        var coOptional = coRepository.findById(coId);
        var ckrOptional = ckrRepository.findById(id);
        if (ckrOptional.isPresent() && coOptional.isPresent()) {
            var ckr = ckrOptional.get();
            var co = coOptional.get();
            var ckrResource = new CompanyKeyResultModel(ckr);

            var halModelBuilder = HalModelBuilder.halModelOf(ckrResource)
                    .embed(new CompanyObjectiveSubresourceModel(co))
                    .link(linkTo(methodOn(CompanyKeyResultController.class).findOne(coId, id)).withSelfRel()
                            .andAffordance(afford(methodOn(CompanyKeyResultController.class).replace(null, coId, id)))
                            .andAffordance(afford(methodOn(CompanyKeyResultController.class).update(null, coId, id)))
                            .andAffordance(afford(methodOn(CompanyKeyResultController.class).delete(coId, id))))
                    .link(linkTo(methodOn(CompanyKeyResultController.class).findAll(coId)).withRel("companyKeyResults"));

            return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Returns a collection resource of multiple company key results, depending on whether the uri path leads to obtainable resources, along with an HTTP status code.
     * <p>
     * The properties of each of the returned company key result resources are defined in the DTO {@link CompanyKeyResultModel}.
     * <p>
     * HTTP status codes returned:
     * <ul>
     *     <li>{@code 200} if the collection resource can be obtained.
     *     <li>{@code 404} if {@code coId} doesn't lead to an existing resource.
     * </ul>
     * @param coId id of the company objective
     * @return company key result collection resource or an empty list, and an HTTP status code.
     */
    /*
    ToDo:
      - missing collection resource BUKR and/or BUO
     */
    @GetMapping
    public ResponseEntity<CollectionModel<CompanyKeyResultModel>> findAll(@PathVariable long coId) {
        var coOptional = coRepository.findById(coId);
        if (coOptional.isPresent()) {
            var co = coOptional.get();
            var ckrModels = ckrRepository
                    .findAllByCompanyObjective(co)
                    .stream()
                    .map(ckr -> new CompanyKeyResultModel(ckr).add(
                            linkTo((methodOn(CompanyKeyResultController.class)
                                    .findOne(coId, ckr.getId()))).withSelfRel()
                                    .andAffordance(afford(methodOn(CompanyKeyResultController.class).replace(null, coId, ckr.getId())))
                                    .andAffordance(afford(methodOn(CompanyKeyResultController.class).update(null, coId, ckr.getId())))
                                    .andAffordance(afford(methodOn(CompanyKeyResultController.class).delete(coId, ckr.getId())))))
                    .collect(Collectors.toList());

            var ckrResource = CollectionModel.of(
                    ckrModels,
                    linkTo(methodOn(CompanyKeyResultController.class).findAll(coId)).withSelfRel()
                            .andAffordance(afford(methodOn(CompanyKeyResultController.class).create(null, coId))),
                    linkTo(methodOn(CompanyObjectiveController.class).findOne(coId)).withRel("companyObjective"));

            return new ResponseEntity<>(ckrResource, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /*
    Todo:
        - hateoas?
    */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long coId, @PathVariable("id") long id) {
        if (!ckrRepository.existsById(id)) return ResponseEntity.notFound().build();
        ckrRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CkrDTO ckrDTO, @PathVariable long coId) {
        var coOptional = coRepository.findById(coId);
        if (coOptional.isPresent()) {
            var co = coOptional.get();
            var ckr = ckrDTO.toCkrEntity();
            ckr.setCompanyObjective(co);
       //     co.getCompanyKeyResults().add(ckr);
            ckr = ckrRepository.save(ckr);
            co = coRepository.save(co);
            var ckrResource = new CompanyKeyResultModel(ckr);
            var halModelBuilder = HalModelBuilder.halModelOf(ckrResource)
                    .embed(new CompanyObjectiveSubresourceModel(co))
                    .link(linkTo(methodOn(CompanyKeyResultController.class).findOne(coId, ckr.getId())).withSelfRel()
                            .andAffordance(afford(methodOn(CompanyKeyResultController.class).replace(null, coId, ckr.getId())))
                            .andAffordance(afford(methodOn(CompanyKeyResultController.class).update(null, coId, ckr.getId())))
                            .andAffordance(afford(methodOn(CompanyKeyResultController.class).delete(coId, ckr.getId()))))
                    .link(linkTo(methodOn(CompanyKeyResultController.class).findAll(coId)).withRel("companyKeyResults"));

            return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /*
    Todo:
        - hateoas
    */
    @PutMapping("/{id}")
    public ResponseEntity<?> replace(@RequestBody CkrDTO ckrDTO, @PathVariable long coId, @PathVariable("id") long id) {
        var ckrOpt = ckrRepository.findById(id);
        if (ckrOpt.isEmpty()) return ResponseEntity.notFound().build();
        var ckr = ckrOpt.get();
        ckr = ckrRepository.save(ckrDTO.toCkrEntity(id, ckr.getCompanyObjective()));
        return ResponseEntity.ok(new CompanyKeyResultModel(ckr));

    }

    /*
    Todo:
        - hateoas
    */
    @PatchMapping(value = "/{id}", consumes = JsonPatcher.MEDIATYPE)
    public ResponseEntity<?> update(@RequestBody JsonPatch patch, @PathVariable long coId, @PathVariable("id") long id) {
        var ckrOpt = ckrRepository.findById(id);
        if (ckrOpt.isEmpty()) return ResponseEntity.notFound().build();
        var ckr = ckrOpt.get();
        var ckrDTO = modelMapper.map(ckr, CkrDTO.class);
        try {
            ckrDTO = patcher.applyPatch(ckrDTO, patch);
        } catch (JsonPatchException | JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        ckr = ckrRepository.save(ckrDTO.toCkrEntity(id, ckr.getCompanyObjective()));
        return ResponseEntity.ok(new CompanyKeyResultModel(ckr));

    }

    @PatchMapping(value = "{id}/update", consumes = JsonPatcher.MEDIATYPE)
    public ResponseEntity<?> updateWithComment(@RequestBody JsonPatch patch, @PathVariable String coId, @PathVariable long id)
            throws JsonPatchException, JsonProcessingException {
        KrUpdateDTO update = updatePatcher.applyPatch(new KrUpdateDTO(), patch);
        return ResponseEntity.status(200)
                .body(new CompanyKeyResultModel((CompanyKeyResult) ckrRepository.updateWithDto(id, update)));
    }
}
