package com.portal.mcp_server.service;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;

public abstract class AbstractRpcService implements RpcService {
    protected static final DateTimeFormatter shortFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    protected ExpressionParser expressionParser;
    protected RpcService nextHandler;
    protected OdooRpcService odooRpcService;

    @Autowired
    public void setExpressionParser(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    @Autowired
    public void setOdooRpcService(OdooRpcService odooRpcService) {
        this.odooRpcService = odooRpcService;
    }

    @Override
    public void setNextHandler(RpcService rpcService) {
        this.nextHandler = rpcService;
    }
}
