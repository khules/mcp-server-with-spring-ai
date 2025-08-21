package com.portal.mcp_server.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbilashobane.ai.mcp_core.dto.search.Rides;
import com.portal.mcp_server.dto.RideSearch;

@Service
public class FindRidesTool implements McpTool {
    private static final Logger logger = LoggerFactory.getLogger(FindRidesTool.class);
    private final Map<String, Rides> ridesMap = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Tool(name = "validateRideInformation", description = "confirms that ride information is valid that the ride search criteria that contains origin and destination are valid for the contact number.")
    public String validateRideInformation(
            @ToolParam(description = "The starting location of the ride.") String origin,
            @ToolParam(description = "The final destination of the ride.") String destination,
            @ToolParam(description = "The contact number for the ride.") String contactNumber) {
        try {

            Rides rides = Rides.builder()
                    .origin(origin)
                    .destination(destination)

                    .build();
            String rideResponse = objectMapper.writeValueAsString(rides);
            logger.info("Finding rides from {} to {}:  {}", origin, destination, rideResponse);
            ridesMap.put(contactNumber, rides);
            return "Ride information is valid";
        } catch (Exception e) {
            logger.error("Error processing JSON", e);
            return "Ride information is invalid"; // Return an empty string in case of error
        }
    }

    public Rides getRides(String contact) {
        return ridesMap.remove(contact);
    }
}
