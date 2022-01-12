package com.epro.ws2122;

import com.epro.ws2122.assembler.CompanyObjectiveKeyResultAssembler;
import com.epro.ws2122.controller.CompanyObjectiveKeyResultController;
import com.epro.ws2122.domain.CompanyObjectiveKeyResult;
import com.epro.ws2122.repository.CompanyObjectiveKeyResultRepository;
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

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {CompanyObjectiveKeyResultController.class, CompanyObjectiveKeyResultAssembler.class})
@AutoConfigureMockMvc
public class CompanyObjectiveKeyResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CompanyObjectiveKeyResultRepository mockRepository;

    @BeforeEach
    public void initializeData() {
        var cokr_0 = CompanyObjectiveKeyResult.builder().id(0L).build();
        var cokr_1 = CompanyObjectiveKeyResult.builder().id(1L).build();

        Mockito.when(mockRepository.findById(0L)).thenReturn(Optional.of(cokr_0));
        Mockito.when(mockRepository.findById(1L)).thenReturn(Optional.of(cokr_1));
        Mockito.when(mockRepository.findAll()).thenReturn(Arrays.asList(cokr_0, cokr_1));
    }

    @Test
    public void should_return_single_cokr() throws Exception {
        this.mockMvc.perform(get("/company-objectives/0/company-objectives-key-results/0").accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/company-objectives/0/company-objectives-key-results/0")));
    }
}
