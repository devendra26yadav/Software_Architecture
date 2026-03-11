package edu.miu.sa.lab9.part1.productquery.data;

import edu.miu.sa.lab9.part1.productquery.domain.ProductView;
import edu.miu.sa.lab9.part1.productquery.domain.ProductViewStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MongoProductViewStore implements ProductViewStore {

    private final SpringDataProductViewRepository repository;

    public MongoProductViewStore(SpringDataProductViewRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ProductView> findByProductNumber(String productNumber) {
        return repository.findById(productNumber).map(this::toDomain);
    }

    @Override
    public ProductView save(ProductView productView) {
        return toDomain(repository.save(new ProductViewDocument(
                productView.productNumber(),
                productView.name(),
                productView.price(),
                productView.numberInStock()
        )));
    }

    @Override
    public void deleteByProductNumber(String productNumber) {
        repository.deleteById(productNumber);
    }

    @Override
    public List<ProductView> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    private ProductView toDomain(ProductViewDocument document) {
        return new ProductView(
                document.getProductNumber(),
                document.getName(),
                document.getPrice(),
                document.getNumberInStock()
        );
    }
}

