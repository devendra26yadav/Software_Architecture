package mydomain.com.productservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "stockservice",url = "http://localhost:8901")
public interface StockServiceClient {

    @GetMapping("/stock/{productNumber}")
    int getStock(@PathVariable("productNumber") String productNumber);
}
