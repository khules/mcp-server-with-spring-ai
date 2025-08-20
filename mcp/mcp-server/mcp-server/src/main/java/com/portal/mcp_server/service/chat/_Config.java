package com.portal.mcp_server.service.chat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration("chatConfig")
public class _Config {
    private final RestTemplate restTemplate;

    public _Config(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Bean
    public ChatClient chatClient() {
        ChatClient chatClient = new ChatClient();
        chatClient.setRestTemplate(restTemplate);
        return chatClient;
    }

}
