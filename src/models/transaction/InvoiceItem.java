package models.transaction;

public class InvoiceItem {
    private String productId;
    public String productName;
    private int quantity;
    public double unitPrice;
    public double subtotal;
    
    public InvoiceItem(String productId, String productName, int quantity, double unitPrice) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = quantity * unitPrice;
    }
    
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public double getSubtotal() { return subtotal; }
    
    public String toCSV() {
        return String.join(",", productId, productName, String.valueOf(quantity),
                          String.valueOf(unitPrice), String.valueOf(subtotal));
    }
}
