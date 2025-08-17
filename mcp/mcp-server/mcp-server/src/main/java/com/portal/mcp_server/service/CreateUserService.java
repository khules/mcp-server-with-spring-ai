package com.portal.mcp_server.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbilashobane.ai.mcp_core.dto.Driver;
import com.mbilashobane.ai.mcp_core.dto.SearchResponse;
import com.mbilashobane.ai.mcp_core.dto.Trip;
import com.mbilashobane.ai.mcp_core.dto.TripDetails;
import com.mbilashobane.ai.mcp_core.dto.User;
import com.mbilashobane.ai.mcp_core.dto.Vehicle;

public class CreateUserService extends AbstractRpcService {

    @Override
    public void handleRequest(StandardEvaluationContext context) {
        Expression nameExpression = expressionParser.parseExpression("driver.name");
        Expression phoneExpression = expressionParser.parseExpression("driver.contact");
        Expression vehicleRegExpression = expressionParser.parseExpression("vehicle.licensePlate");
        Expression priceExpression = expressionParser.parseExpression("tripDetails.price");
        User user = User.builder()
                .name(String.format("%s %s", nameExpression.getValue(context, String.class),
                        vehicleRegExpression.getValue(context, String.class)))
                .email(String.format("%s@%s", phoneExpression.getValue(context, String.class),
                        "mail.com"))
                .phone(phoneExpression.getValue(context, String.class))
                .login(String.format("%s@%s", phoneExpression.getValue(context, String.class),
                        "mail.com"))
                .password(phoneExpression.getValue(context, String.class))
                .newPassword(phoneExpression.getValue(context, String.class))
                .appoiCharge(priceExpression.getValue(context, Double.class))
                .build();
        User existingUser = checkIfUserExists(user.getPhone());

        if (existingUser == null) {
            SearchResponse response = odooRpcService.sendCreateRequest("res.users", user);
            user.setId(response.getResult().get(0));
        } else {
            existingUser.setActive(true);
            existingUser.setGroupsId(null);
            odooRpcService.sendWriteRequest("res.users", Collections.singletonList(existingUser.getId()), existingUser);
            user = existingUser;
        }
        context.setVariable("user", user);
        if (nextHandler != null) {
            nextHandler.handleRequest(context);
        }
    }

    private User checkIfUserExists(String phone) {
        // Logic to check if user already exists in the system
        // This is a placeholder for actual implementation
        SearchResponse response = odooRpcService.sendSearchRequest("res.users", Collections.singletonList(
                Arrays.asList("phone", "=", phone)));
        if (response != null && response.getResult() != null && !response.getResult().isEmpty()) {
            int id = response.getResult().get(0);
            List<User> users = odooRpcService.sendReadRequest("res.users",
                    Collections.singletonList(id),
                    Arrays.asList(), User.class);
            if (users != null && !users.isEmpty()) {
                return users.get(0);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        CreateUserService service = new CreateUserService();
        service.setExpressionParser(new SpelExpressionParser());

        OdooRpcService odooRpcService = new OdooRpcService(new ObjectMapper());
        odooRpcService.setRestTemplate(new RestTemplate());
        service.setOdooRpcService(odooRpcService);

        Trip trip = new Trip();
        TripDetails tripDetails = new TripDetails();
        tripDetails.setOrigin("New York");
        tripDetails.setDestination("Los Angeles");
        tripDetails.setDate("2025-08-10 20:38:40");
        tripDetails.setNumberOfPassengers(2);
        tripDetails.setPrice(100.0);
        trip.setTripDetails(tripDetails);

        Driver driver = new Driver();
        driver.setName("John Doe");
        driver.setContact("1234567890");
        trip.setDriver(driver);

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate("XYZ123");
        trip.setVehicle(vehicle);

        StandardEvaluationContext context = new StandardEvaluationContext(trip);
        service.handleRequest(context);
        System.out.println("User creation request sent successfully.");
    }
}
