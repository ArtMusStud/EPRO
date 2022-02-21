package com.epro.ws2122.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
public class KeyResultHistory {

    @Id
    @EqualsAndHashCode.Include
    private long id;

    @ManyToOne
    private KeyResult keyResult;
    private int version = 0;

    private double oldCurrent;
    private double oldConfidence;

    private String comment;
}
