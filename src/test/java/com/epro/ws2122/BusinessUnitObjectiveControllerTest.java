package com.epro.ws2122;

import com.epro.ws2122.controller.BusinessUnitObjectiveController;
import com.epro.ws2122.domain.BusinessUnitKeyResult;
import com.epro.ws2122.domain.BusinessUnitObjective;
import com.epro.ws2122.repository.BusinessUnitObjectiveRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {BusinessUnitObjectiveController.class})
public class BusinessUnitObjectiveControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BusinessUnitObjectiveRepository mockRepository;

    @BeforeEach
    public void initializeData() {
        var businessUnitKeyResult_0 = BusinessUnitKeyResult.builder()
                .id(0L)
                .name("Business Unit Key Result 0")
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

        var businessUnitObjective_1 = BusinessUnitObjective.builder()
                .id(1L)
                .name("Business Unit Objective 1")
                .startDate(LocalDate.of(1970, 1, 12))
                .businessUnitKeyResults(Arrays.asList(businessUnitKeyResult_2, businessUnitKeyResult_3))
                .build();

        Mockito.when(mockRepository.findById(0L)).thenReturn(Optional.of(businessUnitObjective_0));
        Mockito.when(mockRepository.findById(1L)).thenReturn(Optional.of(businessUnitObjective_1));
        Mockito.when(mockRepository.findAll()).thenReturn(Arrays.asList(businessUnitObjective_0, businessUnitObjective_1));
    }

    @Test
    public void should_return_single_business_unit_objective() throws Exception {
        this.mockMvc.perform(get("/business-unit-objectives/0").accept(MediaTypes.HAL_FORMS_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_FORMS_JSON.toString()));
    }

    @Test
    public void should_return_all_business_unit_objectives() throws Exception {
        this.mockMvc.perform(get("/business-unit-objectives").accept(MediaTypes.HAL_FORMS_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_FORMS_JSON.toString()));
    }
}