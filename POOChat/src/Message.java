import java.io.Serializable;

public class Message implements Serializable {
    private String sender;
    private Object content;

    public Message(String sender, Object content) {
        this.sender = sender;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public Object getMessage() {
        return content;
    }
}
