package com.portal.mcp_server.service.search;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbilashobane.ai.mcp_core.dto.Slot;
import com.mbilashobane.ai.mcp_core.dto.search.Rides;
import com.portal.mcp_server.service.OdooRpcService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FindSlotDetails extends AbstractSearch {

    private static final Logger logger = LoggerFactory.getLogger(FindSlotDetails.class);

    public FindSlotDetails(ExpressionParser expressionParser, OdooRpcService odooRpcService) {
        super(expressionParser, odooRpcService);
    }

    @Override
    public void find(StandardEvaluationContext context) {
        Expression ridesExpression = expressionParser.parseExpression("#rides");
        Rides rides = ridesExpression.getValue(context, Rides.class);
        for (Integer slotId : rides.getSlotIds()) {
            String responseJson = odooRpcService.searchAndRead(
                    "appointement.slots",
                    List.of(
                            List.of("id", "=", slotId)),
                    List.of("id", "start_datetime", "end_datetime"), // Specify fields if needed
                    1); // Limit to 1 for details

            try {
                SlotDetailsDto slotDetailsDto = objectMapper.readValue(responseJson,
                        objectMapper.getTypeFactory().constructType(SlotDetailsDto.class));
                if (slotDetailsDto != null) {
                    rides.getSlots().addAll(slotDetailsDto.getResult());
                }
            } catch (JsonProcessingException e) {
                logger.error("Error parsing JSON response", e);
            }
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlotDetailsDto {
        private String jsonrpc;
        private int id;
        private List<Slot> result;

    }

    public static void main(String[] args) {
        OdooRpcService odooRpcService = new OdooRpcService(new ObjectMapper());
        RestTemplate restTemplate = new RestTemplate();
        odooRpcService.setRestTemplate(restTemplate);
        FindSlotDetails service = new FindSlotDetails(new SpelExpressionParser(), odooRpcService);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("rides", Rides.builder().appointees(List.of(33))
                .slotIds(List.of(13))
                .slots(new CopyOnWriteArrayList<>())
                .build());

        // DTO for [{"id": 33, "slot_ids": [13]}]

        service.find(context);
    }
}
