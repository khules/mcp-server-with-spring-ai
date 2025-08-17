// package com.portal.mcp_client.service;

// import com.mbilashobane.ai.mcp_core.dto.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.ArgumentCaptor;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.Spy;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.expression.spel.standard.SpelExpressionParser;
// import org.springframework.expression.spel.support.StandardEvaluationContext;

// import java.util.Collections;
// import java.util.List;
// import java.util.Map;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyList;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class CreateUserServiceTest {

// @Mock
// private OdooRpcService odooRpcService;

// @Mock
// private RpcService nextHandler;

// @Spy
// private SpelExpressionParser expressionParser = new SpelExpressionParser();

// @InjectMocks
// private CreateUserService createUserService;

// private StandardEvaluationContext context;

// @BeforeEach
// void setUp() {
// // Setup common test data
// Trip trip = new Trip();
// TripDetails tripDetails = new TripDetails();
// tripDetails.setPrice(100.0);
// trip.setTripDetails(tripDetails);

// Driver driver = new Driver();
// driver.setName("John Doe");
// driver.setContact("1234567890");
// trip.setDriver(driver);

// Vehicle vehicle = new Vehicle();
// vehicle.setLicensePlate("XYZ123");
// trip.setVehicle(vehicle);

// context = new StandardEvaluationContext(trip);

// // Inject dependencies
// createUserService.setExpressionParser(expressionParser);
// createUserService.setNextHandler(nextHandler);
// }

// @Test
// void handleRequest_whenUserDoesNotExist_shouldCreateUser() {
// // Arrange
// SearchResponse searchResponse =
// SearchResponse.builder().result(Collections.emptyList()).build();
// when(odooRpcService.sendSearchRequest(eq("res.users"),
// anyList())).thenReturn(searchResponse);

// // Act
// createUserService.handleRequest(context);

// // Assert
// ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
// verify(odooRpcService).sendCreateRequest(eq("res.users"),
// userCaptor.capture());
// User capturedUser = userCaptor.getValue();

// assertEquals("John Doe XYZ123", capturedUser.getName());
// assertEquals("1234567890@mail.com", capturedUser.getEmail());
// assertEquals("1234567890", capturedUser.getPhone());
// assertEquals(100.0, capturedUser.getAppoiCharge());

// verify(odooRpcService, never()).sendWriteRequest(anyString(), anyList(),
// any());
// verify(nextHandler).handleRequest(context);
// }

// @Test
// void handleRequest_whenUserExists_shouldUpdateUserAndSetActive() {
// // Arrange
// User existingUser =
// User.builder().id(99).phone("1234567890").active(false).build();
// SearchResponse searchResponse =
// SearchResponse.builder().result(Collections.singletonList(99)).build();
// when(odooRpcService.sendSearchRequest(eq("res.users"),
// anyList())).thenReturn(searchResponse);
// when(odooRpcService.sendReadRequest(eq("res.users"),
// eq(Collections.singletonList(99)), anyList(),
// eq(User.class)))
// .thenReturn(Collections.singletonList(existingUser));

// // Act
// createUserService.handleRequest(context);

// // Assert
// ArgumentCaptor<List<Integer>> idsCaptor =
// ArgumentCaptor.forClass(List.class);
// @SuppressWarnings("rawtypes")
// ArgumentCaptor<Map> payloadCaptor = ArgumentCaptor.forClass(Map.class);

// verify(odooRpcService, never()).sendCreateRequest(anyString(), any());
// verify(odooRpcService).sendWriteRequest(eq("res.users"), idsCaptor.capture(),
// payloadCaptor.capture());

// assertEquals(Collections.singletonList(99), idsCaptor.getValue());
// assertEquals(Collections.singletonMap("active", true),
// payloadCaptor.getValue());

// verify(nextHandler).handleRequest(context);
// }
// }