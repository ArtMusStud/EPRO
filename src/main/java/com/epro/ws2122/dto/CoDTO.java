package com.epro.ws2122.dto;

import com.epro.ws2122.domain.CompanyObjective;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CoDTO {
    private String name;
    private LocalDate startDate;

    public CompanyObjective toCoEntity() {
        var entity = new CompanyObjective();
        entity.setName(name);
        entity.setStartDate(startDate);

        return entity;
    }
}
