package code.test;

import code.message.MessageInfo;
import code.message.MessageProduct;

public class BaiduMessageProduct implements MessageProduct {

    private MessageInfo messageInfo;

    @Override
    public void setMessageInfo(MessageInfo messageInfo) {
        this.messageInfo = messageInfo;
    }
}
