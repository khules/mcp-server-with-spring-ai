package com.portal.mcp_server.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import com.mbilashobane.ai.mcp_core.dto.Driver;
import com.mbilashobane.ai.mcp_core.dto.Trip;
import com.mbilashobane.ai.mcp_core.dto.TripDetails;
import com.mbilashobane.ai.mcp_core.dto.Vehicle;
import com.mbilashobane.ai.mcp_core.dto.search.Rides;
import com.portal.mcp_server.service.CreateTripService;

/**
 * A Spring component that provides a tool for advertising a ride.
 * This tool is intended for use by a Large Language Model (LLM) to
 * publish a ride offering with all necessary details.
 */
@Service
public class RideAdvertisingTool implements McpTool {
        private CreateTripService createTripService;
        private static final Logger logger = LoggerFactory.getLogger(RideAdvertisingTool.class);

        /**
         * This method advertises a ride offering to potential passengers.
         *
         * @param origin             The starting location of the ride.
         * @param destination        The final destination of the ride.
         * @param date               The date and time the ride is scheduled for.
         * @param numberOfPassengers The number of available seats or passengers.
         * @param price              The price of the ride per person.
         * @param licensePlate       The license plate number of the vehicle.
         * @param name               The name of the driver offering the ride.
         * @param contact            The contact number for the driver.
         * @return A string confirming that the ride has been successfully advertised.
         */
        @Tool(name = "advertiseRide", description = "Used to advertise a ride offering to potential passengers. It publishes the ride's origin, destination, date, available seats, price, and driver/vehicle details.")
        public Trip advertiseRide(
                        @ToolParam(description = "The starting location of the ride.") String origin,
                        @ToolParam(description = "The final destination of the ride.") String destination,
                        @ToolParam(description = "The date and time the ride is scheduled for.") String date,
                        @ToolParam(description = "The number of available seats or passengers.") int numberOfPassengers,
                        @ToolParam(description = "The price of the ride per person.") double price,
                        @ToolParam(description = "The license plate number of the vehicle.") String licensePlate,
                        @ToolParam(description = "The name of the driver offering the ride.") String name,
                        @ToolParam(description = "The contact number for the driver.") String contact) {
                // In a real application, you would add logic here to interact with a database
                // or an external service to publish the ride.
                logger.info(
                                "Attempting to advertise a ride with the following details: Origin: {}, Destination: {}, Date: {}, Passengers: {}, Price: ${}, Vehicle: {}, Driver: {} ({})",
                                origin,
                                destination,
                                date,
                                numberOfPassengers,
                                String.format("%.2f", price),
                                licensePlate,
                                name, contact);

                Trip trip = Trip.builder()
                                .tripDetails(TripDetails.builder()
                                                .origin(origin)
                                                .destination(destination)
                                                .date(date)
                                                .numberOfPassengers(numberOfPassengers)
                                                .price(price)
                                                .build())
                                .vehicle(Vehicle.builder()
                                                .licensePlate(licensePlate)
                                                .build())
                                .driver(Driver.builder()
                                                .name(name)
                                                .contact(contact)
                                                .build())
                                .build();
                try {
                        StandardEvaluationContext context = new StandardEvaluationContext(trip);
                        createTripService.handleRequest(context);
                        // Log the successful advertisement of the ride
                        logger.info("Ride advertised on createTripService successfully: {}", trip);
                } catch (Exception e) {
                        logger.error("Error occurred while advertising ride: {}", e);
                }
                return trip;
        }

        @Autowired
        public void setCreateTripService(CreateTripService createTripService) {
                this.createTripService = createTripService;
        }
}
