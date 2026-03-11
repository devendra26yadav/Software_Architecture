package mydomain.com.productservice.service;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final StockServiceClient stockServiceClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    public ProductService(
            StockServiceClient stockServiceClient,
            CircuitBreakerFactory<?, ?> circuitBreakerFactory
    ) {
        this.stockServiceClient = stockServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public String getProduct(String productNumber) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("stockService");
        Integer stocks = circuitBreaker.run(
                () -> stockServiceClient.getStock(productNumber),
                throwable -> null
        );

        if (stocks == null) {
            return "Product " + productNumber + " stock information is unavailable";
        }

        return "Product " + productNumber + " has " + stocks + " stocks";
    }
}
