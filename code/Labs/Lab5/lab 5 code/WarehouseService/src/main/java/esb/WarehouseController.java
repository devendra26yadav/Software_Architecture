package esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WarehouseController {

    @Autowired
    MonitoringGateway monitoringGateway;

    @PostMapping("/orders")
    public ResponseEntity<?> receiveOrder(@RequestBody Order order) {
        System.out.println("Warehouse Application receiving order: "+order);
        monitoringGateway.track("WAREHOUSE_ORDER_RECEIVED", order, "Warehouse accepted order.");
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }
}
