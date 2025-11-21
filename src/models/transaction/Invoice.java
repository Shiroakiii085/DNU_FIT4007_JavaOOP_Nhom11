package models.transaction;

import services.interfaces.DiscountPolicy;

public class Invoice implements DiscountPolicy {
    public String id;
    public String customerId;
    public String customerName;
    public java.util.List<InvoiceItem> items;
    public String date;
    public double totalAmount;
    public double discountAmount;
    public double finalAmount;
    
    public Invoice(String id, String customerId, String customerName) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.items = new java.util.ArrayList<>();
        this.date = java.time.LocalDate.now().toString();
        this.discountAmount = 0;
    }
    
    public void addItem(InvoiceItem item) {
        items.add(item);
        calculateTotal();
    }
    
    private void calculateTotal() {
        totalAmount = items.stream().mapToDouble(InvoiceItem::getSubtotal).sum();
        finalAmount = applyDiscount(totalAmount);
    }
    
    @Override
    public double applyDiscount(double originalPrice) {
        if (originalPrice > 20000000) {
            discountAmount = originalPrice * 0.05;
            return originalPrice - discountAmount;
        }
        return originalPrice;
    }
    
    @Override
    public String getDiscountDescription() {
        return discountAmount > 0 ? "Giảm 5% cho đơn hàng trên 20.000.000 VND" : "Không có giảm giá";
    }
    
    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getDate() { return date; }
    public double getFinalAmount() { return finalAmount; }
    public java.util.List<InvoiceItem> getItems() { return items; }

    public String getCustomerName() { return customerName; }
    public double getTotalAmount() { return totalAmount; }
    public double getDiscountAmount() { return discountAmount; }
    
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(",").append(customerId).append(",").append(customerName)
          .append(",").append(date).append(",").append(totalAmount).append(",")
          .append(discountAmount).append(",").append(finalAmount).append(",");
        sb.append(items.size());
        for (InvoiceItem item : items) {
            sb.append(",").append(item.toCSV());
        }
        return sb.toString();
    }
}