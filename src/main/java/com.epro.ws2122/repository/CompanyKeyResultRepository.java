package com.epro.ws2122.repository;

import com.epro.ws2122.domain.CompanyKeyResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyKeyResultRepository extends CrudRepository<CompanyKeyResult, Long> {
}
