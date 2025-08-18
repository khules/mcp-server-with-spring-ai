package com.portal.mcp_server.service.search;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbilashobane.ai.mcp_core.dto.search.Rides;
import com.portal.mcp_server.service.OdooRpcService;
import com.portal.mcp_server.service.search.FindAppointees.Appointee;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

public class FindSlots extends AbstractSearch {
    private static final Logger logger = LoggerFactory.getLogger(FindSlots.class);

    public FindSlots(ExpressionParser expressionParser, OdooRpcService odooRpcService) {
        super(expressionParser, odooRpcService);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void find(StandardEvaluationContext context) {
        Expression ridesExpression = expressionParser.parseExpression("#rides");
        Rides rides = ridesExpression.getValue(context, Rides.class);
        List<Integer> appointeeIds = rides.getAppointees();
        for (Integer appointeeId : appointeeIds) {
            List<String> fields = List.of("slot_ids"); // Specify fields if needed
            int limit = 10; // Set limit as needed
            String responseJson = odooRpcService.searchAndRead(
                    "res.partner",
                    List.of(
                            List.of("id", "=", appointeeId)),
                    fields,
                    limit);
            try {
                JsonRpcResponse slotsDto = objectMapper.readValue(responseJson,
                        objectMapper.getTypeFactory().constructType(JsonRpcResponse.class));
                if (slotsDto != null && slotsDto.getResult() != null) {
                    for (Slots slot : slotsDto.getResult()) {
                        rides.getSlotIds().addAll(slot.getSlot_ids());
                    }
                    FindSlotDetails findSlotDetails = new FindSlotDetails(expressionParser, odooRpcService);
                    findSlotDetails.find(context);
                }
            } catch (JsonProcessingException e) {
                logger.error("Error parsing JSON response", e);
            }
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JsonRpcResponse {
        private String jsonrpc;
        private int id;
        private List<Slots> result;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Slots {
        private int id;
        private List<Integer> slot_ids;
    }

    public static void main(String[] args) {
        OdooRpcService odooRpcService = new OdooRpcService(new ObjectMapper());
        RestTemplate restTemplate = new RestTemplate();
        odooRpcService.setRestTemplate(restTemplate);
        FindSlots service = new FindSlots(new SpelExpressionParser(), odooRpcService);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("rides", Rides.builder().appointees(List.of(33)).build());

        // DTO for [{"id": 33, "slot_ids": [13]}]

        service.find(context);
    }
}
