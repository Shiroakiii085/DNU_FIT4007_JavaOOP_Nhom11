package models.person;

public class Customer extends Person {
    private int loyaltyPoints;
    private String registrationDate;
    
    public Customer(String id, String name, String phone, String email, String address) {
        super(id, name, phone, email, address);
        this.loyaltyPoints = 0;
        this.registrationDate = java.time.LocalDate.now().toString();
    }
    
    @Override
    public String getPersonType() {
        return "CUSTOMER";
    }
    
    public int getLoyaltyPoints() { return loyaltyPoints; }
    public void addLoyaltyPoints(int points) { this.loyaltyPoints += points; }
    public String getRegistrationDate() { return registrationDate; }
    
    public String toCSV() {
        return String.join(",", id, name, phone, email, address, 
                          String.valueOf(loyaltyPoints), registrationDate);
    }
    
    public static Customer fromCSV(String csv) {
        String[] parts = csv.split(",");
        Customer c = new Customer(parts[0], parts[1], parts[2], parts[3], parts[4]);
        c.loyaltyPoints = Integer.parseInt(parts[5]);
        c.registrationDate = parts[6];
        return c;
    }
}
