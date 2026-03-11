package mydomain.com.productservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockClient {
    private final RestTemplate restTemplate;

    public StockClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public int getStock(int productNumber) {
        String url = "http://stock-service/api/stock/" + productNumber;
        Integer quantity = restTemplate.getForObject(url, Integer.class);
        return quantity == null ? 0 : quantity;
    }
}
