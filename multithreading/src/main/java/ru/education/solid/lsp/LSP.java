package ru.education.solid.lsp;

import ru.education.solid.Item;
import ru.education.solid.Order;

public class LSP {
    public class OrderStockValidator {
        public boolean isValid(Order order) {
            for (Item item : order.getItems()) {
                if (! item.isInStock()) {
                    return false;
                }
            }

            return true;
        }
    }

    public class OrderStockAndPackValidator extends OrderStockValidator {
        @Override
        public boolean isValid(Order order) {
            for (Item item : order.getItems()) {
                if ( !item.isInStock() || !item.isPacked() ){
                    throw new IllegalStateException(
                            String.format("Order %d is not valid!", order.getId())
                    );
                }
            }

            return true;
        }
    }
}
