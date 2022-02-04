package com.epro.ws2122;

import com.epro.ws2122.controller.BusinessUnitKeyResultController;
import com.epro.ws2122.controller.BusinessUnitObjectiveController;
import com.epro.ws2122.controller.CompanyKeyResultController;
import com.epro.ws2122.domain.BusinessUnitKeyResult;
import com.epro.ws2122.domain.BusinessUnitObjective;
import com.epro.ws2122.domain.CompanyKeyResult;
import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.repository.BusinessUnitKeyResultRepository;
import com.epro.ws2122.repository.BusinessUnitObjectiveRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BusinessUnitKeyResultController.class, BusinessUnitObjectiveController.class, CompanyKeyResultController.class})
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
        Mockito.when(mockBusinessUnitKeyResultRepository.findAll()).thenReturn(
                Arrays.asList(businessUnitKeyResult_0, businessUnitKeyResult_1, businessUnitKeyResult_2, businessUnitKeyResult_3));
    }

    @Test
    public void should_return_single_bukr() throws Exception {
        this.mockMvc.perform(get("/business-unit-objectives/0/business-unit-key-results/0")
                        .accept(MediaTypes.HAL_FORMS_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_FORMS_JSON.toString()));
    }

    @Test
    public void should_return_all_bukr() throws Exception {
        this.mockMvc.perform(get("/business-unit-objectives/0/business-unit-key-results")
                        .accept(MediaTypes.HAL_FORMS_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_FORMS_JSON.toString()));
    }
}
