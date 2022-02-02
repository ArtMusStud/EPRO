package com.epro.ws2122.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class BusinessUnitKeyResult extends KeyResult {

    @OneToOne
    private CompanyKeyResult companyKeyResult;

    @ManyToOne
    private BusinessUnitObjective businessUnitObjective;
}
