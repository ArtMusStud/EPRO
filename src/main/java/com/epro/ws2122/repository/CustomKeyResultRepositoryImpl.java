package com.epro.ws2122.repository;

import com.epro.ws2122.domain.KeyResult;
import com.epro.ws2122.domain.KeyResultHistory;
import lombok.Setter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

public class CustomKeyResultRepositoryImpl implements CustomKeyResultRepository {

    @PersistenceContext
    @Setter
    private EntityManager em;

    @Override
    public KeyResult updateCurrent(long keyResultId, double newCurrent, String comment) {
        var keyResult = em.find(KeyResult.class, keyResultId);
        return updateCurrentAndConfidence(keyResultId, newCurrent, keyResult.getConfidence(), comment);
    }

    @Override
    public KeyResult updateConfidence(long keyResultId, double newConfidence, String comment) {
        var keyResult = em.find(KeyResult.class, keyResultId);
        return updateCurrentAndConfidence(keyResultId, keyResult.getCurrent(), newConfidence, comment);
    }

    @Override
    @Transactional
    public KeyResult updateCurrentAndConfidence(long keyResultId, double newCurrent, double newConfidence, String comment) {
        // fetch KeyResult from db
        var keyResult = em.find(KeyResult.class, keyResultId);

        // create entry into KeyResultHistory
        var krHistory = new KeyResultHistory();
        var lastVersion = em.createQuery("select max(krh.version) from KeyResultHistory krh where krh.keyResult = :kr")
                .setParameter("kr", keyResult)
                .getSingleResult();
        int version = lastVersion != null ? ((Integer) lastVersion)+1 : 0;
        krHistory.setKeyResult(keyResult);
        krHistory.setVersion(version);
        krHistory.setOldCurrent(keyResult.getCurrent());
        krHistory.setOldConfidence(keyResult.getConfidence());
        krHistory.setComment(comment);
        em.persist(krHistory);

        // update KeyResult
        keyResult.setCurrent(newCurrent);
        keyResult.setConfidence(newConfidence);
        return em.merge(keyResult);
    }
}
