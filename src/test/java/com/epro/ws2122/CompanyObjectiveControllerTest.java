package com.epro.ws2122;

import com.epro.ws2122.assembler.CompanyObjectiveAssembler;
import com.epro.ws2122.controller.CompanyObjectiveController;
import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {CompanyObjectiveController.class, CompanyObjectiveAssembler.class})
public class CompanyObjectiveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CompanyObjectiveRepository mockRepository;

    @BeforeEach
    public void initializeData() {
        var companyObjective_0 = CompanyObjective.builder()
                .id(1L)
                .name("Company Objective 1")
//                .overall(0.75)
                .startDate(LocalDate.of(1970, 1, 19))
                .build();

        var companyObjective_1 = CompanyObjective.builder()
                .id(2L)
                .name("Company Objective 2")
//                .overall(0.1)
                .startDate(LocalDate.of(1970, 1, 12))
                .build();

        Mockito.when(mockRepository.findById(1L)).thenReturn(Optional.of(companyObjective_0));
        Mockito.when(mockRepository.findById(2L)).thenReturn(Optional.of(companyObjective_1));
        Mockito.when(mockRepository.findAll()).thenReturn(Arrays.asList(companyObjective_0, companyObjective_1));
    }

    @Test
    public void should_return_single_company_objective() throws Exception {
        this.mockMvc.perform(get("/company-objectives/1").accept(MediaTypes.HAL_FORMS_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_FORMS_JSON.toString()))
                .andExpect(jsonPath("$.name", is("Company Objective 1")))
//                .andExpect(jsonPath("$.overall", is(0.75)))
                .andExpect(jsonPath("$.startDate[0]", is(1970)))
                .andExpect(jsonPath("$.startDate[1]", is(1)))
                .andExpect(jsonPath("$.startDate[2]", is(19)))
                .andExpect(jsonPath("$._templates.default.method", is("PUT")))
                .andExpect(jsonPath("$._templates.patchCompanyObjective.method", is("PATCH")))
                .andExpect(jsonPath("$._templates.deleteCompanyObjective.method", is("DELETE")))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/company-objectives/1")));
    }

    @Test
    public void should_return_all_company_objectives() throws Exception {
        this.mockMvc.perform(get("/company-objectives"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_FORMS_JSON.toString()))
                .andExpect(jsonPath("$._embedded.companyObjectiveModelList", hasSize(2)))

                .andExpect(jsonPath("$._embedded.companyObjectiveModelList[0].name", is("Company Objective 1")))
                .andExpect(jsonPath("$._embedded.companyObjectiveModelList[0].createdAt", is(1640991600)))
                .andExpect(jsonPath("$._embedded.companyObjectiveModelList[0].overall", is(0.75)))
                .andExpect(jsonPath("$._embedded.companyObjectiveModelList[0]._links.self.href", is("http://localhost/company-objectives/1")))
                .andExpect(jsonPath("$._embedded.companyObjectiveModelList[0]._templates.default.method", is("PUT")))
                .andExpect(jsonPath("$._embedded.companyObjectiveModelList[0]._templates.patchCompanyObjective.method", is("PATCH")))
                .andExpect(jsonPath("$._embedded.companyObjectiveModelList[0]._templates.deleteCompanyObjective.method", is("DELETE")))

                .andExpect(jsonPath("$._embedded.companyObjectiveModelList[1].name", is("Company Objective 2")))
                .andExpect(jsonPath("$._embedded.companyObjectiveModelList[1].createdAt", is(978303600)))
                .andExpect(jsonPath("$._embedded.companyObjectiveModelList[1].overall", is(0.1)))
                .andExpect(jsonPath("$._embedded.companyObjectiveModelList[1]._links.self.href", is("http://localhost/company-objectives/2")))
                .andExpect(jsonPath("$._embedded.companyObjectiveModelList[1]._templates.default.method", is("PUT")))
                .andExpect(jsonPath("$._embedded.companyObjectiveModelList[1]._templates.patchCompanyObjective.method", is("PATCH")))
                .andExpect(jsonPath("$._embedded.companyObjectiveModelList[1]._templates.deleteCompanyObjective.method", is("DELETE")))

                .andExpect(jsonPath("$._links.self.href", is("http://localhost/company-objectives")))
                .andExpect(jsonPath("$._templates.default.method", is("POST")));
    }
}