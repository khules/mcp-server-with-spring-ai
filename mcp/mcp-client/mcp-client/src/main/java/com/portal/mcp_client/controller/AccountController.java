package com.portal.mcp_client.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private final ChatClient chatClient;


    public AccountController(ChatClient.Builder chat, ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = chat.defaultTools(toolCallbackProvider)
                .build();
    }


    @GetMapping("/account")
    public String getAccount(@RequestParam("q") String name) {
        PromptTemplate pt = new PromptTemplate(name);
        return this.chatClient.prompt(pt.create())
                .call()
                .content();
    }
}
