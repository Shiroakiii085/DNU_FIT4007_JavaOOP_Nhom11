package repository;

import models.person.Customer;
import models.product.FeaturePhone;
import models.product.Product;
import models.product.SmartPhone;
import models.transaction.Invoice;
import models.transaction.Repair;
import models.transaction.Warranty;

public class FileManager {
    private static final String DATA_DIR = "data/";
    
    static {
        new java.io.File(DATA_DIR).mkdirs();
    }
    
    public static void saveProducts() {
        try (java.io.PrintWriter pw = new java.io.PrintWriter(DATA_DIR + "products.csv")) {
            for (Product p : DataStore.getInstance().getProducts().values()) {
                if (p instanceof SmartPhone) {
                    pw.println(((SmartPhone) p).toCSV());
                } else {
                    pw.println(((FeaturePhone) p).toCSV());
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu sản phẩm: " + e.getMessage());
        }
    }
    
    public static void loadProducts() {
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.FileReader(DATA_DIR + "products.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                Product p = parts[6].equals("DIEN_THOAI_THONG_MINH") ? 
                    SmartPhone.fromCSV(line) : FeaturePhone.fromCSV(line);
                DataStore.getInstance().getProducts().put(p.getId(), p);
            }
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Không tìm thấy file sản phẩm. Bắt đầu mới.");
        } catch (Exception e) {
            System.err.println("Lỗi khi tải sản phẩm: " + e.getMessage());
        }
    }
    
    public static void saveCustomers() {
        try (java.io.PrintWriter pw = new java.io.PrintWriter(DATA_DIR + "customers.csv")) {
            for (Customer c : DataStore.getInstance().getCustomers().values()) {
                pw.println(c.toCSV());
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu khách hàng: " + e.getMessage());
        }
    }
    
    public static void loadCustomers() {
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.FileReader(DATA_DIR + "customers.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Customer c = Customer.fromCSV(line);
                DataStore.getInstance().getCustomers().put(c.getId(), c);
            }
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Không tìm thấy file khách hàng. Bắt đầu mới.");
        } catch (Exception e) {
            System.err.println("Lỗi khi tải khách hàng: " + e.getMessage());
        }
    }
    
    public static void saveInvoices() {
        try (java.io.PrintWriter pw = new java.io.PrintWriter(DATA_DIR + "invoices.csv")) {
            for (Invoice inv : DataStore.getInstance().getInvoices().values()) {
                pw.println(inv.toCSV());
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu hóa đơn: " + e.getMessage());
        }
    }
    
    public static void saveWarranties() {
        try (java.io.PrintWriter pw = new java.io.PrintWriter(DATA_DIR + "warranties.csv")) {
            for (Warranty w : DataStore.getInstance().getWarranties().values()) {
                pw.println(w.toCSV());
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu bảo hành: " + e.getMessage());
        }
    }
    
    public static void loadWarranties() {
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.FileReader(DATA_DIR + "warranties.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                Warranty w = Warranty.fromCSV(line);
                DataStore.getInstance().getWarranties().put(w.getId(), w);
            }
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Không tìm thấy file bảo hành. Bắt đầu mới.");
        } catch (Exception e) {
            System.err.println("Lỗi khi tải bảo hành: " + e.getMessage());
        }
    }
    
    public static void saveRepairs() {
        try (java.io.PrintWriter pw = new java.io.PrintWriter(DATA_DIR + "repairs.csv")) {
            for (Repair r : DataStore.getInstance().getRepairs().values()) {
                pw.println(r.toCSV());
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lưu sửa chữa: " + e.getMessage());
        }
    }
    
    public static void saveAll() {
        saveProducts();
        saveCustomers();
        saveInvoices();
        saveWarranties();
        saveRepairs();
        System.out.println("Đã lưu tất cả dữ liệu thành công.");
    }
    
    public static void loadAll() {
        loadProducts();
        loadCustomers();
        loadWarranties();
        System.out.println("Đã tải dữ liệu thành công.");
    }
}

