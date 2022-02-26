package com.epro.ws2122.domain;

import com.epro.ws2122.domain.userRoles.User;
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
    private User owner;

    @ManyToOne
    private CompanyKeyResult companyKeyResult;

    @ManyToOne
    private BusinessUnitObjective businessUnitObjective;
}
