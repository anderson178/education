### Общие сведения
Набор предварительно созданных объектов соединений с базой данных, которые могут быть повторно использованы приложениями. Вместо создания нового соединения каждый раз, когда приложение взаимодействует с базой данных, пул соединений позволяет многократное использование уже существующих соединений, что значительно увеличивает производительность приложения и уменьшает время, необходимое для выполнения операций с базой данных.

### Плюсы
1. **Производительность**: Создание соединения с базой данных является относительно дорогой операцией, требующей времени и ресурсов. Использование пула соединений позволяет избежать этого накладного времени для повторяющихся операций.
2. **Меньше ресурсов**: Пул соединений помогает ограничить количество активных соединений с базой данных, что важно для управления нагрузкой и предотвращения исчерпания ресурсов базы данных.
3. **Управление временем жизни соединений**: Пулы соединений могут управлять временем жизни соединений, поддерживать или закрывать неактивные соединения, тем самым лучше управляя ресурсами.
4. **Управление транзакциями**: Пулы могут интегрироваться с управлениями транзакциями, обеспечивая более чистую и безопасную работу с базой данных.

### Варианты библиотек
1. Apache DBCP
2. HikariCP - самый популярный
3. C3P0
 
### Пример
```java
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionPool {
    
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/your_database"); // URL вашей базы данных
        config.setUsername("your_username"); // Имя пользователя
        config.setPassword("your_password"); // Пароль
        
        // Настройки пула
        config.setMaximumPoolSize(10); // Максимальное количество соединений в пуле
        config.setMinimumIdle(5); // Минимальное количество неактивных соединений в пуле
        config.setIdleTimeout(30000); // Время ожидания (в миллисекундах) перед закрытием неактивных соединений
        config.setConnectionTimeout(30000); // Время ожидания (в миллисекундах) для получения соединения из пула
        config.setMaxLifetime(1800000); // Максимальное время жизни соединения (в миллисекундах)
        
        dataSource = new HikariDataSource(config);
    }

    // Метод для получения соединения
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
```

```java
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseExample {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnectionPool.getConnection()) {
            System.out.println("Соединение получено успешно!");

            // Создание и выполнение запроса
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM your_table");

            while (resultSet.next()) {
                System.out.println("Данные: " + resultSet.getString("your_column"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```