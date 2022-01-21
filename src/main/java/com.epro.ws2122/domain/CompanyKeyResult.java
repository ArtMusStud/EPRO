package com.epro.ws2122.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@SuperBuilder
@NoArgsConstructor
@Getter
@Entity
public class CompanyKeyResult extends KeyResult {
}
