package com.portal.mcp_server.service;

import java.util.Arrays;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.mbilashobane.ai.mcp_core.dto.SearchResponse;

public class CheckPaymentService extends AbstractRpcService {
    private static final Logger LOG = LoggerFactory.getLogger(CheckPaymentService.class);

    @Override
    public void handleRequest(StandardEvaluationContext context) {
        Expression nameExpression = expressionParser.parseExpression("driver.name");
        String name = nameExpression.getValue(context, String.class);

        Expression originExpression = expressionParser.parseExpression("tripDetails.origin");
        String origin = originExpression.getValue(context, String.class);

        Expression destinationExpression = expressionParser.parseExpression("tripDetails.destination");
        String destination = destinationExpression.getValue(context, String.class);

        Expression phoneExpression = expressionParser.parseExpression("driver.contact");
        String phone = phoneExpression.getValue(context, String.class);

        String paymentCriteria = String.format("PAID-%s-%s-%s", phone, origin, destination);

        SearchResponse response = odooRpcService.sendSearchRequest("res.partner", Collections.singletonList(
                Arrays.asList("work_exp", "=", paymentCriteria)));
        LOG.info("Checking payment for user: {}, criteria: {}", name, response);
        if (response.getResult().isEmpty()) {
            LOG.warn("No payment record found for user: {}", name);
        } else {
            if (nextHandler != null) {
                nextHandler.handleRequest(context);
            }
        }
    }

}
