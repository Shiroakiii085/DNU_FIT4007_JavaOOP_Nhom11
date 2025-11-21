package models.product;

public class FeaturePhone extends Product {
    private String batteryCapacity;
    private boolean hasDualSim;
    
    public FeaturePhone(String id, String name, String brand, double price, int stockQuantity,
                       String description, String batteryCapacity, boolean hasDualSim) {
        super(id, name, brand, price, stockQuantity, description);
        this.batteryCapacity = batteryCapacity;
        this.hasDualSim = hasDualSim;
    }
    
    @Override
    public int getWarrantyPeriod() {
        return 12;
    }
    
    @Override
    public String getProductType() {
        return "DIEN_THOAI_CO_BAN";
    }
    
    public String toCSV() {
        return String.join(",", id, name, brand, String.valueOf(price),
                          String.valueOf(stockQuantity), description, getProductType(),
                          batteryCapacity, String.valueOf(hasDualSim));
    }
    
    public static FeaturePhone fromCSV(String csv) {
        String[] parts = csv.split(",");
        return new FeaturePhone(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]),
                               Integer.parseInt(parts[4]), parts[5], parts[7], 
                               Boolean.parseBoolean(parts[8]));
    }
}