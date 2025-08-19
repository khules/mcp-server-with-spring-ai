package com.mbilashobane.ai.mcp_core.dto.whatsapp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Interaction {

    private InteractiveOptions interactiveOptions;
    private InteractiveResponse interactiveResponse;
}
