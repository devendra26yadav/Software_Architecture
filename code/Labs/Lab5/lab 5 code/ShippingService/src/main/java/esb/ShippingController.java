package esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShippingController {

    @Autowired
    MonitoringGateway monitoringGateway;

    @PostMapping("/orders")
    public ResponseEntity<?> receiveOrder(@RequestBody Order order) {
        System.out.println("ShippingService received order (default): " + order);
        monitoringGateway.track("SHIPPING_DEFAULT_RECEIVED", order, "Shipping endpoint /orders called.");
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    @PostMapping("/orders/normal")
    public ResponseEntity<?> receiveNormalOrder(@RequestBody Order order) {
        System.out.println("ShippingService received normal order: " + order);
        monitoringGateway.track("SHIPPING_NORMAL_RECEIVED", order, "Shipping endpoint /orders/normal called.");
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    @PostMapping("/orders/next-day")
    public ResponseEntity<?> receiveNextDayOrder(@RequestBody Order order) {
        System.out.println("ShippingService received next-day order: " + order);
        monitoringGateway.track("SHIPPING_NEXT_DAY_RECEIVED", order, "Shipping endpoint /orders/next-day called.");
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    @PostMapping("/orders/international")
    public ResponseEntity<?> receiveInternationalOrder(@RequestBody Order order) {
        System.out.println("ShippingService received international order: " + order);
        monitoringGateway.track("SHIPPING_INTERNATIONAL_RECEIVED", order, "Shipping endpoint /orders/international called.");
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }
}
