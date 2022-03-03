package com.epro.ws2122.dto;

import com.epro.ws2122.domain.CompanyKeyResult;
import com.epro.ws2122.domain.CompanyObjective;
import lombok.Data;

@Data
public class CkrDTO {
    private String name;
    private double current;
    private double goal;
    private double confidence;

    public CompanyKeyResult toCkrEntity() {
        var entity = new CompanyKeyResult();
        entity.setName(name);
        entity.setCurrent(current);
        entity.setGoal(goal);
        entity.setConfidence(confidence);

        return entity;
    }

    public CompanyKeyResult toCkrEntity(long id, CompanyObjective co) {
        var entity = toCkrEntity();
        entity.setId(id);
        entity.setCompanyObjective(co);
        return entity;
    }
}
