package edu.miu.sa.lab9.part2.productcommand.data;

import edu.miu.sa.lab9.part2.productcommand.domain.ProductChangeType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document("part2_product_events")
public class ProductEventDocument {

    @Id
    private String id;
    private String productNumber;
    private ProductChangeType changeType;
    private String name;
    private BigDecimal price;
    private long version;
    private Instant createdAt;

    public ProductEventDocument() {
    }

    public ProductEventDocument(String productNumber, ProductChangeType changeType, String name, BigDecimal price, long version, Instant createdAt) {
        this.productNumber = productNumber;
        this.changeType = changeType;
        this.name = name;
        this.price = price;
        this.version = version;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public ProductChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ProductChangeType changeType) {
        this.changeType = changeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

