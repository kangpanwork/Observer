package code.core;

public interface Message {
    @AutoPublish
    void setMessage(String message);
    String getMessage();
}
