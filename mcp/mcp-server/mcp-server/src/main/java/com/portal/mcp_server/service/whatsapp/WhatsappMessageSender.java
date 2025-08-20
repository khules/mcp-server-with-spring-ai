package com.portal.mcp_server.service.whatsapp;

import org.springframework.http.HttpRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import java.io.IOException;

import org.springframework.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WhatsappMessageSender {
    private static final Logger logger = LoggerFactory.getLogger(WhatsappMessageSender.class);
    private RestTemplate restTemplate;

    public void sendMessage(String payload) {
        String url = "https://graph.facebook.com/v23.0/676947338845382/messages";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization",
                "Bearer EAAcKDkUinZAkBPDF5QcsWYvSmZAmCxQjYc0HGk9FDyy39kK8tOZBqfPqnFDZAEkaXlEHNlvlLy757nZBuZAZAgITL2seOeD2L5nLuFT7R7uV2iZBtACnYAfrL3zz5FvbQomrZAh0p17wYN1r5ZAmqskXbJzv27aJM5Cb5SppjlBIBHPpFjt86LwcxVlgYwyuM4QAZDZD");
        // String response = restTemplate.postForObject(url, payload, String.class,
        // headers);

        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);
        String response = restTemplate.postForEntity(url, requestEntity, String.class).getBody();
        logger.info("Response from WhatsApp API: {}", response);

    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
