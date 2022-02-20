package com.epro.ws2122;

import com.epro.ws2122.domain.BusinessUnitObjective;
import com.epro.ws2122.dto.BukrDTO;
import com.epro.ws2122.dto.CkrDTO;
import com.epro.ws2122.dto.CoDTO;
import com.epro.ws2122.repository.BusinessUnitObjectiveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                .startDate(LocalDate.of(1970, 1, 23))
                .businessUnitKeyResults(null)
                .build();

        repository.save(buo);
    }

    @WithMockUser(roles = {"BUO OKR Admin"})
    @Test
    @Transactional
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

    @WithMockUser(roles = {"CO OKR Admin", "BUO OKR Admin"})
    @Test
    @Transactional
    public void should_link_new_bukr_with_ckr() throws Exception {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());

        //save new co
        var coDTO = new CoDTO();
        coDTO.setName("Company Objective 0");
        coDTO.setStartDate(LocalDate.of(2021, 2, 19));

        var coPostValue = OBJECT_MAPPER.writeValueAsString(coDTO);

        this.mockMvc.perform(
                        post("/company-objectives")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(coPostValue))
                .andExpect(status().isCreated());

        //save new ckr
        var ckrDTO = new CkrDTO();
        ckrDTO.setName("Company Key Result 0");
        ckrDTO.setConfidence(100);
        ckrDTO.setCurrent(99);
        ckrDTO.setGoal(100);

        var ckrPostValue = OBJECT_MAPPER.writeValueAsString(ckrDTO);

        this.mockMvc.perform(
                        post("/company-objectives/2/company-key-results")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(ckrPostValue))
                .andExpect(status().isCreated());

        //save new bukr
        var bukrDTO = new BukrDTO();
        bukrDTO.setName("Business Unit Key Result 30");
        bukrDTO.setConfidence(3.9);
        bukrDTO.setCurrent(10);
        bukrDTO.setGoal(100);

        var bukrPostValue = OBJECT_MAPPER.writeValueAsString(bukrDTO);

        this.mockMvc.perform(
                        post("/business-unit-objectives/1/business-unit-key-results")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(bukrPostValue))
                .andExpect(status().isCreated());

        //link bukr with ckr
        this.mockMvc.perform(
                        post("/business-unit-objectives/1/business-unit-key-results/4/link")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content("{\"id\": 3}"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}