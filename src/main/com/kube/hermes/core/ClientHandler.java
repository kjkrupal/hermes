package com.kube.hermes.core;

import com.kube.hermes.http.Controller;
import com.kube.hermes.http.HttpRequest;
import com.kube.hermes.http.HttpResponse;
import com.kube.hermes.http.Router;
import com.kube.hermes.middleware.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;

public class ClientHandler implements Runnable {
    private final SelectionKey key;
    private final Router router;
    private static final List<Middleware> middlewares = List.of(
            new LoggingMiddleware(),
            new JwtMiddleware(),
            new CorsMiddleware(),
            new RateLimitMiddleware()
    );

    public ClientHandler(SelectionKey key, Router router) {
        this.key = key;
        this.router = router;
    }

    @Override
    public void run() {
        try {
            SocketChannel clientChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            int bytesRead = clientChannel.read(buffer);

            if (bytesRead == -1) {
                clientChannel.close();
                return;
            }

            buffer.flip();
            String requestData = new String(buffer.array(), 0, bytesRead);
            HttpRequest request = HttpRequest.parse(requestData);

            MiddlewareChain chain = new MiddlewareChain(middlewares);
            HttpResponse response = chain.next(request);

            if (response == null) {
                Controller controller = router.getController(request.getPath());
                response = controller != null ? controller.handle(request) : new HttpResponse(404, "Not Found", "{\"error\": \"Route not found\"}");
            }

            buffer.clear();
            buffer.put(response.formatResponse().getBytes());
            buffer.flip();
            while (buffer.hasRemaining()) {
                clientChannel.write(buffer);
            }
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
