package com.portal.mcp_server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mbilashobane.ai.mcp_core.dto.search.Rides;
import com.portal.mcp_server.dto.RideSearch;
import com.portal.mcp_server.tools.FindRidesTool;

@RestController
@RequestMapping("/api/rides")
public class RidesController {

    private final FindRidesTool findRidesTool;

    @Autowired
    public RidesController(FindRidesTool findRidesTool) {
        this.findRidesTool = findRidesTool;
    }

    /**
     * Finds available rides based on origin and destination.
     *
     * @param request The request body containing origin and destination.
     * @return A ResponseEntity containing the found rides.
     */
    @PostMapping("/search")
    public ResponseEntity<Rides> findRides(@RequestBody RideSearch request) {
        // Rides rides = findRidesTool.findRides(request.getOrigin(),
        // request.getDestination());
        // return ResponseEntity.ok(rides);
        return null;
    }
}
