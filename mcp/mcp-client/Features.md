## Features List for `com.portal.mcp_client` Package

### Controllers

- **WhatsAppWebhookController**: Handles webhook verification for WhatsApp integration.
- **TripsController**: Exposes an API endpoint to create trips using the service chain.

### Services (Business Logic)

- **CreateTripService**: Orchestrates the trip creation process, delegating to other services in a chain.
- **CheckPaymentService**: Checks if a user has made the required payment before proceeding.
- **CreateUserService**: Creates a new user in the system if not already present.
- **CreateSlotTypeService**: Creates a new slot type (appointment type) if needed.
- **CreateSlotService**: Creates a new slot (appointment slot) for a trip.
- **CreateCategoryService**: Creates a new category for a trip, such as a group or batch.
- **OdooRpcService**: Handles communication with the Odoo backend via JSON-RPC for user, slot, type, and category operations.
- **AbstractRpcService**: Base class for RPC services, supporting a chain-of-responsibility pattern.
- **\_Config**: Spring configuration for wiring up the service chain and dependencies.

### DTOs (Data Transfer Objects)

- **Trip, TripDetails, Vehicle, Driver**: Represent trip data, including details, vehicle, and driver info.
- **User, Type, Slot, Cat**: Represent user, appointment type, slot, and category entities.
- **SearchResponse, JsonRpcRequest, JsonRpcResponse**: Generic wrappers for JSON-RPC communication.
- **Request/Response DTOs**: For create/read/write operations on User, Type, Slot, Cat.

### Patterns & Architecture

- **Chain of Responsibility**: Service classes are chained to process trip creation in steps.
- **Spring Boot**: Application is structured as a Spring Boot app with REST controllers and service beans.
- **Odoo Integration**: Uses JSON-RPC to interact with an Odoo backend for business operations.
