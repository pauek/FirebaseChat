package info.pauek.firebase_chat.Data;

public class Message {
    public String text;
    public String user;

    public Message() {}

    public Message(String text, String user) {
        this.text = text;
        this.user = user;
    }
}

