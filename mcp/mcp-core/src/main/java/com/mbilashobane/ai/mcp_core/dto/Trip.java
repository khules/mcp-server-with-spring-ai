package com.mbilashobane.ai.mcp_core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @JsonProperty("details")
    private TripDetails tripDetails;

    private Vehicle vehicle;

    private Driver driver;
}