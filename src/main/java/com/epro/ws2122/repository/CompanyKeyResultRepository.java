package com.epro.ws2122.repository;

import com.epro.ws2122.domain.CompanyKeyResult;
import com.epro.ws2122.domain.CompanyObjective;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyKeyResultRepository extends CrudRepository<CompanyKeyResult, Long> {

    List<CompanyKeyResult> findAllByCompanyObjective(CompanyObjective co);
}
