package com.epro.ws2122;

import com.epro.ws2122.controller.BusinessUnitKeyResultController;
import com.epro.ws2122.controller.BusinessUnitObjectiveController;
import com.epro.ws2122.controller.CompanyKeyResultController;
import com.epro.ws2122.domain.BusinessUnitKeyResult;
import com.epro.ws2122.domain.BusinessUnitObjective;
import com.epro.ws2122.domain.CompanyKeyResult;
import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.dto.BukrDTO;
import com.epro.ws2122.dto.BuoDTO;
import com.epro.ws2122.dto.CkrDTO;
import com.epro.ws2122.dto.KrUpdateDTO;
import com.epro.ws2122.repository.BusinessUnitKeyResultRepository;
import com.epro.ws2122.repository.BusinessUnitObjectiveRepository;
import com.epro.ws2122.repository.CompanyKeyResultRepository;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import com.epro.ws2122.util.JsonPatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {BusinessUnitKeyResultController.class, BusinessUnitObjectiveController.class, CompanyKeyResultController.class})
@ActiveProfiles("test")
public class BusinessUnitKeyResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BusinessUnitObjectiveRepository mockBusinessUnitObjectiveRepository;

    @MockBean
    BusinessUnitKeyResultRepository mockBusinessUnitKeyResultRepository;

    @MockBean
    CompanyObjectiveRepository mockCompanyObjectiveRepository;

    @MockBean
    CompanyKeyResultRepository mockCompanyKeyResultRepository;

    @MockBean
    JsonPatcher<BukrDTO> mockPatcher;

    @MockBean
    JsonPatcher<KrUpdateDTO> mockUpdatePatcher;

    @MockBean
    ModelMapper mockMapper;

    @MockBean
    JsonPatcher<BuoDTO> mockBuoPatcher;

    @MockBean
    JsonPatcher<CkrDTO> mockCkrPatcher;

    @BeforeEach
    public void initializeData() {
        var companyObjective_0 = CompanyObjective.builder()
                .id(0L)
                .name("Company Objective 0")
                .startDate(LocalDate.of(1970, 1, 19))
                .build();

        var companyKeyResult_0 = CompanyKeyResult.builder()
                .id(0L)
                .name("Company Key Result 0")
                .current(7)
                .goal(10)
                .confidence(0.99)
                .companyObjective(companyObjective_0)
                .build();

        var businessUnitKeyResult_0 = BusinessUnitKeyResult.builder()
                .id(0L)
                .name("Business Unit Key Result 0")
                .companyKeyResult(companyKeyResult_0)
                .build();

        var businessUnitKeyResult_1 = BusinessUnitKeyResult.builder()
                .id(1L)
                .name("Business Unit Key Result 1")
                .build();

        var businessUnitKeyResult_2 = BusinessUnitKeyResult.builder()
                .id(2L)
                .name("Business Unit Key Result 2")
                .build();

        var businessUnitKeyResult_3 = BusinessUnitKeyResult.builder()
                .id(3L)
                .name("Business Unit Key Result 3")
                .build();

        var businessUnitObjective_0 = BusinessUnitObjective.builder()
                .id(0L)
                .name("Business Unit Objective 0")
                .startDate(LocalDate.of(1970, 1, 19))
                .businessUnitKeyResults(Arrays.asList(businessUnitKeyResult_0, businessUnitKeyResult_1))
                .build();


        Mockito.when(mockBusinessUnitKeyResultRepository.findById(0L)).thenReturn(Optional.of(businessUnitKeyResult_0));
        Mockito.when(mockBusinessUnitObjectiveRepository.findById(0L)).thenReturn(Optional.ofNullable(businessUnitObjective_0));
        Mockito.when(mockBusinessUnitKeyResultRepository.findAllByBusinessUnitObjective(businessUnitObjective_0)).thenReturn(
                Arrays.asList(businessUnitKeyResult_0, businessUnitKeyResult_1));
    }

    @Test
    @WithMockUser(roles = {"CO OKR Admin", "BUO OKR Admin", "Read Only User"})
    public void should_return_single_bukr() throws Exception {
        this.mockMvc.perform(get("/business-unit-objectives/0/business-unit-key-results/0")
                        .accept(MediaTypes.HAL_FORMS_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_FORMS_JSON.toString()))
                .andExpect(jsonPath("$.name", is("Business Unit Key Result 0")))

                .andExpect(jsonPath("$._embedded.businessUnitObjective.name", is("Business Unit Objective 0")))
                .andExpect(jsonPath("$._embedded.businessUnitObjective._links.self.href", is("http://localhost/business-unit-objectives/0")))
                .andExpect(jsonPath("$._embedded.companyKeyResult.name", is("Company Key Result 0")))
                .andExpect(jsonPath("$._embedded.companyKeyResult.overall", is(0.7)))
                .andExpect(jsonPath("$._embedded.companyKeyResult._links.self.href", is("http://localhost/company-objectives/0/company-key-results/0")))

                .andExpect(jsonPath("$._links.self.href", is("http://localhost/business-unit-objectives/0/business-unit-key-results/0")))

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
//                .andExpect(jsonPath("$._templates.update.properties", hasSize(4)))
//                .andExpect(jsonPath("$._templates.update.properties[0].name", is("confidence")))
//                .andExpect(jsonPath("$._templates.update.properties[0].type", is("number")))
//                .andExpect(jsonPath("$._templates.update.properties[1].name", is("current")))
//                .andExpect(jsonPath("$._templates.update.properties[1].type", is("number")))
//                .andExpect(jsonPath("$._templates.update.properties[2].name", is("goal")))
//                .andExpect(jsonPath("$._templates.update.properties[2].type", is("number")))
//                .andExpect(jsonPath("$._templates.update.properties[3].name", is("name")))
//                .andExpect(jsonPath("$._templates.update.properties[3].type", is("text")))

                .andExpect(jsonPath("$._templates.delete.method", is("DELETE")))
                .andExpect(jsonPath("$._templates.delete.properties", hasSize(0)));
    }

    @Test
    @WithMockUser(roles = {"CO OKR Admin", "BUO OKR Admin", "Read Only User"})
    public void should_return_all_bukr() throws Exception {
        this.mockMvc.perform(get("/business-unit-objectives/0/business-unit-key-results")
                        .accept(MediaTypes.HAL_FORMS_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_FORMS_JSON.toString()))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults", hasSize(2)))

                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0].name", is("Business Unit Key Result 0")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._links.self.href", is("http://localhost/business-unit-objectives/0/business-unit-key-results/0")))

                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.default.method", is("PUT")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.default.properties", hasSize(4)))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.default.properties[0].name", is("confidence")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.default.properties[0].type", is("number")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.default.properties[1].name", is("current")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.default.properties[1].type", is("number")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.default.properties[2].name", is("goal")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.default.properties[2].type", is("number")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.default.properties[3].name", is("name")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.default.properties[3].type", is("text")))

                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.update.method", is("PATCH")))
//                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.update.properties", hasSize(4)))
//                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.update.properties[0].name", is("confidence")))
//                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.update.properties[0].type", is("number")))
//                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.update.properties[1].name", is("current")))
//                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.update.properties[1].type", is("number")))
//                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.update.properties[2].name", is("goal")))
//                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.update.properties[2].type", is("number")))
//                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.update.properties[3].name", is("name")))
//                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.update.properties[3].type", is("text")))

                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.delete.method", is("DELETE")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[0]._templates.delete.properties", hasSize(0)))

                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[1].name", is("Business Unit Key Result 1")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[1]._links.self.href", is("http://localhost/business-unit-objectives/0/business-unit-key-results/1")))

                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[1]._templates.default.method", is("PUT")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[1]._templates.default.properties", hasSize(4)))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[1]._templates.update.method", is("PATCH")))
//                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[1]._templates.update.properties", hasSize(4)))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[1]._templates.delete.method", is("DELETE")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResults[1]._templates.delete.properties", hasSize(0)))

                .andExpect(jsonPath("$._links.self.href", is("http://localhost/business-unit-objectives/0/business-unit-key-results")))

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

