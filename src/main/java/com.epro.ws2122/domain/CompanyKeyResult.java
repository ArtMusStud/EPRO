package com.epro.ws2122.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Entity
public class CompanyKeyResult extends KeyResult {

    @ManyToOne
    private CompanyObjective companyObjective;

    @OneToMany
    private List<BusinessUnitKeyResult> businessUnitKeyResults;
}
