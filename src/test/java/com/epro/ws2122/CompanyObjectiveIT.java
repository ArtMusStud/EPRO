package com.epro.ws2122;

import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.dto.CkrDTO;
import com.epro.ws2122.dto.CoDTO;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CompanyObjectiveIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(roles = {"CO OKR Admin"})
    @Test
    public void should_create_new_co() throws Exception {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());

        var coDTO = new CoDTO();
        coDTO.setName("Company Objective 0");
        coDTO.setStartDate(LocalDate.of(2021, 2, 19));

        var postValue = OBJECT_MAPPER.writeValueAsString(coDTO);

        this.mockMvc.perform(
                        post("/company-objectives")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(postValue))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @WithMockUser(roles = {"CO OKR Admin"})
    @Test
    public void should_create_multiple_co() throws Exception {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());

        var fstCoDTO = new CoDTO();
        fstCoDTO.setName("Company Objective 0");
        fstCoDTO.setStartDate(LocalDate.of(2021, 2, 19));

        var fstPostValue = OBJECT_MAPPER.writeValueAsString(fstCoDTO);

        var secCoDTO = new CoDTO();
        secCoDTO.setName("Company Objective 0");
        secCoDTO.setStartDate(LocalDate.of(2021, 2, 19));

        var secPostValue = OBJECT_MAPPER.writeValueAsString(secCoDTO);

        this.mockMvc.perform(
                        post("/company-objectives")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(fstPostValue))
                .andExpect(status().isCreated());

        this.mockMvc.perform(
                        post("/company-objectives")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(secPostValue))
                .andExpect(status().isCreated());
    }
}