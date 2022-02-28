package com.epro.ws2122.repository;

import com.epro.ws2122.domain.KeyResultHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeyResultHistoryRepository extends CrudRepository<KeyResultHistory, Long> {
    List<KeyResultHistory> findAllByKeyResultId(long id);
}
