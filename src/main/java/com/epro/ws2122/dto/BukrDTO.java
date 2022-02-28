package com.epro.ws2122.dto;

import com.epro.ws2122.domain.BusinessUnitKeyResult;
import com.epro.ws2122.domain.BusinessUnitObjective;
import lombok.Data;

@Data
public class BukrDTO {
    private String name;
    private double current;
    private double goal;
    private double confidence;

    public BusinessUnitKeyResult toBukrEntity() {
        var entity = new BusinessUnitKeyResult();
        entity.setConfidence(confidence);
        entity.setCurrent(current);
        entity.setGoal(goal);
        entity.setName(name);

        return entity;
    }

    public BusinessUnitKeyResult toBukrEntity(long id, BusinessUnitObjective buo) {
        var entity = toBukrEntity();
        entity.setId(id);
        entity.setBusinessUnitObjective(buo);
        return entity;
    }
}
