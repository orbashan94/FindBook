package war.of.findbook.entities;

public class Message {
    private String userId;
    private String body;
    private String name;
    private long timestamp;

    @Override
    public String toString() {
        return "Message{" +
                "userId='" + userId + '\'' +
                ", body='" + body + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public Message(){ }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public Message(String userId, String body, String name, long timestamp) {
        this.userId = userId;
        this.body = body;
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getBody() {
        return body;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Message setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public Message setBody(String body) {
        this.body = body;
        return this;
    }

    public Message setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
