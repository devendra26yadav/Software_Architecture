package edu.miu.sa.lab9.part1.productcommand.data;

import edu.miu.sa.lab9.part1.productcommand.domain.Product;
import edu.miu.sa.lab9.part1.productcommand.domain.ProductStore;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MongoProductStore implements ProductStore {

    private final SpringDataProductRepository repository;

    public MongoProductStore(SpringDataProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Product> findByProductNumber(String productNumber) {
        return repository.findById(productNumber).map(this::toDomain);
    }

    @Override
    public Product save(Product product) {
        return toDomain(repository.save(new ProductDocument(
                product.productNumber(),
                product.name(),
                product.price()
        )));
    }

    @Override
    public void deleteByProductNumber(String productNumber) {
        repository.deleteById(productNumber);
    }

    private Product toDomain(ProductDocument document) {
        return new Product(document.getProductNumber(), document.getName(), document.getPrice());
    }
}

