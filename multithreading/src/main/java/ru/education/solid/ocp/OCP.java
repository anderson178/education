package ru.education.solid.ocp;

import ru.education.solid.Order;
import ru.education.solid.srp.AfterSRP.OrderProcessor;

public class OCP {
    public static class OrderProcessorWithPreAndPostProcessing extends OrderProcessor {

        @Override
        public void process(Order order) {
            beforeProcessing();
            super.process(order);
            afterProcessing();
        }

        private void beforeProcessing() {
            // Осуществим некоторые действия перед обработкой заказа
        }

        private void afterProcessing() {
            // Осуществим некоторые действия после обработки заказа
        }
    }
}
