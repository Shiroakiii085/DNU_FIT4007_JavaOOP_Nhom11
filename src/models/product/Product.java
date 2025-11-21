package models.product;

import exception.OutOfStockException;

public abstract class Product {
    protected String id;
    protected String name;
    protected String brand;
    protected double price;
    protected int stockQuantity;
    protected String description;
    
    public Product(String id, String name, String brand, double price, int stockQuantity, String description) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
    }
    
    public abstract int getWarrantyPeriod();
    public abstract String getProductType();
    
    public String getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public String getDescription() { return description; }
    
    public void setPrice(double price) { this.price = price; }
    public void setStockQuantity(int quantity) { this.stockQuantity = quantity; }
    public void reduceStock(int quantity) throws OutOfStockException {
        if (stockQuantity < quantity) {
            throw new OutOfStockException("Không đủ tồn kho cho sản phẩm: " + name);
        }
        this.stockQuantity -= quantity;
    }
    public void addStock(int quantity) { this.stockQuantity += quantity; }
}
