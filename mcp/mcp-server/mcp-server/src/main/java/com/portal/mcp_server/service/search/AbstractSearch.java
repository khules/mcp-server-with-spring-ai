package com.portal.mcp_server.service.search;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.mcp_server.service.OdooRpcService;

public abstract class AbstractSearch {
    protected final ExpressionParser expressionParser;
    protected final OdooRpcService odooRpcService;
    protected final ObjectMapper objectMapper;

    public AbstractSearch(ExpressionParser expressionParser, OdooRpcService odooRpcService) {
        this.expressionParser = expressionParser;
        this.odooRpcService = odooRpcService;
        this.objectMapper = new ObjectMapper();
    }

    public abstract void find(StandardEvaluationContext context);
}
