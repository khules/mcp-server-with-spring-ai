package com.portal.mcp_server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mbilashobane.ai.mcp_core.dto.ExecuteParams;
import com.mbilashobane.ai.mcp_core.dto.JsonRpcRequest;
import com.mbilashobane.ai.mcp_core.dto.JsonRpcResponse;
import com.mbilashobane.ai.mcp_core.dto.SearchResponse;
import com.mbilashobane.ai.mcp_core.dto.Type;
import com.mbilashobane.ai.mcp_core.dto.TypeReadJsonRpcResponse;
import com.mbilashobane.ai.mcp_core.dto.User;
import com.mbilashobane.ai.mcp_core.dto.UserReadJsonRpcResponse;

public class OdooRpcService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OdooRpcService.class);
    private final ObjectMapper objectMapper;
    private RestTemplate restTemplate;

    public OdooRpcService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.copy();
        // Configure for pretty printing, which is helpful for debugging
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public String buildUserCreateRequest() throws JsonProcessingException {
        // 1. Create the User object with data from the JSON
        User user = User.builder()
                .login("0790857968@mail.com")
                .password("0790857968")
                .newPassword("0790857968")
                .companiesCount(1)
                .companyIds(Collections.singletonList(1))
                .name("LM44SZGP")
                .email("0790857968@mail.com")
                .workExp("5")
                .appoiCharge(250.0)
                .build();

        // 2. Create the arguments list, wrapping the user object in a list as per the
        // JSON structure
        List<Object> args = Arrays.asList("ridealong", "2", "***", "res.users", "create",
                Collections.singletonList(user));

        // 3. Create the params object
        ExecuteParams params = ExecuteParams.builder().service("object").method("execute").args(args).build();

        // 4. Create the final JSON-RPC request object
        JsonRpcRequest<ExecuteParams> rpcRequest = JsonRpcRequest.<ExecuteParams>builder().jsonrpc("2.0").method("call")
                .params(params).id(123).build();

        // 5. Serialize the request object to a JSON string
        return objectMapper.writeValueAsString(rpcRequest);
    }

    public <R> SearchResponse sendCreateRequest(String model, R params) {
        JsonRpcRequest<ExecuteParams> request = createRequest(model, params);
        try {
            String requestJson = objectMapper.writeValueAsString(request);
            LOGGER.info("Sending create request: {}", requestJson);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error serializing search request", e);
        }
        String url = "http://a35531458b52b46a2b9dd395c3497980-1396126055.us-east-1.elb.amazonaws.com:8059/jsonrpc";
        SearchResponse response = restTemplate.postForObject(url, request, SearchResponse.class);
        LOGGER.info("Response from Odoo: {}", response);
        return response;
    }

    public SearchResponse sendSearchRequest(String model, List<Object> domain) {
        JsonRpcRequest<ExecuteParams> request = createSearchRequest(model, domain);
        try {
            String requestJson = objectMapper.writeValueAsString(request);
            LOGGER.info("Sending search request: {}", requestJson);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error serializing search request", e);
        }
        String url = "http://a35531458b52b46a2b9dd395c3497980-1396126055.us-east-1.elb.amazonaws.com:8059/jsonrpc";
        SearchResponse response = restTemplate.postForObject(url, request, SearchResponse.class);
        LOGGER.info("Response from Odoo: {}", response);
        return response;
    }

    public <P> void sendWriteRequest(String model, List<Integer> ids, P payload) {
        JsonRpcRequest<ExecuteParams> request = createWriteRequest(model, ids, payload);
        try {
            String requestJson = objectMapper.writeValueAsString(request);
            LOGGER.info("Sending write request: {}", requestJson);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error serializing write request", e);
        }
        String url = "http://a35531458b52b46a2b9dd395c3497980-1396126055.us-east-1.elb.amazonaws.com:8059/jsonrpc";
        String response = restTemplate.postForObject(url, request, String.class);
        LOGGER.info("Response from Odoo: {}", response);
    }

    @SuppressWarnings("unchecked")
    public <R> R sendReadRequest(String model, List<Integer> ids, List<String> fields, Class<?> c) {
        {
            JsonRpcRequest<ExecuteParams> request = createReadRequest(model, ids, fields);
            try {
                String requestJson = objectMapper.writeValueAsString(request);
                LOGGER.info("Sending read request: {}", requestJson);
            } catch (JsonProcessingException e) {
                LOGGER.error("Error serializing read request", e);
                return null;
            }
            String url = "http://a35531458b52b46a2b9dd395c3497980-1396126055.us-east-1.elb.amazonaws.com:8059/jsonrpc";

            try {
                LOGGER.info(c.getDeclaredConstructor().newInstance().getClass().getCanonicalName());

                if (c.getDeclaredConstructor().newInstance().getClass().getCanonicalName()
                        .equals("com.mbilashobane.ai.mcp_core.dto.User")) {

                    JsonRpcResponse<List<User>> responseJson = restTemplate.postForObject(url, request,
                            UserReadJsonRpcResponse.class);
                    return (R) responseJson.getResult();

                } else if (c.getDeclaredConstructor().newInstance().getClass().getCanonicalName()
                        .equals("com.mbilashobane.ai.mcp_core.dto.Type")) {

                    JsonRpcResponse<List<Type>> responseJson = restTemplate.postForObject(url, request,
                            TypeReadJsonRpcResponse.class);
                    return (R) responseJson.getResult();

                } else {
                    String response = restTemplate.postForObject(url, request, String.class);
                    LOGGER.info("Response from Odoo: {}", response);
                    return (R) response;

                }
            } catch (Exception e) {
                LOGGER.error("Error sending read request", e);
            }
            return null;
        }
    }

    public <R> JsonRpcRequest<ExecuteParams> createRequest(String model, R params) {
        ExecuteParams executeParams = ExecuteParams.builder()
                .service("object")
                .method("execute")
                .args(Arrays.asList("ridealong", "2", "ODOO@jik008", model, "create",
                        Collections.singletonList(params)))
                .build();

        return JsonRpcRequest.<ExecuteParams>builder()
                .jsonrpc("2.0")
                .method("call")
                .params(executeParams)
                .id(123)
                .build();
    }

    public JsonRpcRequest<ExecuteParams> createSearchRequest(String model, List<Object> domain) {
        ExecuteParams executeParams = ExecuteParams.builder()
                .service("object")
                .method("execute")
                .args(Arrays.asList("ridealong", "2", "ODOO@jik008", model, "search", domain))
                .build();

        return JsonRpcRequest.<ExecuteParams>builder()
                .jsonrpc("2.0")
                .method("call")
                .params(executeParams)
                .id(123)
                .build();
    }

    public JsonRpcRequest<ExecuteParams> createReadRequest(String model, List<Integer> ids, List<String> fields) {
        List<Object> executeArgs = new ArrayList<>(
                Arrays.asList("ridealong", "2", "ODOO@jik008", model, "read", ids));
        if (fields != null && !fields.isEmpty()) {
            executeArgs.add(fields);
        }

        ExecuteParams executeParams = ExecuteParams.builder()
                .service("object")
                .method("execute")
                .args(executeArgs)
                .build();

        return JsonRpcRequest.<ExecuteParams>builder()
                .jsonrpc("2.0")
                .method("call")
                .params(executeParams)
                .id(123)
                .build();
    }

    public JsonRpcRequest<ExecuteParams> createWriteRequest(String model, List<Integer> ids, Object payload) {
        List<Object> executeArgs = new ArrayList<>(
                Arrays.asList("ridealong", "2", "ODOO@jik008", model, "write", ids, payload));

        ExecuteParams executeParams = ExecuteParams.builder()
                .service("object")
                .method("execute")
                .args(executeArgs)
                .build();

        return JsonRpcRequest.<ExecuteParams>builder()
                .jsonrpc("2.0")
                .method("call")
                .params(executeParams)
                .id(123)
                .build();
    }

    /**
     * Sends a delete request to the Odoo 17 JSON-RPC API for the specified model
     * and record IDs.
     *
     * @param model the Odoo model name (e.g., "res.users")
     * @param ids   the list of record IDs to delete
     */
    public void sendDeleteRequest(String model, List<Integer> ids) {
        JsonRpcRequest<ExecuteParams> request = createDeleteRequest(model, ids);
        try {
            String requestJson = objectMapper.writeValueAsString(request);
            LOGGER.info("Sending delete request: {}", requestJson);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error serializing delete request", e);
        }
        String url = "http://a35531458b52b46a2b9dd395c3497980-1396126055.us-east-1.elb.amazonaws.com:8059/jsonrpc";
        String response = restTemplate.postForObject(url, request, String.class);
        LOGGER.info("Response from Odoo: {}", response);
    }

    /**
     * Creates a JSON-RPC request for deleting records in Odoo.
     *
     * @param model the Odoo model name
     * @param ids   the list of record IDs to delete
     * @return the JSON-RPC request object
     */
    public JsonRpcRequest<ExecuteParams> createDeleteRequest(String model, List<Integer> ids) {
        List<Object> executeArgs = new ArrayList<>(
                Arrays.asList("ridealong", "2", "ODOO@jik008", model, "unlink", ids));

        ExecuteParams executeParams = ExecuteParams.builder()
                .service("object")
                .method("execute")
                .args(executeArgs)
                .build();

        return JsonRpcRequest.<ExecuteParams>builder()
                .jsonrpc("2.0")
                .method("call")
                .params(executeParams)
                .id(123)
                .build();
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static void main(String[] args) {
        OdooRpcService service = new OdooRpcService(new ObjectMapper());
        service.setRestTemplate(new RestTemplate());
        try {
            // User user = service.sendReadRequest("res.users", Arrays.asList(12),
            // Arrays.asList("name", "email"), User.class);
            // LOGGER.info("Read user: {}", user != null ? user.getName() : "No user
            // found");

            // // Example of getting the type of an instantiated variable
            // if (user != null) {
            // LOGGER.info("The type of the user object is: {}", user.getClass().getName());
            // LOGGER.info("User details: ID={}, Name={}, Email={}", user.getActive(),
            // user.getName(),
            // user.getEmail());

            // service.sendWriteRequest("res.users", Arrays.asList(user.getId()),
            // Collections.singletonMap("active", true));
            // LOGGER.info("User with ID {} has been activated.", user.getId());
        } catch (Exception e) {
            LOGGER.error("Error processing JSON", e);
        }
    }

}