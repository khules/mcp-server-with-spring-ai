// package com.portal.mcp_client;

// import org.springframework.ai.chat.client.ChatClient;
// import org.springframework.ai.chat.model.ChatModel;
// import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class ChatClientConfig {

// @Bean
// public ChatClient.Builder vertexAiChatClientBuilder(VertexAiGeminiChatModel
// chatModel) {
// return ChatClient.builder(chatModel);
// }
// }