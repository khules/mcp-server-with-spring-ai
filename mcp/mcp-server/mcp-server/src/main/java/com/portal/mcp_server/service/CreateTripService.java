package com.portal.mcp_server.service;

import org.springframework.expression.spel.support.StandardEvaluationContext;

public class CreateTripService extends AbstractRpcService {

    @Override
    public void handleRequest(StandardEvaluationContext context) {

        if (nextHandler != null) {
            nextHandler.handleRequest(context);
        }
    }

}
