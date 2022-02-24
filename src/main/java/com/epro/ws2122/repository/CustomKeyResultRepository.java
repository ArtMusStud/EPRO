package com.epro.ws2122.repository;

import com.epro.ws2122.domain.KeyResult;
import com.epro.ws2122.dto.KrUpdateDTO;

/**
 * Custom repository for updating {@code KeyResult} information.
 */
public interface CustomKeyResultRepository {

    /**
     * Update the current value of a {@code KeyResult} including a mandatory update comment.
     * @param keyResultId id of the {@code KeyResult}
     * @param newCurrent new current value
     * @param comment mandatory comment
     * @return updated {@code KeyResult}
     */
    public KeyResult updateCurrent(long keyResultId, double newCurrent, String comment);

    /**
     * Update the confidence level of a {@code KeyResult} including a mandatory update comment.
     * @param keyResultId id of the {@code KeyResult}
     * @param newConfidence new confidence level
     * @param comment mandatory comment
     * @return updated {@code KeyResult}
     */
    public KeyResult updateConfidence(long keyResultId, double newConfidence, String comment);

    /**
     * Update the current value and confidence level of a {@code KeyResult} including a mandatory update comment.
     * @param keyResultId id of the {@code KeyResult}
     * @param newCurrent new current value
     * @param newConfidence new confidence levle
     * @param comment mandatory comment
     * @return updated {@code KeyResult}
     */
    public KeyResult updateCurrentAndConfidence(long keyResultId, double newCurrent, double newConfidence, String comment);

    /**
     * Update the current value and confidence level of a {@code KeyResult} through a {@code KrUpdateDTO}.
     * {@code null} values shouldn't be updated. If both new current and new confidence are {@code null}, the update
     * should be discarded completely.
     * @param keyResultId id of the {@code KeyResult}
     * @param keyResultUpdate DTO containing updated data and comment
     * @return updated {@code KeyResult}
     */
    public KeyResult updateWithDto(long keyResultId, KrUpdateDTO keyResultUpdate);
}
