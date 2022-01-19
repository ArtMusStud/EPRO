package com.epro.ws2122;

import com.epro.ws2122.assembler.CompanyKeyResultAssembler;
import com.epro.ws2122.controller.CompanyKeyResultController;
import com.epro.ws2122.domain.CompanyKeyResult;
import com.epro.ws2122.repository.CompanyKeyResultRepository;
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

@WebMvcTest(controllers = {CompanyKeyResultController.class, CompanyKeyResultAssembler.class})
@AutoConfigureMockMvc
public class CompanyKeyResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CompanyKeyResultRepository mockRepository;

    @BeforeEach
    public void initializeData() {
        var cokr_0 = CompanyKeyResult.builder().id(0L).build();
        var cokr_1 = CompanyKeyResult.builder().id(1L).build();

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
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/company-objectives/0/company-objectives-key-results/0")))
                .andExpect(jsonPath("$._links.dashboard.href", is("http://localhost/dashboard")));
    }

    @Test
    public void should_return_all_cokr() throws Exception {
        this.mockMvc.perform(get("/company-objectives/0/company-objectives-key-results"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/company-objectives/0/company-objectives-key-results")));
    }
}
