package com.mbilashobane.ai.mcp_core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonRpcRequest<T> {
    private String jsonrpc;
    private String method;
    private T params;
    private long id;
}
