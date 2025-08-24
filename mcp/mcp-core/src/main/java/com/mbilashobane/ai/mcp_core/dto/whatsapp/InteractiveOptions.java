
package com.mbilashobane.ai.mcp_core.dto.whatsapp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InteractiveOptions {

    @JsonProperty("messaging_product")
    private String messagingProduct;

    @JsonProperty("status")
    private String status;

    @JsonProperty("message_id")
    private String messageId;

    @JsonProperty("typing_indicator")
    private TypingIndicator typingIndicator;

    @JsonProperty("recipient_type")
    private String recipientType;

    @JsonProperty("to")
    private String to;

    @JsonProperty("type")
    private String type;

    @JsonProperty("interactive")
    private Interactive interactive;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TypingIndicator {
        @JsonProperty("type")
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Interactive {
        @JsonProperty("type")
        private String type;

        @JsonProperty("header")
        private Header header;

        @JsonProperty("body")
        private Body body;

        @JsonProperty("footer")
        private Footer footer;

        @JsonProperty("action")
        private Action action;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Header {
        @JsonProperty("type")
        private String type;

        @JsonProperty("text")
        private String text;

        @JsonProperty("image")
        private Image image;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Image {
        @JsonProperty("link")
        private String link;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Body {
        @JsonProperty("text")
        private String text;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Footer {
        @JsonProperty("text")
        private String text;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Action {
        @JsonProperty("button")
        private String button;

        @JsonProperty("sections")
        private Section[] sections;

        @JsonProperty("buttons")
        private Button[] buttons;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Button {
        @JsonProperty("type")
        private String type;

        @JsonProperty("reply")
        private Reply reply;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reply {
        @JsonProperty("id")
        private String id;

        @JsonProperty("title")
        private String title;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Section {
        @JsonProperty("title")
        private String title;

        @JsonProperty("rows")
        private Row[] rows;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Row {
        @JsonProperty("id")
        private String id;

        @JsonProperty("title")
        private String title;

        @JsonProperty("description")
        private String description;
    }
}
