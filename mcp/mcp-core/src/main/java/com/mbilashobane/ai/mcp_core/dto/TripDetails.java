package com.mbilashobane.ai.mcp_core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripDetails {
    private String origin;
    private String destination;
    private String date;
    private int numberOfPassengers;
    private double price;
}