package com.portal.mcp_server.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import com.mbilashobane.ai.mcp_core.dto.search.Rides;
import com.portal.mcp_server.service.search.FindSlotTypeService;

@Service
public class FindRidesTool implements McpTool {
    private FindSlotTypeService findSlotTypeService;

    /**
     * Finds available rides based on the specified origin and destination.
     *
     * @param origin      The starting location for the ride search.
     * @param destination The final destination for the ride search.
     * @return A {@link Rides} object, which is currently a placeholder.
     */
    @Tool(name = "findRides", description = "Find available rides based on origin and destination.")
    public Rides findRides(@ToolParam(description = "The starting location of the ride.") String origin,
            @ToolParam(description = "The final destination of the ride.") String destination) {
        // Implementation for finding rides
        // This is a placeholder; actual implementation will depend on the application's
        // requirements
        Rides rides = Rides.builder()
                .origin(origin)
                .destination(destination)
                .appointees(new CopyOnWriteArrayList<>())
                .slotIds(new CopyOnWriteArrayList<>())
                .slots(new CopyOnWriteArrayList<>())
                .build();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("rides", rides);
        findSlotTypeService.handleRequest(context);
        return rides;
    }

    @Autowired
    public void setFindSlotTypeService(FindSlotTypeService findSlotTypeService) {
        this.findSlotTypeService = findSlotTypeService;
    }
}
