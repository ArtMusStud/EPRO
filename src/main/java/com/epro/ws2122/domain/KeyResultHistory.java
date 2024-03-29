package com.epro.ws2122.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
public class KeyResultHistory {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonIgnore
//    @ToString.Exclude
    private KeyResult keyResult;
    private int version = 0;

    private double oldCurrent;
    private double oldConfidence;

    private String comment;
    private Instant timestamp;
}
