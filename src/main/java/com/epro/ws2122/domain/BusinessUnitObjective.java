package com.epro.ws2122.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class BusinessUnitObjective extends Objective {

    @OneToMany(mappedBy = "businessUnitObjective")
    private List<BusinessUnitKeyResult> businessUnitKeyResults;

    @Override
    public double getOverall() {
        return 0;
    }
}
