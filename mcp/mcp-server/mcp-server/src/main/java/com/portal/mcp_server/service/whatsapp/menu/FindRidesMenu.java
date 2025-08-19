package com.portal.mcp_server.service.whatsapp.menu;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbilashobane.ai.mcp_core.dto.Slot;
import com.mbilashobane.ai.mcp_core.dto.search.Rides;
import com.mbilashobane.ai.mcp_core.dto.whatsapp.Interaction;
import com.mbilashobane.ai.mcp_core.dto.whatsapp.InteractiveOptions;
import com.mbilashobane.ai.mcp_core.dto.whatsapp.WebhookMessage;
import com.mbilashobane.ai.mcp_core.dto.whatsapp.InteractiveOptions.Row;
import com.portal.mcp_server.service.OdooRpcService;
import com.portal.mcp_server.service.search.FindSlotTypeService;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;

public class FindRidesMenu extends WhatsappMenu {
    private static final Logger logger = LoggerFactory.getLogger(FindRidesMenu.class);
    private final FindSlotTypeService findSlotTypeService;

    public FindRidesMenu(ExpressionParser expressionParser, FindSlotTypeService findSlotTypeService) {
        super(expressionParser);
        this.findSlotTypeService = findSlotTypeService;
    }

    public InteractiveOptions listOptions(StandardEvaluationContext context) {
        Expression webhookMessageExpression = expressionParser.parseExpression("#webhook.message");
        WebhookMessage webhookMessage = webhookMessageExpression.getValue(context, WebhookMessage.class);

        Expression contactExpression = expressionParser.parseExpression("#contact");
        String contact = contactExpression.getValue(context, String.class);
        if (!optionsMap.containsKey(contact)) {
            return serviceAreas(contact);
        }
        return availableRides(context);
    }

    private InteractiveOptions serviceAreas(String contact) {
        try {
            ClassPathResource resource = new ClassPathResource("whatsapp/templates/ServiceAreas.json");
            InputStream inputStream = resource.getInputStream();
            InteractiveOptions interactiveOptions = mapper.readValue(inputStream, InteractiveOptions.class);

            optionsMap.put(contact,
                    new CopyOnWriteArrayList<>(
                            List.of(Interaction.builder().interactiveOptions(interactiveOptions).build())));
            return interactiveOptions;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load ServiceAreas.json", e);
        }
    }

    private InteractiveOptions availableRides(StandardEvaluationContext context) {
        try {
            ClassPathResource resource = new ClassPathResource("whatsapp/templates/FindRidesMenu.json");
            InputStream inputStream = resource.getInputStream();
            InteractiveOptions options = mapper.readValue(inputStream, InteractiveOptions.class);
            Expression expression = expressionParser.parseExpression("#contact");
            String contact = expression.getValue(context, String.class);
            if (!optionsMap.containsKey(contact)) {
                findSlotTypeService.handleRequest(context);
                Expression ridesExpression = expressionParser.parseExpression("#rides");
                Rides rides = ridesExpression.getValue(context, Rides.class);
                options.setTo(contact);

                String route = String.format("Available %s to %s", rides.getOrigin(), rides.getDestination())
                        .substring(0, 24);
                options.getInteractive().getHeader().setText(route);
                List<Row> rows = new ArrayList<>();
                for (int i = 0; i < rides.getSlotIds().size(); i++) {
                    String rowId = String.format("slot-%s", rides.getSlotIds().get(i));
                    Slot slot = rides.getSlots().get(i);
                    LocalDateTime departureDate = LocalDateTime.parse(slot.getStartDatetime(), shortFormatter);
                    String title = String.format("%s %s %s", departureDate.getMonth(),
                            departureDate.getDayOfMonth(), departureDate.getHour() + ":" + departureDate.getMinute());
                    String description = String.format("%s %s %s - %s", departureDate.getMonth(),
                            departureDate.getDayOfMonth(), slot.getStartDatetime(), slot.getEndDatetime());
                    rows.add(new Row(rowId, title, description));
                }
                options.getInteractive().getAction().getSections()[0].setTitle(route);
                options.getInteractive().getAction().getSections()[0].setRows(rows.toArray(new Row[0]));
            }
            optionsMap.put(contact,
                    new CopyOnWriteArrayList<>(List.of(Interaction.builder().interactiveOptions(options).build())));
            return options;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load SlotsOptionsMessage.json", e);
        }
    }

    public static void main(String[] args) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("rides", Rides.builder()
                .origin("Durban")
                .destination("Cape Town")
                .appointees(new CopyOnWriteArrayList<>())
                .slotIds(new CopyOnWriteArrayList<>())
                .slots(new CopyOnWriteArrayList<>())
                .build());
        context.setVariable("contact", "27844988332");
        OdooRpcService odooRpcService = new OdooRpcService(new ObjectMapper());
        odooRpcService.setRestTemplate(new RestTemplate());
        FindSlotTypeService findSlotTypeService = new FindSlotTypeService();
        findSlotTypeService.setExpressionParser(new SpelExpressionParser());
        findSlotTypeService.setOdooRpcService(odooRpcService);
        FindRidesMenu findRidesFlow = new FindRidesMenu(new SpelExpressionParser(), findSlotTypeService);
        InteractiveOptions options = findRidesFlow.listOptions(context);
        try {
            String interactiveOptions = new ObjectMapper().writeValueAsString(options);
            logger.info("Interactive Options: {}", interactiveOptions);
        } catch (JsonProcessingException e) {
            logger.error("Error processing JSON", e);
        }
    }
}