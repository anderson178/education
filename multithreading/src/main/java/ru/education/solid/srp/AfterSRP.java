package ru.education.solid.srp;

import ru.education.solid.Order;

public class AfterSRP {

    public static class OrderProcessor {
        public void process(Order order) {

            MySQLOrderRepository repository = new MySQLOrderRepository();
            ConfirmationEmailSender mailSender = new ConfirmationEmailSender();

            if (repository.save(order)) {
                mailSender.sendConfirmationEmail(order);
            }
        }
    }

    public static class MySQLOrderRepository {
        public boolean save(Order order) {
            // сохраняем заказ в базу данных

            return true;
        }
    }

    public static class ConfirmationEmailSender {
        public void sendConfirmationEmail(Order order) {

            // Шлем письмо клиенту
        }
    }
}
