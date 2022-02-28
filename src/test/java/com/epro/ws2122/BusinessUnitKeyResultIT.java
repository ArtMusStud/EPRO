package com.epro.ws2122;

import com.epro.ws2122.domain.BusinessUnitKeyResult;
import com.epro.ws2122.domain.BusinessUnitObjective;
import com.epro.ws2122.dto.BukrDTO;
import com.epro.ws2122.dto.CkrDTO;
import com.epro.ws2122.dto.CoDTO;
import com.epro.ws2122.dto.KrUpdateDTO;
import com.epro.ws2122.repository.BusinessUnitKeyResultRepository;
import com.epro.ws2122.repository.BusinessUnitObjectiveRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BusinessUnitKeyResultIT {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BusinessUnitObjectiveRepository buoRepository;
    @Autowired
    BusinessUnitKeyResultRepository bukrRepository;

    @BeforeEach
    public void initializeData() {
        var buo = BusinessUnitObjective.builder()
                .name("Business Unit Objective 0")
                .startDate(LocalDate.of(1970, 1, 23))
                .businessUnitKeyResults(null)
                .build();

        buoRepository.save(buo);
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
                        post("/business-unit-objectives/1/business-unit-key-results")
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
                        post("/business-unit-objectives/1/business-unit-key-results/2/link")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                                .content("{\"id\": 1}"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = {"CO OKR Admin", "BUO OKR Admin"})
    @Test
    @Transactional
    public void should_change_bukr() throws Exception {
        var buo = new BusinessUnitObjective();
        buo.setStartDate(LocalDate.of(2022, 2, 1));
        buo.setName("Business Unit Objective 0");

        var bukr = new BusinessUnitKeyResult();
        bukr.setName("Business Unit Key Result 0");
        bukr.setCurrent(0);
        bukr.setGoal(100);
        bukr.setConfidence(10);
        bukr.setBusinessUnitObjective(buo);

        buoRepository.save(buo);
        bukrRepository.save(bukr);

        OBJECT_MAPPER.registerModule(new JavaTimeModule());

        var krupdate0 = new KrUpdateDTO();
        krupdate0.setConfidence(20d);
        krupdate0.setCurrent(10d);
        krupdate0.setComment("1. Update...");

        var krupdate0PostData = OBJECT_MAPPER.writeValueAsString(krupdate0);

        this.mockMvc.perform(
                        post("/business-unit-objectives/1/business-unit-key-results/1/changes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(krupdate0PostData)
                                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        var krupdate1 = new KrUpdateDTO();
        krupdate1.setConfidence(90d);
        krupdate1.setCurrent(95d);
        krupdate1.setComment("2. Update...");

        var krupdate1PostData = OBJECT_MAPPER.writeValueAsString(krupdate1);

        this.mockMvc.perform(
                        post("/business-unit-objectives/1/business-unit-key-results/1/changes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(krupdate1PostData)
                                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(
                        get("/business-unit-objectives/1/business-unit-key-results/1/changes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.keyResultHistory", hasSize(2)))
                .andExpect(jsonPath("$._embedded.keyResultHistory[0].oldCurrent", is(0.0)))
                .andExpect(jsonPath("$._embedded.keyResultHistory[0].oldConfidence", is(10.0)))
                .andExpect(jsonPath("$._embedded.keyResultHistory[0].comment", is("1. Update...")))
                .andExpect(jsonPath("$._embedded.keyResultHistory[1].oldCurrent", is(10.0)))
                .andExpect(jsonPath("$._embedded.keyResultHistory[1].oldConfidence", is(20.0)))
                .andExpect(jsonPath("$._embedded.keyResultHistory[1].comment", is("2. Update...")))                ;
    }
}