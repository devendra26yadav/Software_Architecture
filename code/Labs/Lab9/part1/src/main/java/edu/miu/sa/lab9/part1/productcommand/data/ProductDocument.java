package edu.miu.sa.lab9.part1.productcommand.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document("part1_product_commands")
public class ProductDocument {

    @Id
    private String productNumber;
    private String name;
    private BigDecimal price;

    public ProductDocument() {
    }

    public ProductDocument(String productNumber, String name, BigDecimal price) {
        this.productNumber = productNumber;
        this.name = name;
        this.price = price;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
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
}

