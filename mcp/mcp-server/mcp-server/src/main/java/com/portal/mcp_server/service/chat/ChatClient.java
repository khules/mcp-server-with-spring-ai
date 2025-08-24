package com.portal.mcp_server.service.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbilashobane.ai.mcp_core.dto.search.Rides;
import com.portal.mcp_server.tools.FindRidesTool;

public class ChatClient {
    private static final Logger logger = LoggerFactory.getLogger(ChatClient.class);
    private RestTemplate restTemplate;
    private FindRidesTool findRidesTool;

    private ExpressionParser expressionParser;

    public Rides findRides(String chatMessage, StandardEvaluationContext context) {
        Expression contactExpression = expressionParser.parseExpression("#contact");
        String contact = contactExpression.getValue(context, String.class);
        String url = String.format("http://mcp-client-service:8040/account?q=%s '%s' %s  '%s'",
                "confirm ride information ", chatMessage,
                "that it is valid for contact number", contact);
        try {
            String response = restTemplate.getForObject(url, String.class);
            logger.info("Response from API: {}", response);
            context.setVariable("toolReply", response);
            return findRidesTool.getRides(contact);
        } catch (Exception e) {
            logger.error("Error fetching rides from API", e);
            return new Rides();
        }
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setFindRidesTool(FindRidesTool findRidesTool) {
        this.findRidesTool = findRidesTool;
    }

    @Autowired
    public void setExpressionParser(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }
}
