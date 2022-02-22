package com.epro.ws2122;

import com.epro.ws2122.dto.BuoDTO;
import com.epro.ws2122.repository.BusinessUnitObjectiveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
public class BusinessUnitObjectiveIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BusinessUnitObjectiveRepository repository;

    @WithMockUser(roles = {"BUO OKR Admin"})
    @Test
    public void should_create_new_buo() throws Exception {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());

        var buoDTO = new BuoDTO();
        buoDTO.setName("Business Unit Objective 0");
        buoDTO.setStartDate(LocalDate.of(2021, 2, 22));

        var postValue = OBJECT_MAPPER.writeValueAsString(buoDTO);

        this.mockMvc.perform(
                        post("/business-unit-objectives")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content(postValue))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}