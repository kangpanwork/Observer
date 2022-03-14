package code.core.message;

import code.core.AutoPublish;

public interface Message {
    @AutoPublish
    void setMessage(String message);
    String getMessage();
}
