package code.test;

import code.baidu.Baidu;
import code.core.PublisherExecutor;
import code.google.Google;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        PublisherExecutor publisherExecutor = PublisherExecutor.build();
        Baidu baidu = new Baidu("Baidu");
        List<String> list = new ArrayList<>();
        list.add("G");
        list.add("o");
        list.add("o");
        list.add("g");
        list.add("l");
        list.add("e");
        Google google = new Google(list);
        publisherExecutor.publishEvent(baidu);
        publisherExecutor.publishEvent(google);
    }
}
