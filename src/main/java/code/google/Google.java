package code.google;

import code.core.Message;

import java.util.EventObject;

/**
 * 谷歌的事件
 */
public class Google extends EventObject implements Message {

    private String message;

    public Google(String source) {
        super(source);
        this.message = source;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

}
