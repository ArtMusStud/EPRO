package com.epro.ws2122.repository;

import com.epro.ws2122.domain.CompanyObjective;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyObjectiveRepository extends CrudRepository<CompanyObjective, Long> {
}
