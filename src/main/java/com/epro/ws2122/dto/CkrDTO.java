package com.epro.ws2122.dto;

import com.epro.ws2122.domain.CompanyKeyResult;
import lombok.Data;

@Data
public class CkrDTO {
    private String name;
    private double current;
    private double goal;
    private double confidence;

    public CompanyKeyResult toCkrEntity() {
        CompanyKeyResult ckr = new CompanyKeyResult();
        ckr.setName(name);
        ckr.setCurrent(current);
        ckr.setGoal(goal);
        ckr.setConfidence(confidence);

        return ckr;
    }
}
