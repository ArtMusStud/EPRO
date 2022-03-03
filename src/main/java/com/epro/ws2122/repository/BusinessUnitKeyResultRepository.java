package com.epro.ws2122.repository;

import com.epro.ws2122.domain.BusinessUnitKeyResult;
import com.epro.ws2122.domain.BusinessUnitObjective;
import com.epro.ws2122.domain.CompanyKeyResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessUnitKeyResultRepository extends CrudRepository<BusinessUnitKeyResult, Long>, CustomKeyResultRepository {

    List<BusinessUnitKeyResult> findAllByBusinessUnitObjective(BusinessUnitObjective buo);
    List<BusinessUnitKeyResult> findAllByCompanyKeyResult(CompanyKeyResult ckr);
}
