package com.epro.ws2122;

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
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompanyObjectiveController.class)
@AutoConfigureMockMvc
class CompanyObjectiveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CompanyObjectiveRepository mockRepository;

    @Autowired
    CompanyObjectiveRepository companyObjectiveRepository;

    @BeforeEach
    public void initializeData() {
        var companyObjective_0 = CompanyObjective.builder()
                .id(1L)
                .name("Company Objective 1")
                .overall(0.75)
                .build();

        var companyObjective_1 = CompanyObjective.builder()
                .id(2L)
                .name("Company Objective 2")
                .overall(0.1)
                .build();

        Mockito.when(mockRepository.findById(1L)).thenReturn(Optional.of(companyObjective_0));
        Mockito.when(mockRepository.findById(2L)).thenReturn(Optional.of(companyObjective_1));
    }

    @Test
    public void Requesting_Single_Company_Objective_By_Id_Should_Return_Ok() throws Exception {
        this.mockMvc.perform(get("/company-objectives/1")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void Requesting_All_Company_Objective_Should_Return_Ok() throws Exception {
        this.mockMvc.perform(get("/company-objectives")).andDo(print()).andExpect(status().isOk());
    }
}