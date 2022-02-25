package com.epro.ws2122.dto;

import lombok.Data;

@Data
public class KrUpdateDTO {
    private Double current;
    private Double confidence;
    private String comment;
}
