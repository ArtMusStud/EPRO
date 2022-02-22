package com.epro.ws2122.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class BusinessUnitKeyResult extends KeyResult {

    @ManyToOne
    private CompanyKeyResult companyKeyResult;

    @ManyToOne
    private BusinessUnitObjective businessUnitObjective;
}
