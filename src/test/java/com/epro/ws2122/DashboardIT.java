package com.epro.ws2122;

import com.epro.ws2122.domain.BusinessUnitKeyResult;
import com.epro.ws2122.domain.BusinessUnitObjective;
import com.epro.ws2122.domain.CompanyKeyResult;
import com.epro.ws2122.domain.CompanyObjective;
import com.epro.ws2122.repository.BusinessUnitKeyResultRepository;
import com.epro.ws2122.repository.BusinessUnitObjectiveRepository;
import com.epro.ws2122.repository.CompanyKeyResultRepository;
import com.epro.ws2122.repository.CompanyObjectiveRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class DashboardIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BusinessUnitKeyResultRepository bukrRepository;
    @Autowired
    BusinessUnitObjectiveRepository buoRepository;
    @Autowired
    CompanyKeyResultRepository ckrRepository;
    @Autowired
    CompanyObjectiveRepository coRepository;

    @Test
    @Transactional
    @WithMockUser(roles = {"Read Only User"})
    void should_get_dahsboard() throws Exception {
        var co0 = new CompanyObjective().builder()
                .name("Company Objective 0")
                .startDate(LocalDate.of(2022, 1, 1))
                .build();

        var ckr0 = new CompanyKeyResult().builder()
                .name("Company Key Result 0")
                .confidence(100)
                .current(99)
                .goal(100)
                .companyObjective(co0)
                .build();

        var ckr1 = new CompanyKeyResult().builder()
                .name("Company Key Result 1")
                .confidence(100)
                .current(99)
                .goal(100)
                .companyObjective(co0)
                .build();

        var buo0 = new BusinessUnitObjective();
        buo0.setStartDate(LocalDate.of(2022, 2, 1));
        buo0.setName("Business Unit Objective 0");

        var bukr0 = new BusinessUnitKeyResult();
        bukr0.setName("Business Unit Key Result 0");
        bukr0.setCurrent(0);
        bukr0.setGoal(100);
        bukr0.setConfidence(20);
        bukr0.setBusinessUnitObjective(buo0);
        bukr0.setCompanyKeyResult(ckr0);

        coRepository.save(co0);
        ckrRepository.save(ckr0);
        ckrRepository.save(ckr1);
        buoRepository.save(buo0);
        bukrRepository.save(bukr0);

        this.mockMvc.perform(get("/dashboard").accept(MediaTypes.HAL_FORMS_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.companyObjectives", hasSize(1)))
                .andExpect(jsonPath("$._embedded.companyObjectives[0].companyKeyResults", hasSize(2)))
                .andExpect(jsonPath("$._embedded.companyObjectives[0].companyKeyResults[0].businessUnitKeyResults", hasSize(1)))
                .andExpect(jsonPath("$._embedded.companyObjectives[0].companyKeyResults[0].businessUnitKeyResults[0]._links.businessUnitObjective.href",
                        is("http://localhost/business-unit-objectives/2")));
    }
}
