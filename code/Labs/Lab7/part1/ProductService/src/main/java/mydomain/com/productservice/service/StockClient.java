package mydomain.com.productservice.service;

import java.net.URI;
import java.util.List;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockClient {
    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;

    public StockClient(DiscoveryClient discoveryClient, RestTemplate restTemplate) {
        this.discoveryClient = discoveryClient;
        this.restTemplate = restTemplate;
    }

    public int getStock(int productNumber) {
        List<ServiceInstance> instances = discoveryClient.getInstances("stock-service");
        if (instances.isEmpty()) {
            return -1;
        }

        URI stockServiceUri = instances.get(0).getUri();
        String url = stockServiceUri + "/api/stock/" + productNumber;
        Integer quantity = restTemplate.getForObject(url, Integer.class);
        return quantity == null ? 0 : quantity;
    }
}
