package com.epro.ws2122;

import com.epro.ws2122.domain.BusinessUnitObjective;
import com.epro.ws2122.dto.BukrDTO;
import com.epro.ws2122.repository.BusinessUnitObjectiveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BusinessUnitKeyResultIT {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BusinessUnitObjectiveRepository repository;

    @BeforeEach
    public void initializeData() {
        var buo = BusinessUnitObjective.builder()
                .id(0L)
                .name("Business Unit Objective 0")
                .startDate(LocalDate.of(1970, 1, 19))
                .businessUnitKeyResults(null)
                .build();

        repository.save(buo);
    }

    @WithMockUser(roles = {"CO OKR Admin"})
    @Test
    public void should_create_new_bukr() throws Exception {
        var bukrDTO = new BukrDTO();
        bukrDTO.setName("Business Unit Key Result 30");
        bukrDTO.setConfidence(3.9);
        bukrDTO.setCurrent(10);
        bukrDTO.setGoal(100);

        var postValue = OBJECT_MAPPER.writeValueAsString(bukrDTO);

        this.mockMvc.perform(
                        post("/business-unit-objectives/0/business-unit-key-results")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(postValue))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}