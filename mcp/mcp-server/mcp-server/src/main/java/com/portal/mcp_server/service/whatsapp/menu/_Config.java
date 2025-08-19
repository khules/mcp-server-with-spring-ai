package com.portal.mcp_server.service.whatsapp.menu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ExpressionParser;

import com.portal.mcp_server.service.OdooRpcService;
import com.portal.mcp_server.service.search.FindSlotTypeService;

@Configuration("whatsappMenuConfig")
public class _Config {
    private final ExpressionParser expressionParser;
    private final FindSlotTypeService findSlotTypeService;

    public _Config(ExpressionParser expressionParser, FindSlotTypeService findSlotTypeService) {
        this.expressionParser = expressionParser;
        this.findSlotTypeService = findSlotTypeService;
    }

    @Bean
    MainMenu mainMenu() {
        return new MainMenu(expressionParser);
    }

    @Bean
    AdvertiseRideMenu advertiseRideMenu() {
        return new AdvertiseRideMenu(expressionParser);
    }

    @Bean
    FindRidesMenu findRidesMenu() {
        return new FindRidesMenu(expressionParser, findSlotTypeService);
    }
}
