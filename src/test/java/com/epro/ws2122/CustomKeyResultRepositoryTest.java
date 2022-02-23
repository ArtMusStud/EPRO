package com.epro.ws2122;

import com.epro.ws2122.domain.CompanyKeyResult;
import com.epro.ws2122.domain.KeyResult;
import com.epro.ws2122.repository.CompanyKeyResultRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CustomKeyResultRepositoryTest {

    private CompanyKeyResultRepository repo;
    @Autowired
    public void setRepo(CompanyKeyResultRepository repo) {
        this.repo = repo;
    }

    private static final String CKR_NAME = "Test Company Key Result";
    private static final double INIT_CURRENT = 0.0;
    private static final double INIT_GOAL = 12.0;
    private static final double INIT_CONFIDENCE = 0.5;

    private static final double[] CURRENT_UPDATES = { 1.0, 5.0, 13.0 };
    private static final double[] CONFIDENCE_UPDATES = { 0.4, 0.6, 1.0 };
    private static final String[] UPDATE_COMMENTS = {
            "first comment",
            "second comment",
            "third comment"
    };

    private static Long CKR_ID;

    @BeforeAll
    static void initValue(@Autowired CompanyKeyResultRepository repo) {
        var ckr = new CompanyKeyResult();
        ckr.setName(CKR_NAME);
        ckr.setCurrent(INIT_CURRENT);
        ckr.setGoal(INIT_GOAL);
        ckr.setConfidence(INIT_CONFIDENCE);
        ckr = repo.save(ckr);
        CKR_ID = ckr.getId();
    }

    @Test
    void returnCorrectEntity() {
        assertEquals(repo.findById(CKR_ID).get(), repo.updateCurrent(CKR_ID, 0.0, "comment"));
        assertEquals(repo.findById(CKR_ID).get(), repo.updateConfidence(CKR_ID, 0.0, "comment"));
        assertEquals(repo.findById(CKR_ID).get(),
                repo.updateCurrentAndConfidence(CKR_ID, 0.0, 0.0, "comment"));
    }

    @Test
    void updateCurrent() {
        double newCurrent = CURRENT_UPDATES[0];
        assertEquals(newCurrent, repo.updateCurrent(CKR_ID, newCurrent, "comment").getCurrent());
        assertEquals(newCurrent, repo.findById(CKR_ID).get().getCurrent());
    }

    @Test
    void updateConfidence() {
        double newConfidence = CONFIDENCE_UPDATES[0];
        assertEquals(newConfidence, repo.updateConfidence(CKR_ID, newConfidence, "comment").getConfidence());
        assertEquals(newConfidence, repo.findById(CKR_ID).get().getConfidence());
    }

    @Test
    void updateCurrentAndConfidence() {
        double newCurrent = CURRENT_UPDATES[0];
        double newConfidence = CONFIDENCE_UPDATES[0];

        KeyResult updatedCkr = repo.updateCurrentAndConfidence(CKR_ID, newCurrent, newConfidence, "comment");
        assertEquals(newCurrent, updatedCkr.getCurrent());
        assertEquals(newConfidence, updatedCkr.getConfidence());

        updatedCkr = repo.findById(CKR_ID).get();
        assertEquals(newCurrent, updatedCkr.getCurrent());
        assertEquals(newConfidence, updatedCkr.getConfidence());
    }

    @Test
    void historyCount() {
        KeyResult ckr = repo.findById(CKR_ID).get();
        assertTrue(ckr.getHistory().isEmpty());

        IntStream.range(1, 5).forEach( i -> {
                repo.updateCurrent(CKR_ID, 0.0, "comment");
                assertEquals(i, ckr.getHistory().size());
        });
    }

    @Test
    void historyReference() {
        KeyResult ckr = repo.findById(CKR_ID).get();
        IntStream.range(1, 5).forEach( i -> {
            repo.updateCurrent(CKR_ID, 0.0, "comment");
        });
        ckr.getHistory().forEach( h -> assertEquals(ckr, h.getKeyResult()));
    }

    @Test
    void historyCurrent() {
        KeyResult ckr = repo.findById(CKR_ID).get();
        List<Double> expectedOldCurrents = new ArrayList<>();
        Arrays.stream(CURRENT_UPDATES).forEach( c -> {
            expectedOldCurrents.add(ckr.getCurrent());
            repo.updateCurrent(CKR_ID, c, "comment");
        });
        List<Double> actualOldCurrents = ckr.getHistory().stream()
                .mapToDouble( h -> h.getOldCurrent())
                .boxed()
                .collect(toList());
        assertTrue(actualOldCurrents.containsAll(expectedOldCurrents));
        assertTrue(expectedOldCurrents.containsAll(actualOldCurrents));
    }

    @Test
    void historyConfidence() {
        KeyResult ckr = repo.findById(CKR_ID).get();
        List<Double> expectedOldConfidences = new ArrayList<>();
        Arrays.stream(CONFIDENCE_UPDATES).forEach( c -> {
            expectedOldConfidences.add(ckr.getConfidence());
            repo.updateConfidence(CKR_ID, c, "comment");
        });
        List<Double> actualOldConfidences = ckr.getHistory().stream()
                .mapToDouble( h -> h.getOldConfidence())
                .boxed()
                .collect(toList());
        assertTrue(actualOldConfidences.containsAll(expectedOldConfidences));
        assertTrue(expectedOldConfidences.containsAll(actualOldConfidences));
    }

    @Test
    void historyCurrentAndConfidence() {
        KeyResult ckr = repo.findById(CKR_ID).get();
        List<Double> expectedOldCurrents = new ArrayList<>();
        List<Double> expectedOldConfidences = new ArrayList<>();
        int n = Integer.min(CURRENT_UPDATES.length, CONFIDENCE_UPDATES.length);
        IntStream.range(0, n-1).forEach( i -> {
            expectedOldCurrents.add(ckr.getCurrent());
            expectedOldConfidences.add(ckr.getConfidence());
            repo.updateCurrentAndConfidence(CKR_ID, CURRENT_UPDATES[i], CONFIDENCE_UPDATES[i], "comment");
        });
        List<Double> actualOldCurrents = ckr.getHistory().stream()
                .mapToDouble( h -> h.getOldCurrent())
                .boxed()
                .collect(toList());
        List<Double> actualOldConfidences = ckr.getHistory().stream()
                .mapToDouble( h -> h.getOldConfidence())
                .boxed()
                .collect(toList());

        assertTrue(actualOldCurrents.containsAll(expectedOldCurrents));
        assertTrue(expectedOldCurrents.containsAll(actualOldCurrents));
        assertTrue(actualOldConfidences.containsAll(expectedOldConfidences));
        assertTrue(expectedOldConfidences.containsAll(actualOldConfidences));
    }

    @Test
    void historyComment() {
        KeyResult ckr = repo.findById(CKR_ID).get();
        Arrays.stream(UPDATE_COMMENTS).forEach( c -> repo.updateConfidence(CKR_ID, 0.5, c));
        List<String> actualComments = ckr.getHistory().stream()
                .map( h -> h.getComment() )
                .collect(toList());
        assertTrue(actualComments.containsAll(Arrays.asList(UPDATE_COMMENTS)));
    }

    @Test
    void historyVersionNumber() {
        KeyResult ckr = repo.findById(CKR_ID).get();
        int n = 5;
        List<Integer> expectedVersions = IntStream.range(0, 5).boxed().collect(toList());
        IntStream.range(0, n).forEach( i ->
                repo.updateCurrentAndConfidence(CKR_ID, 0.0, 0.5, "comment")
        );
        List<Integer> actualVersions = ckr.getHistory().stream()
                .mapToInt( h -> h.getVersion())
                .boxed()
                .collect(toList());

        assertTrue(actualVersions.containsAll(expectedVersions));
        assertTrue(expectedVersions.containsAll(actualVersions));
    }
}
