package code.transport;

import code.core.pool.HttpURLConnectionPool;
import code.core.protocol.Peer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class HttpTransportClient implements TransportClient {

    private HttpTransportClient() {

    }

    public static HttpTransportClient build() {
        return Builder.httpTransportClient;
    }

    public static class Builder {
        private static final HttpTransportClient httpTransportClient = new HttpTransportClient();
    }

    private final HttpURLConnectionPool httpURLConnectionPool =
            new HttpURLConnectionPool(10, new Peer("127.0.0.", 3000));

    public HttpURLConnectionPool getHttpURLConnectionPool() {
        return httpURLConnectionPool;
    }

    private HttpURLConnection httpURLConnection;

    public HttpURLConnection getHttpURLConnection() {
        return httpURLConnection;
    }

    @Override
    public void connect() {
        try {
            httpURLConnection = httpURLConnectionPool.get();
            httpURLConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InputStream write(InputStream inputStream) {
        try {
            OutputStream outputStream = httpURLConnection.getOutputStream();
            byte[] bytes = new byte[1024 * 4];
            int len;
            while (-1 != (len = inputStream.read(bytes))) {
                // 输出流，输出给这个 POST 请求
                outputStream.write(bytes, 0, len);
            }
            // Cannot write output after reading input.(在 Main 方法用了同一个 MessageQueueClient 会报错)
            // 加状态码判断，该请求已经结束了，若要再写入数据，需要发起一个新的请求
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return httpURLConnection.getInputStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpURLConnection.getErrorStream();
    }
}
