### Общие сведения
низкоуровневый механизм TCP/UDP-соединения, работающий на любом уровне

### Блокирующий сокет
Как работает:
- Когда вы вызываете read() — поток останавливается и ждёт, пока не появятся данные.
- Аналогично: accept(), write() — тоже могут блокировать.

❌Проблема:
- Каждый клиент — отдельный поток.
- 10 000 клиентов → 10 000 потоков → высокое потребление памяти, переключение контекста.



### Пример
```java
ServerSocket server = new ServerSocket(8080);
while (true) {
    Socket socket = server.accept(); // ⛔ Блокируется, пока не подключится клиент

    // Новый поток на клиента
    new Thread(() -> {
        InputStream in = socket.getInputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = in.read(buffer); // ⛔ Блокируется, пока не придут данные
        // Обработка...
    }).start();
}
```

### Неблокирующий сокет
Как работает:
- Операции не останавливают поток.
- Если данных нет — read() возвращает 0 или null (в зависимости от API).
- Используется селектор (Selector) — может следить за многими сокетами сразу.

Селектор - позволяет выбрать определенный канал среди других каналов путем опроса каждого из каналов который готов к операции ввода-вывода

✅ Преимущества:
- Один поток может обрабатывать тысячи соединений.
- Нет накладных расходов на создание потоков.
- Подходит для высоконагруженных серверов (Netty, Vert.x, Reactor).

### Пример

```java
Selector selector = Selector.open();
ServerSocketChannel serverChannel = ServerSocketChannel.open();
serverChannel.configureBlocking(false); // 🔓 Неблокирующий режим
serverChannel.bind(new InetSocketAddress(8080));
serverChannel.register(selector, SelectionKey.OP_ACCEPT);

while (true) {
    selector.select(); // ⏳ Ждёт, пока что-то произойдёт (ввод/вывод)

    Set<SelectionKey> keys = selector.selectedKeys();
    for (SelectionKey key : keys) {
        if (key.isAcceptable()) {
            // Новое соединение
            SocketChannel client = serverChannel.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        }
        if (key.isReadable()) {
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead = client.read(buffer); // 🚫 Не блокируется!
            if (bytesRead > 0) {
                // Данные есть — обрабатываем
            } else if (bytesRead == -1) {
                client.close();
            }
        }
    }
    keys.clear();
}
```
