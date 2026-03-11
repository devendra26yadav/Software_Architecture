package mydomain.com.productservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "StockService")
public interface StockClient {
    @GetMapping("/stock/{productNumber}")
    int getStock(@PathVariable String productNumber);
}
