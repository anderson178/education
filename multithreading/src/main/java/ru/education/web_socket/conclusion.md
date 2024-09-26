## Общие сведения 
WebSocket — это протокол для двусторонней связи между клиентом и сервером по постоянному соединению. Он позволяет обмениваться данными в реальном времени, используя единую TCP-сессию, что делает его более эффективным, чем традиционные HTTP-запросы.

### Механизм работы WebSocket

1. **Установление соединения**:
    - Классический HTTP-запрос отправляется клиентом на сервер с заголовком `Upgrade`, который указывает на то, что клиент хочет использовать WebSocket.
    - Если сервер поддерживает WebSocket, он отвечает с кодом состояния 101 (Switching Protocols) и устанавливает соединение.

2. **Двусторонняя связь**:
    - После установления соединения клиент и сервер могут обмениваться данными в обоих направлениях. Это может быть выполнено с использованием текстовых или бинарных сообщений.

3. **Закрытие соединения**:
    - Соединение может быть закрыто как с клиентской, так и с серверной стороны. Закрытие происходит через специальные сообщения, которые указывают на причины завершения.
   
## Пример
```xml
<dependency>
   <groupId>org.java-websocket</groupId>
   <artifactId>Java-WebSocket</artifactId>
   <version>1.5.7</version>
</dependency>
```
### Клиент
```java
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
```
### Сервер
```java
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
```