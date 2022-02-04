package com.epro.ws2122;

import com.epro.ws2122.controller.CompanyKeyResultController;
import com.epro.ws2122.domain.CompanyKeyResult;
import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.repository.CompanyKeyResultRepository;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {CompanyKeyResultController.class})
public class CompanyKeyResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyKeyResultRepository mockCompanyKeyResultRepository;

    @MockBean
    private CompanyObjectiveRepository mockCompanyObjectiveRepository;

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

        Mockito.when(mockCompanyKeyResultRepository.findById(0L)).thenReturn(Optional.of(companyKeyResult_0));
        Mockito.when(mockCompanyObjectiveRepository.findById(0L)).thenReturn(Optional.ofNullable(companyObjective_0));
        Mockito.when(mockCompanyKeyResultRepository.findAll()).thenReturn(
                Arrays.asList(companyKeyResult_0, companyKeyResult_1, companyKeyResult_2, companyKeyResult_3));
    }

    @Test
    public void should_return_single_ckr() throws Exception {
        this.mockMvc.perform(get("/company-objectives/0/company-key-results/0").accept(MediaTypes.HAL_FORMS_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_FORMS_JSON.toString()))
                .andExpect(jsonPath("$.name", is("Company Key Result 0")))
                .andExpect(jsonPath("$.current", is(7.0)))
                .andExpect(jsonPath("$.goal", is(10.0)))
                .andExpect(jsonPath("$.confidence", is(0.99)))

                .andExpect(jsonPath("$._embedded.companyObjective.name", is("Company Objective 0")))
                .andExpect(jsonPath("$._embedded.companyObjective._links.self.href", is("http://localhost/company-objectives/0")))

                .andExpect(jsonPath("$._links.self.href", is("http://localhost/company-objectives/0/company-key-results/0")))
                .andExpect(jsonPath("$._links.companyKeyResults.href", is("http://localhost/company-objectives/0/company-key-results")))

                .andExpect(jsonPath("$._templates.default.method", is("PUT")))
                .andExpect(jsonPath("$._templates.default.properties", hasSize(4)))
                .andExpect(jsonPath("$._templates.default.properties[0].name", is("confidence")))
                .andExpect(jsonPath("$._templates.default.properties[0].type", is("number")))
                .andExpect(jsonPath("$._templates.default.properties[1].name", is("current")))
                .andExpect(jsonPath("$._templates.default.properties[1].type", is("number")))
                .andExpect(jsonPath("$._templates.default.properties[2].name", is("goal")))
                .andExpect(jsonPath("$._templates.default.properties[2].type", is("number")))
                .andExpect(jsonPath("$._templates.default.properties[3].name", is("name")))
                .andExpect(jsonPath("$._templates.default.properties[3].type", is("text")))

                .andExpect(jsonPath("$._templates.update.method", is("PATCH")))
                .andExpect(jsonPath("$._templates.update.properties", hasSize(4)))
                .andExpect(jsonPath("$._templates.update.properties[0].name", is("confidence")))
                .andExpect(jsonPath("$._templates.update.properties[0].type", is("number")))
                .andExpect(jsonPath("$._templates.update.properties[1].name", is("current")))
                .andExpect(jsonPath("$._templates.update.properties[1].type", is("number")))
                .andExpect(jsonPath("$._templates.update.properties[2].name", is("goal")))
                .andExpect(jsonPath("$._templates.update.properties[2].type", is("number")))
                .andExpect(jsonPath("$._templates.update.properties[3].name", is("name")))
                .andExpect(jsonPath("$._templates.update.properties[3].type", is("text")))

                .andExpect(jsonPath("$._templates.delete.method", is("DELETE")))
                .andExpect(jsonPath("$._templates.delete.properties", hasSize(0)));
    }

    @Test
    public void should_return_all_ckr() throws Exception {
        this.mockMvc.perform(get("/company-objectives/0/company-key-results"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_FORMS_JSON.toString()))
                .andExpect(jsonPath("$._embedded.companyKeyResults", hasSize(4)))

                .andExpect(jsonPath("$._embedded.companyKeyResults[0].name", is("Company Key Result 0")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0].current", is(7.0)))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0].goal", is(10.0)))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0].confidence", is(0.99)))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._links.self.href", is("http://localhost/company-objectives/0/company-key-results/0")))

                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.default.method", is("PUT")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.default.properties", hasSize(4)))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.default.properties[0].name", is("confidence")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.default.properties[0].type", is("number")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.default.properties[1].name", is("current")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.default.properties[1].type", is("number")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.default.properties[2].name", is("goal")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.default.properties[2].type", is("number")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.default.properties[3].name", is("name")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.default.properties[3].type", is("text")))

                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.update.method", is("PATCH")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.update.properties", hasSize(4)))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.update.properties[0].name", is("confidence")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.update.properties[0].type", is("number")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.update.properties[1].name", is("current")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.update.properties[1].type", is("number")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.update.properties[2].name", is("goal")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.update.properties[2].type", is("number")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.update.properties[3].name", is("name")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.update.properties[3].type", is("text")))

                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.delete.method", is("DELETE")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[0]._templates.delete.properties", hasSize(0)))

                .andExpect(jsonPath("$._embedded.companyKeyResults[1].name", is("Company Key Result 1")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[1].current", is(0.0)))
                .andExpect(jsonPath("$._embedded.companyKeyResults[1].goal", is(200.0)))
                .andExpect(jsonPath("$._embedded.companyKeyResults[1].confidence", is(0.0)))
                .andExpect(jsonPath("$._embedded.companyKeyResults[1]._links.self.href", is("http://localhost/company-objectives/0/company-key-results/1")))

                .andExpect(jsonPath("$._embedded.companyKeyResults[1]._templates.default.method", is("PUT")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[1]._templates.default.properties", hasSize(4)))
                .andExpect(jsonPath("$._embedded.companyKeyResults[1]._templates.update.method", is("PATCH")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[1]._templates.update.properties", hasSize(4)))
                .andExpect(jsonPath("$._embedded.companyKeyResults[1]._templates.delete.method", is("DELETE")))
                .andExpect(jsonPath("$._embedded.companyKeyResults[1]._templates.delete.properties", hasSize(0)))

                .andExpect(jsonPath("$._links.self.href", is("http://localhost/company-objectives/0/company-key-results")))
                .andExpect(jsonPath("$._links.companyObjective.href", is("http://localhost/company-objectives/0")))

                .andExpect(jsonPath("$._templates.default.method", is("POST")))
                .andExpect(jsonPath("$._templates.default.properties", hasSize(4)))
                .andExpect(jsonPath("$._templates.default.properties[0].name", is("confidence")))
                .andExpect(jsonPath("$._templates.default.properties[0].type", is("number")))
                .andExpect(jsonPath("$._templates.default.properties[1].name", is("current")))
                .andExpect(jsonPath("$._templates.default.properties[1].type", is("number")))
                .andExpect(jsonPath("$._templates.default.properties[2].name", is("goal")))
                .andExpect(jsonPath("$._templates.default.properties[2].type", is("number")))
                .andExpect(jsonPath("$._templates.default.properties[3].name", is("name")))
                .andExpect(jsonPath("$._templates.default.properties[3].type", is("text")));
    }
}
