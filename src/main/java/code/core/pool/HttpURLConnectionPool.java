package code.core.pool;

import code.core.protocol.Peer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public final class HttpURLConnectionPool extends AbstractObjectPool<HttpURLConnection> {

    public HttpURLConnectionPool(int size, Peer peer) {
        super(size);
        init(peer);
    }

    public void init(Peer peer) {
        for (int i = 0; i < size; i++) {
            String url = "Http://" + peer.getHost() + (i + 1) + ":" + peer.getPort();
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestMethod("POST");
                queue.add(httpURLConnection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
