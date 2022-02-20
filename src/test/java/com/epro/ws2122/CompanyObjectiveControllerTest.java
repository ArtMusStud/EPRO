package com.epro.ws2122;

import com.epro.ws2122.controller.CompanyObjectiveController;
import com.epro.ws2122.domain.CompanyKeyResult;
import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("ConstantConditions")
@WebMvcTest(controllers = {CompanyObjectiveController.class})
public class CompanyObjectiveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CompanyObjectiveRepository mockRepository;

    @BeforeEach
    public void initializeData() {
        var companyKeyResult_0 = CompanyKeyResult.builder()
                .id(0L)
                .name("Company Key Result 0")
                .current(7)
                .goal(10)
                .confidence(0.99)
                .build();

        var companyKeyResult_1 = CompanyKeyResult.builder()
                .id(1L)
                .name("Company Key Result 1")
                .current(0)
                .goal(200)
                .confidence(0)
                .build();

        var companyKeyResult_2 = CompanyKeyResult.builder()
                .id(2L)
                .name("Company Key Result 2")
                .current(99)
                .goal(100)
                .confidence(1)
                .build();

        var companyKeyResult_3 = CompanyKeyResult.builder()
                .id(3L)
                .name("Company Key Result 3")
                .current(50)
                .goal(100)
                .confidence(0.49)
                .build();

        var companyObjective_0 = CompanyObjective.builder()
                .id(0L)
                .name("Company Objective 0")
                .startDate(LocalDate.of(1970, 1, 19))
                .companyKeyResults(Arrays.asList(companyKeyResult_0, companyKeyResult_1))
                .build();

        var companyObjective_1 = CompanyObjective.builder()
                .id(1L)
                .name("Company Objective 1")
                .startDate(LocalDate.of(1970, 1, 12))
                .companyKeyResults(Arrays.asList(companyKeyResult_2, companyKeyResult_3))
                .build();

        Mockito.when(mockRepository.findById(0L)).thenReturn(Optional.of(companyObjective_0));
        Mockito.when(mockRepository.findById(1L)).thenReturn(Optional.of(companyObjective_1));
        Mockito.when(mockRepository.findAll()).thenReturn(Arrays.asList(companyObjective_0, companyObjective_1));
    }

    @WithMockUser(roles = {"CO OKR Admin", "BUO OKR Admin", "Read Only User"})
    @Test
    public void should_return_single_company_objective() throws Exception {
        this.mockMvc.perform(get("/company-objectives/0").accept(MediaTypes.HAL_FORMS_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_FORMS_JSON.toString()))

                .andExpect(jsonPath("$.name", is("Company Objective 0")))
                .andExpect(jsonPath("$.startDate[0]", is(1970)))
                .andExpect(jsonPath("$.startDate[1]", is(1)))
                .andExpect(jsonPath("$.startDate[2]", is(19)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/company-objectives/0")))

                .andExpect(jsonPath("$._embedded.companyKeyResults[0].name", is("Company Key Result 0")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0].overall", is(0.7)))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._links.self.href", is("http://localhost/company-objectives/0/company-key-results/0")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[1].name", is("Company Key Result 1")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[1].overall", is(0.0)))
                .andExpect(jsonPath("$._embedded.companyKeyResults[1]._links.self.href", is("http://localhost/company-objectives/0/company-key-results/1")))

                .andExpect(jsonPath("$._templates.default.method", is("PUT")))
                .andExpect(jsonPath("$._templates.default.properties", hasSize(2)))
                .andExpect(jsonPath("$._templates.default.properties[0].name", is("name")))
                .andExpect(jsonPath("$._templates.default.properties[0].type", is("text")))
                .andExpect(jsonPath("$._templates.default.properties[1].name", is("startDate")))
                .andExpect(jsonPath("$._templates.default.properties[1].type", is("date")))

                .andExpect(jsonPath("$._templates.update.method", is("PATCH")))
                .andExpect(jsonPath("$._templates.update.properties", hasSize(2)))
                .andExpect(jsonPath("$._templates.update.properties[0].name", is("name")))
                .andExpect(jsonPath("$._templates.update.properties[0].type", is("text")))
                .andExpect(jsonPath("$._templates.update.properties[1].name", is("startDate")))
                .andExpect(jsonPath("$._templates.update.properties[1].type", is("date")))

                .andExpect(jsonPath("$._templates.delete.method", is("DELETE")))
                .andExpect(jsonPath("$._templates.delete.properties", hasSize(0)));
    }

    @WithMockUser(roles = {"CO OKR Admin", "BUO OKR Admin", "Read Only User"})
    @Test
    public void should_return_all_company_objectives() throws Exception {
        this.mockMvc.perform(get("/company-objectives"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_FORMS_JSON.toString()))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/company-objectives")))
                .andExpect(jsonPath("$._templates.default.method", is("POST")))
                .andExpect(jsonPath("$._embedded.companyObjectives", hasSize(2)))

                .andExpect(jsonPath("$._embedded.companyObjectives[0].name", is("Company Objective 0")))
                .andExpect(jsonPath("$._embedded.companyObjectives[0].startDate[0]", is(1970)))
                .andExpect(jsonPath("$._embedded.companyObjectives[0].startDate[1]", is(1)))
                .andExpect(jsonPath("$._embedded.companyObjectives[0].startDate[2]", is(19)))
                .andExpect(jsonPath("$._embedded.companyObjectives[0]._links.self.href", is("http://localhost/company-objectives/0")))

                .andExpect(jsonPath("$._embedded.companyObjectives[0]._templates.default.method", is("PUT")))
                .andExpect(jsonPath("$._embedded.companyObjectives[0]._templates.default.properties", hasSize(2)))
                .andExpect(jsonPath("$._embedded.companyObjectives[0]._templates.default.properties[0].name", is("name")))
                .andExpect(jsonPath("$._embedded.companyObjectives[0]._templates.default.properties[0].type", is("text")))
                .andExpect(jsonPath("$._embedded.companyObjectives[0]._templates.default.properties[1].name", is("startDate")))
                .andExpect(jsonPath("$._embedded.companyObjectives[0]._templates.default.properties[1].type", is("date")))

                .andExpect(jsonPath("$._embedded.companyObjectives[0]._templates.update.method", is("PATCH")))
                .andExpect(jsonPath("$._embedded.companyObjectives[0]._templates.update.properties", hasSize(2)))
                .andExpect(jsonPath("$._embedded.companyObjectives[0]._templates.update.properties[0].name", is("name")))
                .andExpect(jsonPath("$._embedded.companyObjectives[0]._templates.update.properties[0].type", is("text")))
                .andExpect(jsonPath("$._embedded.companyObjectives[0]._templates.update.properties[1].name", is("startDate")))
                .andExpect(jsonPath("$._embedded.companyObjectives[0]._templates.update.properties[1].type", is("date")))

                .andExpect(jsonPath("$._embedded.companyObjectives[0]._templates.delete.method", is("DELETE")))
                .andExpect(jsonPath("$._embedded.companyObjectives[0]._templates.delete.properties", hasSize(0)))

                .andExpect(jsonPath("$._embedded.companyObjectives[1].name", is("Company Objective 1")))
                .andExpect(jsonPath("$._embedded.companyObjectives[1].startDate[0]", is(1970)))
                .andExpect(jsonPath("$._embedded.companyObjectives[1].startDate[1]", is(1)))
                .andExpect(jsonPath("$._embedded.companyObjectives[1].startDate[2]", is(12)))
                .andExpect(jsonPath("$._embedded.companyObjectives[1]._links.self.href", is("http://localhost/company-objectives/1")))
                .andExpect(jsonPath("$._embedded.companyObjectives[1]._templates.default.method", is("PUT")))
                .andExpect(jsonPath("$._embedded.companyObjectives[1]._templates.update.method", is("PATCH")))
                .andExpect(jsonPath("$._embedded.companyObjectives[1]._templates.delete.method", is("DELETE")));
    }
}