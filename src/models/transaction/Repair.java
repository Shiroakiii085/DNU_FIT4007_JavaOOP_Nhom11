package models.transaction;

import models.enums.RepairStatus;

public class Repair {
    private String id;
    public String warrantyId;
    public String issueDescription;
    public String receiveDate;
    private String returnDate;
    private RepairStatus status;
    
    public Repair(String id, String warrantyId, String issueDescription) {
        this.id = id;
        this.warrantyId = warrantyId;
        this.issueDescription = issueDescription;
        this.receiveDate = java.time.LocalDate.now().toString();
        this.status = RepairStatus.PENDING;
    }
    
    public String getId() { return id; }
    public RepairStatus getStatus() { return status; }
    public void setStatus(RepairStatus status) { this.status = status; }
    public void setReturnDate(String date) { this.returnDate = date; }
    
    public String toCSV() {
        return String.join(",", id, warrantyId, issueDescription, receiveDate,
                          returnDate != null ? returnDate : "", status.toString());
    }
}
