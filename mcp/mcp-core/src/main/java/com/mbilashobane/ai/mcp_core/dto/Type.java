package com.mbilashobane.ai.mcp_core.dto;

import java.util.List;

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
public class Type {
    private Integer id;
    private String name;
    @JsonProperty("staff_user_ids")
    private List<Integer> staffUserIds;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("slot_ids")
    private List<Integer> slotIds;
}
