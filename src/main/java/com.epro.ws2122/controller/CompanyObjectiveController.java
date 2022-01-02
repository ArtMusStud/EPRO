package com.epro.ws2122.controller;

import com.epro.ws2122.assembler.CompanyObjectiveAssembler;
import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyObjectiveController {

    private CompanyObjectiveRepository companyObjectiveRepository;
    private CompanyObjectiveAssembler companyObjectiveAssembler;

    @GetMapping("/company-objectives/{id}")
    public ResponseEntity<CompanyObjective> companyObjectiveById(@PathVariable("id") Long id) {
        return companyObjectiveRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
