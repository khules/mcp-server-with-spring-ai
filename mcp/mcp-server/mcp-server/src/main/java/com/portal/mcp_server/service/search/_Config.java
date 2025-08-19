package com.portal.mcp_server.service.search;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ExpressionParser;

import com.portal.mcp_server.service.OdooRpcService;

@Configuration("searchConfig")
public class _Config {
    private final OdooRpcService odooRpcService;
    private final ExpressionParser expressionParser;

    public _Config(OdooRpcService odooRpcService, ExpressionParser expressionParser) {
        this.odooRpcService = odooRpcService;
        this.expressionParser = expressionParser;
    }

    @Bean
    FindSlotTypeService findSlotTypeService() {
        FindSlotTypeService findSlotTypeService = new FindSlotTypeService();
        findSlotTypeService.setOdooRpcService(odooRpcService);
        findSlotTypeService.setExpressionParser(expressionParser);
        return findSlotTypeService;
    }

    @Bean
    FindAppointees findAppointees() {
        return new FindAppointees(expressionParser, odooRpcService);
    }

    @Bean
    FindSlots findSlots() {
        return new FindSlots(expressionParser, odooRpcService);
    }

    @Bean
    FindSlotDetails findSlotDetails() {
        return new FindSlotDetails(expressionParser, odooRpcService);
    }
}
