package code.transport;

import java.io.InputStream;

public interface TransportClient {
    void connect();
    InputStream write(InputStream inputStream);
}
