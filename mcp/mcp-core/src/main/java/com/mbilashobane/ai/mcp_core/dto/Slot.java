package com.mbilashobane.ai.mcp_core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Slot {
    private Integer id;
    @JsonProperty("appointment_type_id")
    private Integer appointmentTypeId;
    private String weekday;
    @JsonProperty("start_hour")
    private Integer startHour;
    @JsonProperty("end_hour")
    private Integer endHour;
    @JsonProperty("start_datetime")
    private String startDatetime;
    @JsonProperty("end_datetime")
    private String endDatetime;
    private Double duration;
}
