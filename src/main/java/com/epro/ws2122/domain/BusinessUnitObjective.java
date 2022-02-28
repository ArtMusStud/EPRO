package com.epro.ws2122.domain;

import com.epro.ws2122.util.OkrUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class BusinessUnitObjective extends Objective {

    @OneToMany(mappedBy = "businessUnitObjective")
    private List<BusinessUnitKeyResult> businessUnitKeyResults = new ArrayList<>();

    @Override
    public double getOverall() {
        return OkrUtils.calculateOverall(businessUnitKeyResults);
    }
}
