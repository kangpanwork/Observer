package code.google;

import java.util.EventObject;
import java.util.List;

/**
 * 谷歌的事件
 */
public class Google extends EventObject {

    private List<String> message;

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public Google(List<String> source) {
        super(source);
        this.message = source;
    }

}
