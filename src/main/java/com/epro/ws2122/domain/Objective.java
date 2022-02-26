package com.epro.ws2122.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Objective {

    private static final long DURATION_IN_DAYS = 90;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private String name;
    private LocalDate startDate;

    public LocalDate getEndDate() {
        return startDate.plusDays(DURATION_IN_DAYS);
    }

    public boolean isActive() {
        var today = LocalDate.now();
        return today.isAfter(startDate) &&
                today.isBefore(getEndDate());
    }

    public boolean isDone() {
        var today = LocalDate.now();
        return today.isAfter(getEndDate());
    }

    public abstract double getOverall();
}
