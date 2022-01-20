package com.epro.ws2122.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@SuperBuilder
@NoArgsConstructor
@Entity
public class BusinessUnitKeyResult extends KeyResult {

    @Getter
    @Setter
    @OneToOne
    private CompanyKeyResult companyKeyResult;
}
