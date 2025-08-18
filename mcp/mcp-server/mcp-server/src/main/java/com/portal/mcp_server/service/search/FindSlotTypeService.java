package com.portal.mcp_server.service.search;

import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbilashobane.ai.mcp_core.dto.Driver;
import com.mbilashobane.ai.mcp_core.dto.Trip;
import com.mbilashobane.ai.mcp_core.dto.TripDetails;
import com.mbilashobane.ai.mcp_core.dto.User;
import com.mbilashobane.ai.mcp_core.dto.Vehicle;
import com.portal.mcp_server.service.AbstractRpcService;
import com.portal.mcp_server.service.OdooRpcService;

public class FindSlotTypeService extends AbstractRpcService {

    @Override
    public void handleRequest(StandardEvaluationContext context) {

        FindAppointees findAppointees = new FindAppointees(expressionParser, odooRpcService);
        findAppointees.find(context);
        if (nextHandler != null) {
            nextHandler.handleRequest(context);
        }

    }

    public static void main(String[] args) {
        FindSlotTypeService service = new FindSlotTypeService();
        service.setExpressionParser(new SpelExpressionParser());

        OdooRpcService odooRpcService = new OdooRpcService(new ObjectMapper());
        odooRpcService.setRestTemplate(new RestTemplate());
        service.setOdooRpcService(odooRpcService);
        TripDetails tripDetails = new TripDetails();
        tripDetails.setNumberOfPassengers(3);
        tripDetails.setOrigin("Johannesburg");
        tripDetails.setDestination("Cape Town");
        tripDetails.setPrice(100.0);
        tripDetails.setDate("2025-08-17 20:38:40");

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
    }

}
