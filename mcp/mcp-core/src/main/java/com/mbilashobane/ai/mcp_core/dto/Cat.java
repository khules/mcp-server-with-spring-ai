package com.mbilashobane.ai.mcp_core.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cat {
    private Integer id;
    private String name;
    private List<Integer> appointees;
    private String origin;
    private String destination;

}
