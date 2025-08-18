package com.mbilashobane.ai.mcp_core.dto.search;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mbilashobane.ai.mcp_core.dto.Slot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rides {
    private String origin;
    private String destination;
    private List<Slot> slots;
    private List<Integer> appointees;
    private List<Integer> slotIds;
}
