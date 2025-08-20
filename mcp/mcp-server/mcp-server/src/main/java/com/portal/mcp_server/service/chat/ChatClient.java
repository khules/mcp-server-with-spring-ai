package com.portal.mcp_server.service.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.mbilashobane.ai.mcp_core.dto.search.Rides;

public class ChatClient {
    private static final Logger logger = LoggerFactory.getLogger(ChatClient.class);
    private RestTemplate restTemplate;

    public Rides findRides(String chatMessage) {
        String url = String.format("http://mcp-client-service:8040/account?q=%s %s", chatMessage,
                "return results from tool as json returned by the tool");
        try {
            return restTemplate.getForObject(url, Rides.class);
        } catch (Exception e) {
            logger.error("Error fetching rides from API", e);
            return new Rides();
        }
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
