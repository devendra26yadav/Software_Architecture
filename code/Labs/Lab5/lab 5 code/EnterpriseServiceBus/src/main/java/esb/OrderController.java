package esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    @Qualifier("warehouseChannel")
    MessageChannel warehouseChannel;

    @Autowired
    MonitoringGateway monitoringGateway;

    @PostMapping("/orders")
    public ResponseEntity<?> receiveOrder(@RequestBody Order order) {
        monitoringGateway.track("ESB_ORDER_RECEIVED", order, "Order received by ESB and sent to warehouse.");
        Message<Order> orderMessage = MessageBuilder.withPayload(order).build();
        warehouseChannel.send(orderMessage);
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }
}
