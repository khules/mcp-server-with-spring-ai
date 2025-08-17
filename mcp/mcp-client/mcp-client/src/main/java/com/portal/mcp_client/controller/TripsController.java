// package com.portal.mcp_client.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.expression.spel.support.StandardEvaluationContext;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.mbilashobane.ai.mcp_core.dto.Trip;
// import com.portal.mcp_client.service.CreateTripService;

// @RestController
// @RequestMapping("/api")
// public class TripsController {
// private CreateTripService createTripService;

// @PostMapping("/trips")
// public void createTrip(@RequestBody Trip trip) {
// StandardEvaluationContext context = new StandardEvaluationContext(trip);
// createTripService.handleRequest(context);
// }

// @Autowired
// public void setCreateTripService(CreateTripService createTripService) {
// this.createTripService = createTripService;
// }

// }
