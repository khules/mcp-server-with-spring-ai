package com.portal.mcp_server.service;

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
import com.mbilashobane.ai.mcp_core.dto.Type;
import com.mbilashobane.ai.mcp_core.dto.User;
import com.mbilashobane.ai.mcp_core.dto.Vehicle;

public class CreateSlotTypeService extends AbstractRpcService {

    @Override
    public void handleRequest(StandardEvaluationContext context) {

        Expression numberOfPassengersExpression = expressionParser.parseExpression("tripDetails.numberOfPassengers");
        Expression userIdExpression = expressionParser.parseExpression("#user.id");
        Integer userId = userIdExpression.getValue(context, Integer.class);

        Expression vehicleRegExpression = expressionParser.parseExpression("vehicle.licensePlate");
        String vehicleLicensePlate = vehicleRegExpression.getValue(context, String.class);

        SearchResponse response = odooRpcService.sendSearchRequest("appointement.type",
                List.of(List.of("name", "=", vehicleLicensePlate)));

        if (response.getResult().isEmpty()) {

            // for (int i = 0; i < 1; i++) {
            Type type = new Type();
            type.setName(String.format("%s", vehicleLicensePlate));
            type.setStaffUserIds(List.of(userId));
            odooRpcService.sendCreateRequest("appointement.type", type);

            // }
        } else {
            List<Type> types = odooRpcService.sendReadRequest("appointement.type", response.getResult(),
                    List.of(), Type.class);
            if (types != null && !types.isEmpty()) {
                Type existingType = types.get(0);

                odooRpcService.sendDeleteRequest("appointement.slots", existingType.getSlotIds());
            }
        }
        if (nextHandler != null) {
            nextHandler.handleRequest(context);
        }
    }

    public static void main(String[] args) {
        CreateSlotTypeService service = new CreateSlotTypeService();
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
        user.setId(15);
        context.setVariable("user", user);
        service.handleRequest(context);
        System.out.println("Slot type creation request sent successfully.");
    }
}
