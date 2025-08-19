package com.portal.mcp_server.service.whatsapp.menu;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.mbilashobane.ai.mcp_core.dto.whatsapp.InteractiveOptions;

public class AdvertiseRideMenu extends WhatsappMenu {

    public AdvertiseRideMenu(ExpressionParser expressionParser) {
        super(expressionParser);
    }

    @Override
    public InteractiveOptions listOptions(StandardEvaluationContext context) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listOptions'");
    }

}
