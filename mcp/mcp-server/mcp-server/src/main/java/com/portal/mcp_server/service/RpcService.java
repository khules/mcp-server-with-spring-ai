package com.portal.mcp_server.service;

import org.springframework.expression.spel.support.StandardEvaluationContext;

public interface RpcService {
    /**
     * Sets the next handler in the chain.
     *
     * @param rpcService the next RpcService to handle the request
     */
    void setNextHandler(RpcService rpcService);

    void handleRequest(StandardEvaluationContext context);
}
