package com.epro.ws2122.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Entity;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CompanyObjective {

    @Id
    private Long id;
    private String name;
    @JsonFormat(shape=JsonFormat.Shape.NUMBER)
    private Date createdAt;
    private double overall;
}
