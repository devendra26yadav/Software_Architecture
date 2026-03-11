package edu.miu.sa.lab11.kafka.service;

import edu.miu.sa.lab11.kafka.domain.Order;
import edu.miu.sa.lab11.kafka.domain.OrderStatus;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderFactory {

    public List<Order> basicOrders() {
        return List.of(
                order("ORD-1001", "Alice", "USA", "120.50", OrderStatus.NEW),
                order("ORD-1002", "Bob", "Canada", "89.99", OrderStatus.PROCESSING),
                order("ORD-1003", "Carla", "India", "250.00", OrderStatus.COMPLETED),
                order("ORD-1004", "David", "Germany", "64.75", OrderStatus.NEW),
                order("ORD-1005", "Elena", "Brazil", "410.10", OrderStatus.PROCESSING)
        );
    }

    public List<Order> offsetOrders(int batchNumber) {
        return List.of(
                order("OFF-" + batchNumber + "-01", "Fatima", "UAE", "91.00", OrderStatus.NEW),
                order("OFF-" + batchNumber + "-02", "George", "USA", "155.40", OrderStatus.PROCESSING),
                order("OFF-" + batchNumber + "-03", "Haruto", "Japan", "79.25", OrderStatus.COMPLETED)
        );
    }

    public List<Order> competingOrders() {
        return List.of(
                order("CMP-2001", "Isla", "UK", "33.40", OrderStatus.NEW),
                order("CMP-2002", "Jon", "USA", "99.95", OrderStatus.NEW),
                order("CMP-2003", "Kamal", "India", "101.15", OrderStatus.NEW),
                order("CMP-2004", "Lina", "Jordan", "208.20", OrderStatus.NEW),
                order("CMP-2005", "Marta", "Spain", "57.45", OrderStatus.NEW)
        );
    }

    public List<Order> partitionedOrders() {
        return List.of(
                order("PRT-3001", "Noah", "USA", "75.00", OrderStatus.NEW),
                order("PRT-3002", "Olivia", "Canada", "132.50", OrderStatus.NEW),
                order("PRT-3003", "Pavel", "Ukraine", "48.60", OrderStatus.NEW),
                order("PRT-3004", "Quinn", "USA", "501.99", OrderStatus.NEW),
                order("PRT-3005", "Ravi", "India", "210.00", OrderStatus.NEW),
                order("PRT-3006", "Sara", "Egypt", "87.30", OrderStatus.NEW)
        );
    }

    public List<Order> blockingRetryOrders() {
        return List.of(
                order("BLK-4001", "Tara", "USA", "44.00", OrderStatus.NEW),
                order("BLK-4002", "Umar", "Pakistan", "145.75", OrderStatus.REJECTED),
                order("BLK-4003", "Vera", "France", "88.20", OrderStatus.COMPLETED)
        );
    }

    public List<Order> retryableOrders() {
        return List.of(
                order("RET-5001", "Wendy", "USA", "70.00", OrderStatus.NEW),
                order("RET-5002", "Xavier", "Mexico", "310.15", OrderStatus.REJECTED),
                order("RET-5003", "Yasmin", "Morocco", "29.99", OrderStatus.COMPLETED)
        );
    }

    public List<Order> batchOrders(String prefix) {
        return List.of(
                order(prefix + "-01", "Zane", "USA", "20.00", OrderStatus.NEW),
                order(prefix + "-02", "Asha", "India", "21.00", OrderStatus.NEW),
                order(prefix + "-03", "Ben", "Canada", "22.00", OrderStatus.NEW),
                order(prefix + "-04", "Chen", "China", "23.00", OrderStatus.NEW),
                order(prefix + "-05", "Dina", "Egypt", "24.00", OrderStatus.NEW),
                order(prefix + "-06", "Evan", "USA", "25.00", OrderStatus.NEW)
        );
    }

    public List<Order> transactionalOrders() {
        return List.of(
                order("TX-6001", "Farah", "USA", "199.00", OrderStatus.NEW),
                order("TX-6002", "Gio", "Italy", "299.00", OrderStatus.NEW),
                order("TX-6003", "Hana", "Japan", "399.00", OrderStatus.NEW),
                order("TX-6004", "Ivan", "Bulgaria", "499.00", OrderStatus.NEW),
                order("TX-6005", "Jill", "USA", "599.00", OrderStatus.NEW)
        );
    }

    private Order order(
            String orderNumber,
            String customerName,
            String customerCountry,
            String amount,
            OrderStatus status) {
        return new Order(orderNumber, customerName, customerCountry, new BigDecimal(amount), status);
    }
}
