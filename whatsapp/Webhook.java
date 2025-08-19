package whatsapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class Webhook {
    private String object;
    private List<Entry> entry;

    @Data
    public static class Entry {
        private String id;
        private List<Change> changes;
    }

    @Data
    public static class Change {
        private Value value;
        private String field;
    }

    @Data
    public static class Value {
        @JsonProperty("messaging_product")
        private String messagingProduct;
        private Metadata metadata;
        private List<Contact> contacts;
        private List<Message> messages;
    }

    @Data
    public static class Metadata {
        @JsonProperty("display_phone_number")
        private String displayPhoneNumber;
        @JsonProperty("phone_number_id")
        private String phoneNumberId;
    }

    @Data
    public static class Contact {
        private Profile profile;
        @JsonProperty("wa_id")
        private String waId;
    }

    @Data
    public static class Profile {
        private String name;
    }

    @Data
    public static class Message {
        private Context context;
        private String from;
        private String id;
        private String timestamp;
        private String type;
        private Interactive interactive;
    }

    @Data
    public static class Context {
        private String from;
        private String id;
    }

    @Data
    public static class Interactive {
        private String type;
        @JsonProperty("list_reply")
        private ListReply listReply;
    }

    @Data
    public static class ListReply {
        private String id;
        private String title;
        private String description;
    }
}
