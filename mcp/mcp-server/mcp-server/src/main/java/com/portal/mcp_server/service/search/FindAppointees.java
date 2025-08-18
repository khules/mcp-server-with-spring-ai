package com.portal.mcp_server.service.search;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mbilashobane.ai.mcp_core.dto.Slot;
import com.mbilashobane.ai.mcp_core.dto.search.Rides;
import com.portal.mcp_server.service.OdooRpcService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FindAppointees extends AbstractSearch {
    private static final Logger logger = LoggerFactory.getLogger(FindAppointees.class);

    public FindAppointees(ExpressionParser expressionParser, OdooRpcService odooRpcService) {
        super(expressionParser, odooRpcService);
    }

    @Override
    public void find(StandardEvaluationContext context) {
        Expression ridesExpression = expressionParser.parseExpression("#rides");
        Rides rides = ridesExpression.getValue(context, Rides.class);
        String origin = rides.getOrigin();
        String destination = rides.getDestination();
        List<String> fields = List.of("appointees"); // Specify fields if needed
        int limit = 10; // Set limit as needed
        String responseJson = odooRpcService.searchAndRead(
                "appointees.category",
                List.of(
                        List.of("origin", "=", origin),
                        List.of("destination", "=", destination)),
                fields,
                limit);
        try {
            JsonRpcResponse appointees = objectMapper.readValue(responseJson,
                    objectMapper.getTypeFactory().constructType(JsonRpcResponse.class));
            if (appointees != null && appointees.getResult() != null) {
                for (Appointee appointee : appointees.getResult()) {
                    if (appointee.getAppointees() != null) {
                        rides.getAppointees().addAll(appointee.getAppointees()); // potential parallel execution
                    }
                }
                FindSlots findSlots = new FindSlots(expressionParser, odooRpcService);
                findSlots.find(context);
            }
        } catch (JsonProcessingException e) {
            logger.error("Error parsing JSON response", e);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Appointee {
        private int id;
        private List<Integer> appointees;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JsonRpcResponse {
        private String jsonrpc;
        private int id;
        private List<Appointee> result;

    }
}
