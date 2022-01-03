package com.epro.ws2122;

import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
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
        var companyObjective = new CompanyObjective();
        companyObjective.setId(1L);
        companyObjective.setName("Company Objective 1");
        companyObjective.setOverall(0.75);

        Mockito.when(mockRepository.findById(1L)).thenReturn(Optional.of(companyObjective));
    }

    @Test
    public void Requesting_Single_Company_Objective_By_Id_Should_Return_Ok() throws Exception {
        this.mockMvc.perform(get("/company-objectives/1")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void HttpRequestAllTest() throws Exception {
        this.mockMvc.perform(get("/company-objectives")).andDo(print()).andExpect(status().isOk());
    }
}