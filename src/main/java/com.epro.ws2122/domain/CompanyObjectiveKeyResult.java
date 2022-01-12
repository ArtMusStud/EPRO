package com.epro.ws2122.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/*
    Todo: implement properties
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CompanyObjectiveKeyResult {

    @Id
    private Long id;
}
