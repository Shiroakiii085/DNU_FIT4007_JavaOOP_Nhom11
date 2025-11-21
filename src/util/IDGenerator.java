package util;

public class IDGenerator {
    private static int productCounter = 1000;
    private static int customerCounter = 1000;
    private static int invoiceCounter = 1000;
    private static int warrantyCounter = 1000;
    private static int repairCounter = 1000;
    
    public static String generateProductId() {
        return "PRD" + (++productCounter);
    }
    
    public static String generateCustomerId() {
        return "CUS" + (++customerCounter);
    }
    
    public static String generateInvoiceId() {
        return "INV" + (++invoiceCounter);
    }
    
    public static String generateWarrantyId() {
        return "WAR" + (++warrantyCounter);
    }
    
    public static String generateRepairId() {
        return "REP" + (++repairCounter);
    }
}
