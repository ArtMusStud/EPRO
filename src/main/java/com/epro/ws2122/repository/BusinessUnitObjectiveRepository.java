package com.epro.ws2122.repository;

import com.epro.ws2122.domain.BusinessUnitObjective;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessUnitObjectiveRepository extends CrudRepository<BusinessUnitObjective, Long> {
}
