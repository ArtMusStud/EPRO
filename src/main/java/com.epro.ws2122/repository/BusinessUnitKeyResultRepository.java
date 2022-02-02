package com.epro.ws2122.repository;

import com.epro.ws2122.domain.BusinessUnitKeyResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessUnitKeyResultRepository extends CrudRepository<BusinessUnitKeyResult, Long> {
}
