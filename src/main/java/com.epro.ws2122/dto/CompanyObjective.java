package com.epro.ws2122.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CompanyObjective {
    private String name;
    private LocalDate startDate;
}
