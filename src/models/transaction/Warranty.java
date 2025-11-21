package models.transaction;

import models.enums.WarrantyStatus;

public class Warranty {
    private String id;
    private String productId;
    public String customerId;
    private String invoiceId;
    public String startDate;
    public String endDate;
    private WarrantyStatus status;
    
    public Warranty(String id, String productId, String customerId, String invoiceId, int warrantyMonths) {
        this.id = id;
        this.productId = productId;
        this.customerId = customerId;
        this.invoiceId = invoiceId;
        this.startDate = java.time.LocalDate.now().toString();
        this.endDate = java.time.LocalDate.now().plusMonths(warrantyMonths).toString();
        this.status = WarrantyStatus.ACTIVE;
    }
    
    public boolean isValid() {
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate end = java.time.LocalDate.parse(endDate);
        return !today.isAfter(end) && status == WarrantyStatus.ACTIVE;
    }
    
    public String getId() { return id; }
    public String getProductId() { return productId; }
    public WarrantyStatus getStatus() { return status; }
    public void setStatus(WarrantyStatus status) { this.status = status; }
    
    public String toCSV() {
        return String.join(",", id, productId, customerId, invoiceId, startDate, endDate, status.toString());
    }
    
    public static Warranty fromCSV(String csv) {
        String[] parts = csv.split(",");
        Warranty w = new Warranty(parts[0], parts[1], parts[2], parts[3], 0);
        w.startDate = parts[4];
        w.endDate = parts[5];
        w.status = WarrantyStatus.valueOf(parts[6]);
        return w;
    }
}
