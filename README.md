# Spring AI integration with MCP (Model Context Protocol)

The [Model Context Protocol (MCP)](https://modelcontextprotocol.io/docs/concepts/architecture) is a standardized protocol that enables AI models to interact with external tools and resources in a structured way. It supports multiple transport mechanisms to provide flexibility across different environments.

The MCP Java SDK provides a Java implementation of the Model Context Protocol, enabling standardized interaction with AI models and tools through both synchronous and asynchronous communication patterns.

Spring AI MCP extends the MCP Java SDK with Spring Boot integration, providing both client and server starters. Bootstrap your AI applications with MCP support using Spring Initializer.

# MCP Java SDK Architecture

The Java MCP implementation follows a three-layer architecture

- Client/Server Layer: The McpClient handles client-side operations while the McpServer manages server-side protocol operations. Both utilize McpSession for communication management.

- Session Layer (McpSession): Manages communication patterns and state through the DefaultMcpSession implementation.

- Transport Layer (McpTransport): Handles JSON-RPC message serialization and deserialization with support for multiple transport implementations.

# [MCP Client](https://modelcontextprotocol.io/sdk/java/mcp-client)

The MCP Client is a key component in the Model Context Protocol (MCP) architecture, responsible for establishing and managing connections with MCP servers.

# [MCP Client](https://modelcontextprotocol.io/sdk/java/mcp-server)

The MCP Server is a foundational component in the Model Context Protocol (MCP) architecture that provides tools, resources, and capabilities to clients.

# What is MCP ?

MCP follows a client-server architecture that revolves around several key components:

**MCP Host:** is our main application that integrates with an LLM and requires it to connect with external data sources

**MCP Clients:** are components that establish and maintain 1:1 connections with MCP servers

**MCP Servers:** are components that integrate with external data sources and expose functionalities to interact with them

**Tools**: refer to the executable functions/methods that the MCP servers expose for clients to invoke
Additionally, to handle communication between clients and servers, MCP provides two transport channels.

To enable communication through standard input and output streams with local processes and command-line tools, it provides the Standard Input/Output (stdio) transport type. Alternatively, for HTTP-based communication between clients and servers, it provides the Server-Sent Events (SSE) transport type.

# What is done in the example ?

In this turorial MCP Server connects to Postgresql DB and its fetches data, we have also exposed two Tools in MCP Server. MCP Client connects to MCP Server and based on the Prompt LLM decides which Tool to connect.

## Step 1 Create Postgresql DB

Run docker-compose.yml file and run the table.sql to create table.

## Step 2 Run ollama locally

Download [ollama](https://ollama.com/) and run any [model](https://ollama.com/search) as per you machine capacity

## Step 3 Creating MCP Server

Download the mcp-server project and run it.

Important classes to note - SellerAccountTools

```
@Service
public class SellerAccountTools {
    private final SellerAccountRepository sellerAccountRepository;

    public SellerAccountTools(SellerAccountRepository sellerAccountRepository) {
        this.sellerAccountRepository = sellerAccountRepository;
    }

    @Tool(name = "Search Seller account by name", description = "Find all Seller Accounts by name")
    public String getAccountByName(
            @ToolParam(description = "Seller Account Name") String name) {
        List<SellerAccount> accountList = sellerAccountRepository.findByName(name);
        StringBuilder result = new StringBuilder();
        for (SellerAccount account : accountList) {
            result.append(account.toString()).append("\n");
        }
        return result.toString();
    }

    @Tool(description = "Find all Seller Accounts by owner")
    public String getAccountByOwner(
            @ToolParam(description = "Seller Account owner") String owner) {
        List<SellerAccount> accountList = sellerAccountRepository.findByOwner(owner);
        StringBuilder result = new StringBuilder();
        for (SellerAccount account : accountList) {
            result.append(account.toString()).append("\n");
        }
        return result.toString();
    }
}
```
Explanation 
- Tools allows server to expose tools that can be invoked by LLM, for this example i have created two tools which will search from account table by Name and by Owner.
- Based  on the prompt AI LLM will automatocally pick the correct method but we need to write name and descrition properly 

## Step 4 Creating MCP Client

Download the mcp-client project and run.

## Step 5 Testing

### DB Table Data

![image](https://github.com/user-attachments/assets/adb49692-dc26-4fb1-99db-a4656ed5ffbb)

### Testing with Promts

![image](https://github.com/user-attachments/assets/4921c7a3-793b-47ce-baf9-7298cb3f04eb)

Here you can observe that based on the prompt LLM is automatically selecting the right tool and returning the results and it also format it.

