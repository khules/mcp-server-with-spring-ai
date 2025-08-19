package com.portal.mcp_server.service;

import java.io.IOException;
import java.util.Collections;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration("mcpServerConfig")
public class _Config {

    @Bean
    CreateTripService createTripService() {
        CreateTripService createTripService = new CreateTripService();
        createTripService.setOdooRpcService(odooRpcService());
        createTripService.setExpressionParser(expressionParser());
        createTripService.setNextHandler(createUserService());
        return createTripService;
    }

    @Bean
    CheckPaymentService checkPaymentService() {
        CheckPaymentService checkPaymentService = new CheckPaymentService();
        checkPaymentService.setOdooRpcService(odooRpcService());
        checkPaymentService.setExpressionParser(expressionParser());
        checkPaymentService.setNextHandler(createUserService());
        return checkPaymentService;
    }

    @Bean
    CreateUserService createUserService() {
        CreateUserService createUserService = new CreateUserService();
        createUserService.setOdooRpcService(odooRpcService());
        createUserService.setExpressionParser(expressionParser());
        createUserService.setNextHandler(createSlotTypeService());
        return createUserService;
    }

    @Bean
    CreateSlotTypeService createSlotTypeService() {
        CreateSlotTypeService createSlotTypeService = new CreateSlotTypeService();
        createSlotTypeService.setOdooRpcService(odooRpcService());
        createSlotTypeService.setExpressionParser(expressionParser());
        createSlotTypeService.setNextHandler(createSlotService());
        return createSlotTypeService;
    }

    @Bean
    CreateSlotService createSlotService() {
        CreateSlotService createSlotService = new CreateSlotService();
        createSlotService.setOdooRpcService(odooRpcService());
        createSlotService.setExpressionParser(expressionParser());
        createSlotService.setNextHandler(createCategoryService());
        return createSlotService;
    }

    @Bean
    CreateCategoryService createCategoryService() {
        CreateCategoryService createCategoryService = new CreateCategoryService();
        createCategoryService.setOdooRpcService(odooRpcService());
        createCategoryService.setExpressionParser(expressionParser());
        return createCategoryService;
    }

    @Bean
    ExpressionParser expressionParser() {
        return new SpelExpressionParser();
    }

    @Bean
    OdooRpcService odooRpcService() {
        OdooRpcService odooRpcService = new OdooRpcService(new ObjectMapper());
        odooRpcService.setRestTemplate(restTemplate());
        return odooRpcService;
    }

    @Bean
    RestTemplate restTemplate() {
        // You can customize the HttpClient here, for example, adding a connection pool
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);

        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;
    }

}
