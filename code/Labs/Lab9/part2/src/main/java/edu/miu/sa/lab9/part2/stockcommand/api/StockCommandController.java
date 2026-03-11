package edu.miu.sa.lab9.part2.stockcommand.api;

import edu.miu.sa.lab9.part2.stockcommand.application.StockCommandService;
import edu.miu.sa.lab9.part2.stockcommand.domain.StockLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stocks")
public class StockCommandController {

    private final StockCommandService stockCommandService;

    public StockCommandController(StockCommandService stockCommandService) {
        this.stockCommandService = stockCommandService;
    }

    @PutMapping("/{productNumber}")
    public StockLevel updateStock(@PathVariable String productNumber, @Valid @RequestBody StockRequest request) {
        return stockCommandService.upsertStock(productNumber, request.quantity());
    }

    @DeleteMapping("/{productNumber}")
    public void deleteStock(@PathVariable String productNumber) {
        stockCommandService.deleteStock(productNumber);
    }

    public record StockRequest(@Min(0) int quantity) {
    }
}

