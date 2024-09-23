package ru.education.solid.dip;

import ru.education.solid.Order;

public class AfterDIP {
    public interface MailSender {
        void sendConfirmationEmail(Order order);
    }

    public interface OrderRepository {
        boolean save(Order order);
    }

    public static class ConfirmationEmailSender implements MailSender {

        @Override
        public void sendConfirmationEmail(Order order) {
            // Шлем письмо клиенту
        }

    }

    public static class MySQLOrderRepository implements OrderRepository {

        @Override
        public boolean save(Order order) {
            // сохраняем заказ в базу данных
            return true;
        }
    }

    public static class OrderProcessor {

        private final MailSender mailSender;
        private final OrderRepository repository;

        public OrderProcessor(MailSender mailSender, OrderRepository repository) {
            this.mailSender = mailSender;
            this.repository = repository;
        }

        public void process(Order order){
            if (order.isValid() && repository.save(order)) {
                mailSender.sendConfirmationEmail(order);
            }
        }
    }
}
