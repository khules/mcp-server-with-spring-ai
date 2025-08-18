package com.portal.mcp_server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbilashobane.ai.mcp_core.dto.Trip;
import com.portal.mcp_server.tools.RideAdvertisingTool;

@RestController
@RequestMapping("/api/rides")
public class RideAdvertisingController {

    private final RideAdvertisingTool rideAdvertisingTool;

    public RideAdvertisingController(RideAdvertisingTool rideAdvertisingTool) {
        this.rideAdvertisingTool = rideAdvertisingTool;
    }

    /**
     * Exposes the RideAdvertisingTool's advertiseRide method as a REST endpoint.
     * It accepts a Trip object as JSON and uses it to advertise a ride.
     *
     * @param trip The trip details, vehicle, and driver information.
     * @return The advertised trip information.
     */
    @PostMapping("/advertise")
    public ResponseEntity<Trip> advertiseRide(@RequestBody Trip trip) {
        // The request body is deserialized into a Trip object.
        // We then extract the details to call the existing tool method.
        Trip advertisedTrip = rideAdvertisingTool.advertiseRide(
                trip.getTripDetails().getOrigin(),
                trip.getTripDetails().getDestination(),
                trip.getTripDetails().getDate(),
                trip.getTripDetails().getNumberOfPassengers(),
                trip.getTripDetails().getPrice(),
                trip.getVehicle().getLicensePlate(),
                trip.getDriver().getName(),
                trip.getDriver().getContact());
        return ResponseEntity.ok(advertisedTrip);
    }
}