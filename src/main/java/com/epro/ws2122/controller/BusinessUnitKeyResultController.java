package com.epro.ws2122.controller;

import com.epro.ws2122.domain.BusinessUnitKeyResult;
import com.epro.ws2122.domain.userRoles.User;
import com.epro.ws2122.dto.BukrDTO;
import com.epro.ws2122.dto.KrUpdateDTO;
import com.epro.ws2122.model.BusinessUnitKeyResultModel;
import com.epro.ws2122.model.BusinessUnitObjectiveSubresourceModel;
import com.epro.ws2122.model.CompanyKeyResultSubresourceModel;
import com.epro.ws2122.model.KeyResultHistoryModel;
import com.epro.ws2122.repository.*;
import com.epro.ws2122.util.JsonPatcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
 * <li>{@link #update(JsonPatch, long, long) PATCH}</li>
 * <li>{@link #replace(BukrDTO, long, long) PUT}</li>
 * <li>{@link #create(BukrDTO, long) POST}</li>
 * <li>{@link #delete(long, long) DELETE}</li>
 * </ul>
 */
@RestController
@RequestMapping("/business-unit-objectives/{buoId}/business-unit-key-results")
@RequiredArgsConstructor
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

    private final KeyResultHistoryRepository keyResultHistoryRepository;

    private final CustomKeyResultRepositoryImpl customKeyResultRepository;

    /**
     * Patcher for regular patch requests
     */
    private final JsonPatcher<BukrDTO> patcher;

    /**
     * Patcher for current and confidence updates with comment, that should log into history
     */
    private final JsonPatcher<KrUpdateDTO> updatePatcher;

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

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
                    .link(linkTo(methodOn(BusinessUnitKeyResultController.class).findOne(buoId, id)).withSelfRel()
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).replace(null, buoId, id)))
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).update(null, buoId, id)))
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).delete(buoId, id))))
                    .link(linkTo(methodOn(BusinessUnitKeyResultController.class).findAll(buoId)).withRel("businessUnitKeyResults"));
            if (assignedCompanyKeyResult != null) {
                halModelBuilder.embed(new CompanyKeyResultSubresourceModel(assignedCompanyKeyResult.getCompanyObjective().getId(), assignedCompanyKeyResult));
            }

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
    public ResponseEntity<CollectionModel<BusinessUnitKeyResultModel>> findAll(@PathVariable long buoId) {
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


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long buoId, @PathVariable("id") long id) {
        var bukrOpt = bukrRepository.findById(id);
        if (bukrOpt.isEmpty()) return ResponseEntity.notFound().build();
        var bukr = bukrOpt.get();
        if (!bukr.getOwner().equals(getAuthenticatedUser())) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        bukrRepository.delete(bukr);
        return ResponseEntity.noContent().build();
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody BukrDTO bukrDTO, @PathVariable long buoId) {
        var buoOptional = buoRepository.findById(buoId);
        if (buoOptional.isPresent()) {
            var buo = buoOptional.get();
            var bukr = bukrDTO.toBukrEntity();
            bukr.setOwner(getAuthenticatedUser());
            bukr.setBusinessUnitObjective(buo);
//            buo.getBusinessUnitKeyResults().add(bukr);
            bukr = bukrRepository.save(bukr);
            buo = buoRepository.save(buo);
            var bukrResource = new BusinessUnitKeyResultModel(bukr);
            var halModelBuilder = HalModelBuilder.halModelOf(bukrResource)
                    .embed(new BusinessUnitObjectiveSubresourceModel(buo))
                    .link(linkTo(methodOn(BusinessUnitKeyResultController.class).findOne(buoId, bukr.getId())).withSelfRel()
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).replace(null, buoId, bukr.getId())))
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).update(null, buoId, bukr.getId())))
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).delete(buoId, bukr.getId()))))
                    .link(linkTo(methodOn(BusinessUnitKeyResultController.class).findAll(buoId)).withRel("businessUnitKeyResults"));

            return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> replace(@RequestBody BukrDTO bukrDTO, @PathVariable long buoId, @PathVariable("id") long id) {
        User authenticatedUser = getAuthenticatedUser();
        var bukrOpt = bukrRepository.findById(id);
        if (bukrOpt.isEmpty()) return ResponseEntity.notFound().build();
        var bukr = bukrOpt.get();
        if (!bukr.getOwner().equals(authenticatedUser)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        bukr = bukrRepository.save(
                bukrDTO.toBukrEntity(id, bukr.getBusinessUnitObjective(), authenticatedUser)
        );
        return ResponseEntity.ok(new BusinessUnitKeyResultModel(bukr));
    }

    @PatchMapping(value = "/{id}", consumes = JsonPatcher.MEDIATYPE)
    public ResponseEntity<?> update(@RequestBody JsonPatch patch, @PathVariable long buoId, @PathVariable("id") long id) {
        User authenticatedUser = getAuthenticatedUser();
        var bukrOpt = bukrRepository.findById(id);
        if (bukrOpt.isEmpty()) return ResponseEntity.notFound().build();
        var bukr = bukrOpt.get();
        if (!bukr.getOwner().equals(authenticatedUser)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        var bukrDto = modelMapper.map(bukr, BukrDTO.class);
        try {
            bukrDto = patcher.applyPatch(bukrDto, patch);
        } catch (JsonPatchException | JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        bukr = bukrRepository.save(
                bukrDto.toBukrEntity(id, bukr.getBusinessUnitObjective(), authenticatedUser)
        );
        return ResponseEntity.ok(new BusinessUnitKeyResultModel(bukr));
    }

    @PostMapping("/{id}/link")
    public ResponseEntity<?> link(@RequestBody Map<String, Long> ckrById, @PathVariable long id, @PathVariable long buoId) {
        var buoOptional = buoRepository.findById(buoId);
        var bukrOptional = bukrRepository.findById(id);
        var ckrOptional = ckrRepository.findById(ckrById.get("id"));
        if (buoOptional.isPresent() && bukrOptional.isPresent() && ckrOptional.isPresent()) {
            var buo = buoOptional.get();
            var bukr = bukrOptional.get();
            if (!bukr.getOwner().equals(getAuthenticatedUser())) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            var ckr = ckrOptional.get();
            var bukrResource = new BusinessUnitKeyResultModel(bukr);

            ckr.getBusinessUnitKeyResults().add(bukr);

            var halModelBuilder = HalModelBuilder.halModelOf(bukrResource)
                    .embed(new BusinessUnitObjectiveSubresourceModel(buo))
                    .embed(new CompanyKeyResultSubresourceModel(ckr.getId(), ckr))
                    .link(linkTo(methodOn(BusinessUnitKeyResultController.class).findOne(buoId, id)).withSelfRel()
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).replace(null, buoId, id)))
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).update(null, buoId, id)))
                            .andAffordance(afford(methodOn(BusinessUnitKeyResultController.class).delete(buoId, id))))
                    .link(linkTo(methodOn(BusinessUnitKeyResultController.class).findAll(buoId)).withRel("businessUnitKeyResults"));
            return new ResponseEntity<>(halModelBuilder.build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = "{id}/update", consumes = JsonPatcher.MEDIATYPE)
    public ResponseEntity<?> updateWithComment(@RequestBody JsonPatch patch, @PathVariable String buoId, @PathVariable long id)
            throws JsonPatchException, JsonProcessingException {
        KrUpdateDTO update = updatePatcher.applyPatch(new KrUpdateDTO(), patch);
        var bukr = (BusinessUnitKeyResult) bukrRepository.updateWithDto(id, update);
        if (!bukr.getOwner().equals(getAuthenticatedUser())) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(200)
                .body(new BusinessUnitKeyResultModel(bukr));
    }

    @PostMapping("/{id}/changes")
    public ResponseEntity<?> updateWithComment(@RequestBody KrUpdateDTO krUpdateDTO, @PathVariable long id, @PathVariable long buoId) {
        var buoOptional = buoRepository.findById(buoId);
        var bukrOptional = bukrRepository.findById(id);
        if (buoOptional.isPresent() && bukrOptional.isPresent()) {
            customKeyResultRepository.updateCurrentAndConfidence(id, krUpdateDTO.getCurrent(),
                    krUpdateDTO.getConfidence(), krUpdateDTO.getComment());
            var bukr = bukrOptional.get();
            if (!bukr.getOwner().equals(getAuthenticatedUser())) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            var bukrResource = new BusinessUnitKeyResultModel(bukr);

            return new ResponseEntity<>(EntityModel.of(bukrResource), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/changes")
    public ResponseEntity<CollectionModel<KeyResultHistoryModel>> getHistory(@PathVariable long id, @PathVariable long buoId) {
        var buoOptional = buoRepository.findById(buoId);
        var bukrOptional = bukrRepository.findById(id);
        if (buoOptional.isPresent() && bukrOptional.isPresent()) {
            var bukr = bukrOptional.get();
            var historyModels = keyResultHistoryRepository
                    .findAllByKeyResultId(bukr.getId())
                    .stream()
                    .map(krh -> new KeyResultHistoryModel(krh))
                    .collect(Collectors.toList());
            var historyResource = CollectionModel.of(
                    historyModels,
                    linkTo(methodOn(BusinessUnitKeyResultController.class).updateWithComment(null, id, buoId)).withRel("changeBusinessUnitKeyResult"),
                    linkTo(methodOn(BusinessUnitKeyResultController.class).findOne(buoId, id)).withRel("businessUnitKeyResult"));
            return new ResponseEntity<>(historyResource, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var username = auth.getName();
        return userRepository.findByUsername(username);
    }
}
