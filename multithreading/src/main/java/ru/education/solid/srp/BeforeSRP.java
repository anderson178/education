package ru.education.solid.srp;

import ru.education.solid.Order;

public class BeforeSRP {
    public class OrderProcessor {

        public void process(Order order) {
            if (save(order)) {
                sendConfirmationEmail(order);
            }
        }

        private boolean save(Order order) {
            // сохраняем заказ в базу данных

            return true;
        }

        private void sendConfirmationEmail(Order order) {
            // Шлем письмо клиенту
        }
    }
}
