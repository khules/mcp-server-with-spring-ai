package com.portal.mcp_client.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private ChatClient chatClient;
    private final ToolCallbackProvider toolCallbackProvider;
    private final ChatClient.Builder chatBuilder;

    public AccountController(ChatClient.Builder chatBuilder, ToolCallbackProvider toolCallbackProvider) {
        this.chatBuilder = chatBuilder;
        this.chatClient = null;
        this.toolCallbackProvider = toolCallbackProvider;
    }

    @GetMapping("/account")
    public String getAccount(@RequestParam("q") String name) {
        this.chatClient = chatBuilder.defaultToolCallbacks(toolCallbackProvider)
                .build();
        PromptTemplate pt = new PromptTemplate(name);
        return this.chatClient.prompt(pt.create())
                .call()
                .content();
    }
}
