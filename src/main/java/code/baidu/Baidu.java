package code.baidu;

import code.core.Message;

import java.util.EventObject;

/**
 * 百度的事件
 */
public class Baidu extends EventObject implements Message {

    private String message;

    public Baidu(String source) {
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
