package com.epro.ws2122.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class CompanyKeyResult extends KeyResult {

    @ManyToOne
    private CompanyObjective companyObjective;

    @OneToMany(mappedBy = "companyKeyResult")
    private List<BusinessUnitKeyResult> businessUnitKeyResults = new ArrayList<>();
}
