package ru.education.web_socket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class ServerWebSocket extends WebSocketServer {

    public ServerWebSocket(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("New connection from: " + webSocket.getRemoteSocketAddress());
        webSocket.send("Welcome to the WebSocket server!"); // Отправка сообщениия клиенту
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println("Closed connection: " + webSocket.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println("Message from client: " + s);
        webSocket.send("Echo: " + s); // Отправка ответа клиенту
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Server started on port: " + this.getPort());
    }

    public static void main(String[] args) {
        ServerWebSocket serverWebSocket = new ServerWebSocket(new InetSocketAddress("127.0.0.1",8083));
        serverWebSocket.onStart();
        serverWebSocket.start();
    }
}
