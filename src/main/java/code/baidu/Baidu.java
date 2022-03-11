package code.baidu;

import java.util.EventObject;

/**
 * 百度的事件
 */
public class Baidu extends EventObject {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Baidu(String source) {
        super(source);
        this.message = source;
    }

}
