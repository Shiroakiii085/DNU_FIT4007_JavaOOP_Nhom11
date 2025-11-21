package models.product;

public class SmartPhone extends Product {
    private String os;
    private int ram;
    private int storage;
    private String processor;
    
    public SmartPhone(String id, String name, String brand, double price, int stockQuantity, 
                     String description, String os, int ram, int storage, String processor) {
        super(id, name, brand, price, stockQuantity, description);
        this.os = os;
        this.ram = ram;
        this.storage = storage;
        this.processor = processor;
    }
    
    @Override
    public int getWarrantyPeriod() {
        return 24;
    }
    
    @Override
    public String getProductType() {
        return "DIEN_THOAI_THONG_MINH";
    }
    
    public String toCSV() {
        return String.join(",", id, name, brand, String.valueOf(price), 
                          String.valueOf(stockQuantity), description, getProductType(),
                          os, String.valueOf(ram), String.valueOf(storage), processor);
    }
    
    public static SmartPhone fromCSV(String csv) {
        String[] parts = csv.split(",");
        return new SmartPhone(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]),
                            Integer.parseInt(parts[4]), parts[5], parts[7], 
                            Integer.parseInt(parts[8]), Integer.parseInt(parts[9]), parts[10]);
    }
}
