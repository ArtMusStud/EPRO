package com.epro.ws2122;

import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.dto.CkrDTO;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CompanyKeyResultIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CompanyObjectiveRepository repository;

    @BeforeEach
    public void initializeData() {
        var co = CompanyObjective.builder()
                .name("Company Objective 0")
                .startDate(LocalDate.of(1970, 1, 19))
                .companyKeyResults(null)
                .build();

        repository.save(co);
    }

    @WithMockUser(roles = {"CO OKR Admin"})
    @Test
    @Transactional
    public void should_create_new_ckr() throws Exception {
        var ckrDTO = new CkrDTO();
        ckrDTO.setName("CompanyKeyResult 99");
        ckrDTO.setConfidence(50);
        ckrDTO.setCurrent(2);
        ckrDTO.setGoal(8);

        var postValue = OBJECT_MAPPER.writeValueAsString(ckrDTO);

        this.mockMvc.perform(
                        post("/company-objectives/1/company-key-results")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(postValue))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
