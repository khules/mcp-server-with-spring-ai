package com.portal.mcp_server.service.whatsapp.menu;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbilashobane.ai.mcp_core.dto.whatsapp.Interaction;
import com.mbilashobane.ai.mcp_core.dto.whatsapp.InteractiveOptions;
import com.mbilashobane.ai.mcp_core.dto.whatsapp.WebhookMessage;

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

    protected void extractSelectedOption(StandardEvaluationContext context) {
        Expression webhookMessageExpression = expressionParser.parseExpression("#webhookMessage");
        WebhookMessage webhookMessage = webhookMessageExpression.getValue(context, WebhookMessage.class);
        if (webhookMessage == null) {
            return;
        }
        List<WebhookMessage.Message> messages = webhookMessage.getEntry().get(0).getChanges().get(0).getValue()
                .getMessages();
        if (messages != null && messages.isEmpty()) {
            return;
        }
        String selectedOption = messages.get(0).getInteractive() != null
                ? messages.get(0).getInteractive().getListReply().getId()
                : messages.get(0).getId();
        context.setVariable("mainMenuOption", selectedOption);
    }

    protected void extractReply(StandardEvaluationContext context) {
        Expression webhookMessageExpression = expressionParser.parseExpression("#webhookMessage");
        WebhookMessage webhookMessage = webhookMessageExpression.getValue(context, WebhookMessage.class);
        if (webhookMessage == null) {
            return;
        }
        List<WebhookMessage.Message> messages = webhookMessage.getEntry().get(0).getChanges().get(0).getValue()
                .getMessages();
        if (messages != null && messages.isEmpty()) {
            return;
        }
        String reply = messages.get(0).getInteractive() != null
                ? messages.get(0).getInteractive().getListReply().getTitle()
                : messages.get(0).getText().getBody();
        String replyId = messages.get(0).getInteractive() != null
                ? messages.get(0).getInteractive().getListReply().getId()
                : messages.get(0).getId();
        context.setVariable("responseReply", reply);
        context.setVariable("responseReplyId", replyId);
    }

    public abstract InteractiveOptions listOptions(StandardEvaluationContext context);
}
