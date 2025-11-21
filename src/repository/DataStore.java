package repository;

import models.person.Customer;
import models.product.Product;
import models.transaction.Invoice;
import models.transaction.Repair;
import models.transaction.Warranty;

public class DataStore {
    
    private static DataStore instance;
    private java.util.Map<String, Product> products = new java.util.HashMap<>();
    private java.util.Map<String, Customer> customers = new java.util.HashMap<>();
    private java.util.Map<String, Invoice> invoices = new java.util.HashMap<>();
    private java.util.Map<String, Warranty> warranties = new java.util.HashMap<>();
    private java.util.Map<String, Repair> repairs = new java.util.HashMap<>();
    
    private DataStore() {}
    
    public static DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }
    
    public java.util.Map<String, Product> getProducts() { return products; }
    public java.util.Map<String, Customer> getCustomers() { return customers; }
    public java.util.Map<String, Invoice> getInvoices() { return invoices; }
    public java.util.Map<String, Warranty> getWarranties() { return warranties; }
    public java.util.Map<String, Repair> getRepairs() { return repairs; }

}
