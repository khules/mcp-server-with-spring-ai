package com.portal.mcp_server.tools;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbilashobane.ai.mcp_core.dto.search.Rides;

@Service
public class FindRidesTool implements McpTool {
    private static final Logger logger = LoggerFactory.getLogger(FindRidesTool.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Tool(name = "findRides", description = "Find available rides based on (origin, from , pickup location) and (destination, to.")
    public String findRides(
            @ToolParam(description = "The starting location of the ride.") String origin,
            @ToolParam(description = "The final destination of the ride.") String destination) {
        try {

            Rides rides = Rides.builder()
                    .origin(origin)
                    .destination(destination)
                    .appointees(new ArrayList<>())
                    .slotIds(new ArrayList<>())
                    .slots(new ArrayList<>())
                    .build();
            String rideResponse = objectMapper.writeValueAsString(rides);
            logger.info("Finding rides from {} to {}", origin, destination);
            return rideResponse;
        } catch (Exception e) {
            logger.error("Error processing JSON", e);
            return "Error processing request";
        }
    }
}
