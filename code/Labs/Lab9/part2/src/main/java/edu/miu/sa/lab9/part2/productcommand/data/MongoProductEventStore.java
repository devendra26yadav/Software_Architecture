package edu.miu.sa.lab9.part2.productcommand.data;

import edu.miu.sa.lab9.part2.productcommand.domain.ProductEventRecord;
import edu.miu.sa.lab9.part2.productcommand.domain.ProductEventStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongoProductEventStore implements ProductEventStore {

    private final SpringDataProductEventRepository repository;

    public MongoProductEventStore(SpringDataProductEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProductEventRecord> findByProductNumber(String productNumber) {
        return repository.findByProductNumberOrderByVersionAsc(productNumber).stream().map(this::toDomain).toList();
    }

    @Override
    public ProductEventRecord append(ProductEventRecord eventRecord) {
        ProductEventDocument saved = repository.save(new ProductEventDocument(
                eventRecord.productNumber(),
                eventRecord.changeType(),
                eventRecord.name(),
                eventRecord.price(),
                eventRecord.version(),
                eventRecord.createdAt()
        ));
        return toDomain(saved);
    }

    private ProductEventRecord toDomain(ProductEventDocument document) {
        return new ProductEventRecord(
                document.getProductNumber(),
                document.getChangeType(),
                document.getName(),
                document.getPrice(),
                document.getVersion(),
                document.getCreatedAt()
        );
    }
}

