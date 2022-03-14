package code.message;

import com.alibaba.fastjson.JSON;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import sun.misc.IOUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MessageQueueServer {

    public MessageQueueServer() {
        Server server = new Server(3000);
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        ServletHolder servletHolder = new ServletHolder(new RequestServlet());
        servletContextHandler.addServlet(servletHolder, "/*");
        server.setHandler(servletContextHandler);
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class RequestServlet extends HttpServlet {
        // 处理 POST 请求
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String ackMessage = "success";
            // 获取请求过来的输入流
            InputStream inputStream = req.getInputStream();
            // 响应给客户端
            OutputStream outputStream = resp.getOutputStream();
            try {
                byte[] bytes = IOUtils.readFully(inputStream, inputStream.available(), true);
                MessageInfo messageInfo = JSON.parseObject(bytes, MessageInfo.class);
                MessageHandler handler = SubscriberRegistrationCenter.getInstance(messageInfo.getTopic());
                handler.handle(messageInfo.getMessage());
            } catch (IOException ioException) {
                ackMessage = "error";
            } finally {
                byte[] bytes = JSON.toJSONBytes(ackMessage);
                outputStream.write(bytes);
                outputStream.flush();
            }
        }
    }
}
