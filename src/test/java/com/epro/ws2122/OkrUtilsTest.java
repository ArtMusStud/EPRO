package com.epro.ws2122;

import com.epro.ws2122.domain.CompanyKeyResult;
import com.epro.ws2122.domain.KeyResult;
import com.epro.ws2122.util.OkrUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OkrUtilsTest {

    private final double[][] currentsAndGoals = {
            {0.1, 1.0},
            {0.5, 0.7},
            {12.5, 20.1}
    };

    @Test
    void testCalculateOverall() {
        List<KeyResult> krs = new ArrayList<>();
        Arrays.stream(currentsAndGoals).forEach( pair -> {
            KeyResult kr = new CompanyKeyResult();
            kr.setCurrent(pair[0]);
            kr.setGoal(pair[1]);
            krs.add(kr);
        });
        assertEquals(0.479, OkrUtils.calculateOverall(krs), 0.001);
    }
}
