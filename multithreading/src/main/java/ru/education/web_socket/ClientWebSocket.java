package ru.education.web_socket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class ClientWebSocket extends WebSocketClient {

    public ClientWebSocket(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Connected to server");
        send("Hello, server!"); // Отправка сообщения серверу
    }

    @Override
    public void onMessage(String s) {
        System.out.println("Message from server: " + s);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Closed connection: " + reason);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }


    public static void main(String[] args) throws Exception {
        WebSocketClient client = new ClientWebSocket(new URI("ws://127.0.0.1:8083"));
        client.connect();
        while (!client.isOpen()) {
            Thread.sleep(100); // Ждем 100 мс
        }
        client.send("Hello, server!");
        Thread.sleep(5000); // Ждем 5 секунд, чтобы увидеть полученные сообщения
        client.close();
    }
}