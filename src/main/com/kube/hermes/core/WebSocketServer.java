package com.kube.hermes.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketServer {
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private final ConcurrentHashMap<SocketChannel, String> connectedClients = new ConcurrentHashMap<>();

    public void start(int port) throws IOException {
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("WebSocket server started on port " + port);

        while (true) {
            selector.select();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();

                if (key.isAcceptable()) {
                    acceptConnection();
                } else if (key.isReadable()) {
                    handleMessage(key);
                }
            }
        }
    }

    private void acceptConnection() throws IOException {
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        connectedClients.put(clientChannel, clientChannel.getRemoteAddress().toString());
        System.out.println("New WebSocket connection: " + clientChannel.getRemoteAddress());
    }

    private void handleMessage(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = clientChannel.read(buffer);

        if (bytesRead == -1) {
            closeConnection(clientChannel);
            return;
        }

        buffer.flip();
        String message = new String(buffer.array(), 0, bytesRead).trim();
        System.out.println("Received WebSocket message: " + message);

        // Echo the message back to the client
        broadcastMessage(clientChannel, "Echo: " + message);
    }

    private void broadcastMessage(SocketChannel sender, String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        for (SocketChannel client : connectedClients.keySet()) {
            if (client.isOpen() && client != sender) {
                client.write(buffer.duplicate());
            }
        }
    }

    private void closeConnection(SocketChannel clientChannel) throws IOException {
        System.out.println("Closing WebSocket connection: " + clientChannel.getRemoteAddress());
        connectedClients.remove(clientChannel);
        clientChannel.close();
    }

    public static void main(String[] args) throws IOException {
        new WebSocketServer().start(9000);
    }
}
