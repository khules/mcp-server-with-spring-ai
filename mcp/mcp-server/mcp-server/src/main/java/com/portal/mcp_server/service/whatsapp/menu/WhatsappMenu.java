package com.portal.mcp_server.service.whatsapp.menu;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbilashobane.ai.mcp_core.dto.whatsapp.Interaction;
import com.mbilashobane.ai.mcp_core.dto.whatsapp.InteractiveOptions;

public abstract class WhatsappMenu {

    protected static final DateTimeFormatter shortFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    protected final ObjectMapper mapper = new ObjectMapper();
    protected final Map<String, List<Interaction>> optionsMap = new LinkedHashMap<>();
    protected final ExpressionParser expressionParser;

    public WhatsappMenu(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    public boolean hasBeenSelected(String contact) {
        return optionsMap.containsKey(contact);
    }

    public void completeSelection(String contact) {
        optionsMap.remove(contact);
    }

    public abstract InteractiveOptions listOptions(StandardEvaluationContext context);
}
