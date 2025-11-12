package services.interfaces;

import models.transaction.Warranty;

public interface WarrantyService {
    Warranty createWarranty(String productId, String customerId, String invoiceId);
    boolean isWarrantyValid(String warrantyId);
    void processRepairRequest(String warrantyId, String issueDescription);
}