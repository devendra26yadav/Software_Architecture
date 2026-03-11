package mydomain.com.productservice.dto;

public class ProductResponse {
    private int productNumber;
    private String name;
    private int stock;

    public ProductResponse(int productNumber, String name, int stock) {
        this.productNumber = productNumber;
        this.name = name;
        this.stock = stock;
    }

    public int getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(int productNumber) {
        this.productNumber = productNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
