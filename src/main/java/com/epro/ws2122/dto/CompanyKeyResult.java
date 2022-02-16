package com.epro.ws2122.dto;

import lombok.Data;

@Data
public class CompanyKeyResult {
    private String name;
    private double current;
    private double goal;
    private double confidence;
}
