package com.epro.ws2122.util;

import com.epro.ws2122.domain.KeyResult;

import java.util.List;

/**
 * <p>Utility class for calculations on entities and other things.</p>
 */
public class OkrUtils {

    /**
     * Calculates the overall progress of a list of {@link KeyResult}s
     * @param keyResults keyresults
     * @return overall progress
     */
    public static double calculateOverall(List<? extends KeyResult> keyResults) {
        return keyResults.stream()
                .mapToDouble( kr -> kr.getCurrent()/ kr.getGoal())
                .average().getAsDouble();
    }
}
