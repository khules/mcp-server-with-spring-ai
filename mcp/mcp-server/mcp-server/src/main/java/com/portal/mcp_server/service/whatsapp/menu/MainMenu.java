package com.portal.mcp_server.service.whatsapp.menu;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbilashobane.ai.mcp_core.dto.search.Rides;
import com.mbilashobane.ai.mcp_core.dto.whatsapp.InteractiveOptions;
import com.mbilashobane.ai.mcp_core.dto.whatsapp.WebhookMessage;
import com.portal.mcp_server.service.OdooRpcService;
import com.portal.mcp_server.service.search.FindSlotTypeService;

public class MainMenu extends WhatsappMenu {
    private static final Logger logger = LoggerFactory.getLogger(MainMenu.class);
    private FindRidesMenu findRidesMenu;

    public MainMenu(ExpressionParser expressionParser) {
        super(expressionParser);
    }

    @Override
    public InteractiveOptions listOptions(StandardEvaluationContext context) {

        extractContact(context);
        Expression contactExpression = expressionParser.parseExpression("#contact");
        String contact = contactExpression.getValue(context, String.class);
        if (findRidesMenu.hasBeenSelected(contact)) {
            return findRidesMenu.listOptions(context);
        } else {
            extractSelectedOption(context);
            Expression mainMenuOptionExpression = expressionParser.parseExpression("#mainMenuOption");
            String mainMenuOption = mainMenuOptionExpression.getValue(context, String.class);
            if (mainMenuOption == null || mainMenuOption.isEmpty()) {
                return loadMainMenuTemplate();
            }
            switch (mainMenuOption) {
                case "Menu-Find-A-Ride-1":
                    // Handle option1
                    return findRidesMenu.listOptions(context);

                default:
                    return loadMainMenuTemplate();
            }

        }
    }

    private InteractiveOptions loadMainMenuTemplate() {
        try {
            ClassPathResource resource = new ClassPathResource("whatsapp/templates/MainMenu.json");
            InputStream inputStream = resource.getInputStream();
            return mapper.readValue(inputStream, InteractiveOptions.class);
        } catch (IOException e) {
            logger.error("Error loading Main Menu template", e);
            return null;
        }
    }

    @Autowired
    public void setFindRidesMenu(FindRidesMenu findRidesMenu) {
        this.findRidesMenu = findRidesMenu;
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
        // context.setVariable("contact", "27844988332");
        OdooRpcService odooRpcService = new OdooRpcService(new ObjectMapper());
        odooRpcService.setRestTemplate(new RestTemplate());
        FindSlotTypeService findSlotTypeService = new FindSlotTypeService();
        findSlotTypeService.setExpressionParser(new SpelExpressionParser());
        findSlotTypeService.setOdooRpcService(odooRpcService);

        FindRidesMenu findRidesMenu = new FindRidesMenu(new SpelExpressionParser(), findSlotTypeService);

        MainMenu mainMenu = new MainMenu(new SpelExpressionParser());
        mainMenu.setFindRidesMenu(findRidesMenu);
        InteractiveOptions options = mainMenu.listOptions(context);

        try {
            String interactiveOptions = new ObjectMapper().writeValueAsString(options);
            logger.info("Interactive Options: {}", interactiveOptions);

            ClassPathResource resource = new ClassPathResource("db/webhook.json");
            InputStream inputStream = resource.getInputStream();
            WebhookMessage webhookMessage = new ObjectMapper().readValue(inputStream, WebhookMessage.class);
            context.setVariable("webhookMessage", webhookMessage);
            options = mainMenu.listOptions(context);
        } catch (Exception e) {
            logger.error("Error processing JSON", e);
        }
        try {
            String interactiveOptions = new ObjectMapper().writeValueAsString(options);
            logger.info("Interactive Options: {}", interactiveOptions);
            options = mainMenu.generateTypingResponse(context);
            interactiveOptions = new ObjectMapper().writeValueAsString(options);
            logger.info("Interactive Options: {}", interactiveOptions);
        } catch (JsonProcessingException e) {
            logger.error("Error processing JSON", e);
        }
    }
}
