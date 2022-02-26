package com.epro.ws2122.util;

import com.epro.ws2122.domain.KeyResult;

import java.util.List;

public class OkrUtils {

    public static double calculateOverall(List<? extends KeyResult> keyResults) {
        return keyResults.stream()
                .mapToDouble( kr -> kr.getCurrent()/ kr.getGoal())
                .average().getAsDouble();
    }
}
