package com.epro.ws2122.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Getter
@Entity
public class BusinessUnitObjective extends Objective {

    @OneToMany
    private List<BusinessUnitKeyResult> businessUnitKeyResults;
}
