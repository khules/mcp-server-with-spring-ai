package com.portal.mcp_server.service;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbilashobane.ai.mcp_core.dto.Cat;
import com.mbilashobane.ai.mcp_core.dto.Driver;
import com.mbilashobane.ai.mcp_core.dto.SearchResponse;
import com.mbilashobane.ai.mcp_core.dto.Trip;
import com.mbilashobane.ai.mcp_core.dto.TripDetails;
import com.mbilashobane.ai.mcp_core.dto.User;
import com.mbilashobane.ai.mcp_core.dto.Vehicle;

public class CreateCategoryService extends AbstractRpcService {
    private RpcService nextHandler;

    @Override
    public void setNextHandler(RpcService rpcService) {
        this.nextHandler = rpcService;
    }

    @Override
    public void handleRequest(StandardEvaluationContext context) {
        Expression originExpression = expressionParser.parseExpression("tripDetails.origin");
        String origin = originExpression.getValue(context, String.class);

        Expression destinationExpression = expressionParser.parseExpression("tripDetails.destination");
        String destination = destinationExpression.getValue(context, String.class);

        Expression vehicleRegExpression = expressionParser.parseExpression("vehicle.licensePlate");
        String vehicleReg = vehicleRegExpression.getValue(context, String.class);

        Expression phoneExpression = expressionParser.parseExpression("driver.contact");
        String phone = phoneExpression.getValue(context, String.class);

        SearchResponse response = odooRpcService.sendSearchRequest("appointees.category", Arrays.asList(
                Arrays.asList("origin", "=", origin),
                Arrays.asList("destination", "=", destination)));

        if (response.getResult().isEmpty()) {

            response = odooRpcService.sendSearchRequest("res.partner", Collections.singletonList(
                    Arrays.asList("phone", "=", phone)));
            context.setVariable("appointees", response.getResult().get(0));

            String name = String.format("%s>>>>%s", origin, destination, vehicleReg);

            Cat cat = new Cat();
            cat.setName(name);
            cat.setOrigin(origin);
            cat.setDestination(destination);
            cat.setAppointees(Arrays.asList(response.getResult().get(0)));
            odooRpcService.sendCreateRequest("appointees.category", cat);
        }
        if (nextHandler != null) {
            nextHandler.handleRequest(context);
        }
    }

    public static void main(String[] args) {
        CreateCategoryService service = new CreateCategoryService();
        service.setExpressionParser(new SpelExpressionParser());
        OdooRpcService odooRpcService = new OdooRpcService(new ObjectMapper());
        odooRpcService.setRestTemplate(new RestTemplate());
        service.setOdooRpcService(odooRpcService);
        TripDetails tripDetails = new TripDetails();
        tripDetails.setNumberOfPassengers(3);
        tripDetails.setOrigin("New York");
        tripDetails.setDestination("Los Angeles");
        tripDetails.setPrice(100.0);
        tripDetails.setDate("2025-08-10 20:38:40");

        Driver driver = new Driver();
        driver.setName("John Doe");
        driver.setContact("1234567890");

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("XYZ123");

        Trip trip = new Trip();
        trip.setTripDetails(tripDetails);
        trip.setDriver(driver);
        trip.setVehicle(vehicle);

        StandardEvaluationContext context = new StandardEvaluationContext(trip);

        User user = new User();
        user.setId(21);
        context.setVariable("user", user);
        service.handleRequest(context);
        System.out.println("Appointment type creation request sent successfully.");
    }

}
