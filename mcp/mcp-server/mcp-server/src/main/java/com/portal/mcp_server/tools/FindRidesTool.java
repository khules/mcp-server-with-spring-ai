package com.portal.mcp_server.tools;

import java.util.ArrayList;

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
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Tool(name = "validateRideInformation", description = "returns a valid json ride search criteria that contains origin and destination.")
    public RideSearch validateRideInformation(
            @ToolParam(description = "The starting location of the ride.") String origin,
            @ToolParam(description = "The final destination of the ride.") String destination) {
        try {

            RideSearch rides = RideSearch.builder()
                    .origin(origin)
                    .destination(destination)
                    .build();
            String rideResponse = objectMapper.writeValueAsString(rides);
            logger.info("Finding rides from {} to {}:  {}", origin, destination, rideResponse);
            return rides;
        } catch (Exception e) {
            logger.error("Error processing JSON", e);
            return new RideSearch(); // Return an empty RideSearch object in case of error
        }
    }
}
