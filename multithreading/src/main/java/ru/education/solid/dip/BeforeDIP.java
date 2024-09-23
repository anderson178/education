package ru.education.solid.dip;

import ru.education.solid.Order;
import ru.education.solid.srp.AfterSRP;

public class BeforeDIP {
    public class OrderProcessor {
        public void process(Order order){

            AfterSRP.MySQLOrderRepository repository = new AfterSRP.MySQLOrderRepository();
            AfterSRP.ConfirmationEmailSender mailSender = new AfterSRP.ConfirmationEmailSender();

            if (order.isValid() && repository.save(order)) {
                mailSender.sendConfirmationEmail(order);
            }
        }
    }
}
