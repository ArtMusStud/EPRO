package com.epro.ws2122.dto;

import com.epro.ws2122.domain.BusinessUnitObjective;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BuoDTO {
    private String name;
    private LocalDate startDate;

    public BusinessUnitObjective toBuEntity() {
        var entity = new BusinessUnitObjective();
        entity.setName(name);
        entity.setStartDate(startDate);

        return entity;
    }

    public BusinessUnitObjective toBuEntity(long id) {
        var entity = toBuEntity();
        entity.setId(id);
        return entity;
    }
}
