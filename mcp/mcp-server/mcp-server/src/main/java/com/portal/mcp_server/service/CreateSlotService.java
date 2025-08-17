package com.portal.mcp_server.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
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
import com.mbilashobane.ai.mcp_core.dto.Slot;
import com.mbilashobane.ai.mcp_core.dto.Trip;
import com.mbilashobane.ai.mcp_core.dto.TripDetails;
import com.mbilashobane.ai.mcp_core.dto.User;
import com.mbilashobane.ai.mcp_core.dto.Vehicle;

public class CreateSlotService extends AbstractRpcService {

    @Override
    public void handleRequest(StandardEvaluationContext context) {
        Expression dateExpression = expressionParser.parseExpression("tripDetails.date");
        String dateString = dateExpression.getValue(context, String.class);
        LocalDateTime date = LocalDateTime.parse(dateString, shortFormatter);
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        int hour = date.getHour();

        SearchResponse slots = odooRpcService.sendSearchRequest("appointement.slots",
                List.of(List.of("start_hour", "=", hour)));
        if (slots.getResult().isEmpty()) {

            SearchResponse types = odooRpcService.sendSearchRequest("appointement.type",
                    List.of(List.of("name", "like",
                            expressionParser.parseExpression("vehicle.licensePlate").getValue(context, String.class)
                                    + "%")));

            Slot slot = new Slot();

            slot.setWeekday(getDayName(dayOfWeek));
            slot.setAppointmentTypeId(types.getResult().get(0));
            slot.setStartDatetime(date.format(shortFormatter));
            slot.setEndDatetime(date.plusHours(1).format(shortFormatter));
            slot.setStartHour(date.getHour());
            slot.setEndHour(date.getHour() + 1);
            slot.setDuration(1.0);
            Expression phoneExpression = expressionParser.parseExpression("driver.contact");
            String phone = phoneExpression.getValue(context, String.class);

            SearchResponse response = odooRpcService.sendSearchRequest("res.partner", Collections.singletonList(
                    Arrays.asList("phone", "=", phone)));

            context.setVariable("appointees", response.getResult().get(0));

            SearchResponse searchResponse = odooRpcService.sendCreateRequest("appointement.slots", slot);
            Expression userExpression = expressionParser.parseExpression("#user");
            User user = userExpression.getValue(context, User.class);
            user.setSlotIds(List.of(searchResponse.getResult().get(0)));

            Expression appointeesExpression = expressionParser.parseExpression("#appointees");
            Integer appointees = appointeesExpression.getValue(context, Integer.class);

            user.setLogin(null);
            user.setPassword(null);
            user.setNewPassword(null);
            user.setShare(null);
            user.setState(null);
            user.setActivePartner(null);
            user.setCompaniesCount(null);
            user.setCompanyIds(null);
            user.setNotificationType(null);
            user.setGroupsId(null);
            odooRpcService.sendWriteRequest("res.partner", Collections.singletonList(appointees), user);
        }

        if (nextHandler != null) {
            nextHandler.handleRequest(context);
        }
    }

    private String getDayName(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return "Monday";
            case TUESDAY:
                return "Tuesday";
            case WEDNESDAY:
                return "Wednesday";
            case THURSDAY:
                return "Thursday";
            case FRIDAY:
                return "Friday";
            case SATURDAY:
                return "Saturday";
            case SUNDAY:
                return "Sunday";
            default:
                return "Sunday";
        }
    }

    public static void main(String[] args) {
        CreateSlotService service = new CreateSlotService();
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
    }

}
