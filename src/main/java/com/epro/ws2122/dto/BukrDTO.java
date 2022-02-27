package com.epro.ws2122.dto;

import com.epro.ws2122.domain.BusinessUnitKeyResult;
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

    public BusinessUnitKeyResult toReplacedBukrEntity(long id) {
        var entity = toBukrEntity();
        entity.setId(id);
        return entity;
    }
}
