import exception.CustomerNotFoundException;
import exception.OutOfStockException;
import exception.WarrantyExpiredException;
import java.time.LocalDate;
import java.util.*;
import models.enums.WarrantyStatus;
import models.person.Customer;
import models.product.FeaturePhone;
import models.product.Product;
import models.product.SmartPhone;
import models.transaction.Invoice;
import models.transaction.InvoiceItem;
import models.transaction.Repair;
import models.transaction.Warranty;
import repository.DataStore;
import repository.FileManager;
import util.IDGenerator;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static DataStore store = DataStore.getInstance();
    
    public static void main(String[] args) {

        FileManager.loadAll();
        
        initializeSampleData();

        while (true) {
            displayMainMenu();
            int choice = getIntInput("Chọn tùy chọn: ");
            
            try {
                switch (choice) {
                    case 1: productManagement(); break;
                    case 2: customerManagement(); break;
                    case 3: sellProduct(); break;
                    case 4: warrantyManagement(); break;
                    case 5: reportMenu(); break;
                    case 6: 
                        FileManager.saveAll();
                        System.out.println("Tạm biệt!");
                        return;
                    default: System.out.println("Lựa chọn không hợp lệ!");
                }
                FileManager.saveAll();
            } catch (Exception e) {
                System.err.println("Lỗi: " + e.getMessage());
            }
        }
    }
    
    private static void displayMainMenu() {
        System.out.println("\n=== HỆ THỐNG QUẢN LÝ CỬA HÀNG ĐIỆN THOẠI ===");
        System.out.println("1. Quản lý sản phẩm");
        System.out.println("2. Quản lý khách hàng");
        System.out.println("3. Bán sản phẩm");
        System.out.println("4. Quản lý bảo hành");
        System.out.println("5. Báo cáo & Thống kê");
        System.out.println("6. Lưu & Thoát");
    }
    
    private static void productManagement() {
        System.out.println("\n--- Quản lý sản phẩm ---");
        System.out.println("1. Thêm sản phẩm");
        System.out.println("2. Xem tất cả sản phẩm");
        System.out.println("3. Tìm sản phẩm");
        System.out.println("4. Cập nhật tồn kho");
        int choice = getIntInput("Chọn: ");
        
        switch (choice) {
            case 1: addProduct(); break;
            case 2: viewAllProducts(); break;
            case 3: searchProduct(); break;
            case 4: updateStock(); break;
        }
    }
    
    private static void addProduct() {
    System.out.print("Loại sản phẩm (1=Điện thoại thông minh, 2=Điện thoại cơ bản): ");
        int type = scanner.nextInt();
        scanner.nextLine();
        
    System.out.print("Tên: ");
    String name = scanner.nextLine();
    System.out.print("Thương hiệu: ");
    String brand = scanner.nextLine();
    System.out.print("Giá: ");
    double price = scanner.nextDouble();
    System.out.print("Số lượng tồn: ");
    int stock = scanner.nextInt();
    scanner.nextLine();
    System.out.print("Mô tả: ");
    String desc = scanner.nextLine();
        
        Product product;
        if (type == 1) {
            System.out.print("Hệ điều hành: ");
            String os = scanner.nextLine();
            System.out.print("RAM (GB): ");
            int ram = scanner.nextInt();
            System.out.print("Bộ nhớ (GB): ");
            int storage = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Bộ xử lý: ");
            String cpu = scanner.nextLine();
            
            product = new SmartPhone(IDGenerator.generateProductId(), name, brand, 
                                    price, stock, desc, os, ram, storage, cpu);
        } else {
            System.out.print("Dung lượng pin: ");
            String battery = scanner.nextLine();
            System.out.print("Hỗ trợ Dual SIM (true/false): ");
            boolean dualSim = scanner.nextBoolean();
            
            product = new FeaturePhone(IDGenerator.generateProductId(), name, brand,
                                      price, stock, desc, battery, dualSim);
        }
        
        store.getProducts().put(product.getId(), product);
        System.out.println("Thêm sản phẩm thành công! ID: " + product.getId());
    }
    
    private static void viewAllProducts() {
    System.out.println("\n--- Tất cả sản phẩm ---");
    System.out.printf("%-10s %-30s %-15s %-12s %-10s %-15s%n", 
             "ID", "Tên", "Thương hiệu", "Giá", "Tồn", "Loại");
    System.out.println("-".repeat(95));
        
        for (Product p : store.getProducts().values()) {
            System.out.printf("%-10s %-30s %-15s %,12.0f %-10d %-15s%n",
                            p.getId(), p.getName(), p.getBrand(), p.getPrice(),
                            p.getStockQuantity(), p.getProductType());
        }
    }
    
    private static void searchProduct() {
    System.out.print("Tìm theo (1=ID, 2=Tên, 3=Thương hiệu): ");
        int type = scanner.nextInt();
        scanner.nextLine();
    System.out.print("Nhập từ khóa tìm kiếm: ");
    String term = scanner.nextLine().toLowerCase();
        
        for (Product p : store.getProducts().values()) {
            boolean match = false;
            switch (type) {
                case 1: match = p.getId().toLowerCase().contains(term); break;
                case 2: match = p.getName().toLowerCase().contains(term); break;
                case 3: match = p.getBrand().toLowerCase().contains(term); break;
            }
            
            if (match) {
                System.out.printf("%s - %s (%s) - %,.0f VND - Tồn: %d%n",
                                p.getId(), p.getName(), p.getBrand(), 
                                p.getPrice(), p.getStockQuantity());
            }
        }
    }
    
    private static void updateStock() {
        System.out.print("ID: ");
        String id = scanner.nextLine();
        Product p = store.getProducts().get(id);
        
        if (p == null) {
            System.out.println("Không tìm thấy mặt hàng!");
            return;
        }
        
        System.out.printf("Current stock: %d%n", p.getStockQuantity());
        System.out.print("Add quantity: ");
        int qty = scanner.nextInt();
        p.addStock(qty);
        System.out.println("Stock updated successfully!");
    }
    
    private static void customerManagement() {
        System.out.println("\n--- Quản lý khách hàng ---");
        System.out.println("1. Thêm khách hàng");
        System.out.println("2. Xem tất cả khách hàng");
        System.out.println("3. Tìm khách hàng");
        int choice = getIntInput("Chọn: ");
        
        switch (choice) {
            case 1: addCustomer(); break;
            case 2: viewAllCustomers(); break;
            case 3: searchCustomer(); break;
        }
    }
    
    private static void addCustomer() {
        System.out.print("Tên: ");
        String name = scanner.nextLine();
        System.out.print("Sđt: ");
        String phone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Địa chỉ: ");
        String address = scanner.nextLine();
        
        Customer customer = new Customer(IDGenerator.generateCustomerId(), 
                                        name, phone, email, address);
        store.getCustomers().put(customer.getId(), customer);
        System.out.println("Đã thêm khách hàng! ID: " + customer.getId());
    }
    
    private static void viewAllCustomers() {
        System.out.println("\n--- Tất cả khách hàng ---");
        System.out.printf("%-10s %-25s %-15s %-30s%n", "ID", "Tên", "Sđt", "Email");
        System.out.println("-".repeat(85));
        
        for (Customer c : store.getCustomers().values()) {
            System.out.printf("%-10s %-25s %-15s %-30s%n",
                            c.getId(), c.getName(), c.getPhone(), c.getEmail());
        }
    }
    
    private static void searchCustomer() {
        System.out.print("Tìm kiếm (1=Tên, 2=Sđt, 3=Email): ");
        int type = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nhập dữ liệu muốn tìm kiếm: ");
        String term = scanner.nextLine().toLowerCase();
        
        for (Customer c : store.getCustomers().values()) {
            boolean match = false;
            switch (type) {
                case 1: match = c.getName().toLowerCase().contains(term); break;
                case 2: match = c.getPhone().contains(term); break;
                case 3: match = c.getEmail().toLowerCase().contains(term); break;
            }
            
            if (match) {
                System.out.printf("%s - %s - %s - %s%n",
                                c.getId(), c.getName(), c.getPhone(), c.getEmail());
            }
        }
    }
    
    private static void sellProduct() throws Exception {
        System.out.print("ID khách hàng: ");
        String customerId = scanner.nextLine();
        Customer customer = store.getCustomers().get(customerId);
        
        if (customer == null) {
            throw new CustomerNotFoundException("Không tìm thấy khách hàng: " + customerId);
        }
        
        Invoice invoice = new Invoice(IDGenerator.generateInvoiceId(), 
                                      customerId, customer.getName());
        
        while (true) {
            System.out.print("ID Sản phẩm (hoặc điền 'done' để kết thúc): ");
            String productId = scanner.nextLine();
            
            if (productId.equalsIgnoreCase("done")) break;
            
            Product product = store.getProducts().get(productId);
            if (product == null) {
                System.out.println("Không tìm thấy sản phẩm!");
                continue;
            }
            
            System.out.printf("Sản phẩm: %s - Giá: %,.0f - Số lượng tồn: %d%n",
                            product.getName(), product.getPrice(), product.getStockQuantity());
            System.out.print("Số lượng: ");
            int qty = scanner.nextInt();
            scanner.nextLine();
            
            try {
                product.reduceStock(qty);
                InvoiceItem item = new InvoiceItem(product.getId(), product.getName(),
                                                  qty, product.getPrice());
                invoice.addItem(item);
                
                // Create warranty for each product
                for (int i = 0; i < qty; i++) {
                    Warranty warranty = new Warranty(IDGenerator.generateWarrantyId(),
                                                    product.getId(), customerId,
                                                    invoice.getId(), product.getWarrantyPeriod());
                    store.getWarranties().put(warranty.getId(), warranty);
                }
                
                System.out.println("Đã thêm vào hóa đơn!");
            } catch (OutOfStockException e) {
                System.out.println("Lỗi: " + e.getMessage());
            }
        }
        
        if (invoice.getItems().isEmpty()) {
            System.out.println("Không có mặt hàng trong hóa đơn!");
            return;
        }
        
        store.getInvoices().put(invoice.getId(), invoice);
        customer.addLoyaltyPoints((int)(invoice.getFinalAmount() / 100000));

        printInvoice(invoice);
        System.out.println("\nBán hàng hoàn tất!");
    }
    
    private static void printInvoice(Invoice invoice) {
    System.out.println("\n" + "=".repeat(80));
    System.out.println("                      HÓA ĐƠN CỬA HÀNG ĐIỆN THOẠI");
    System.out.println("=".repeat(80));
    System.out.printf("Mã hóa đơn: %s                    Ngày: %s%n", 
             invoice.getId(), invoice.getDate());
    System.out.printf("Khách hàng: %s (Mã: %s)%n", 
             invoice.customerName, invoice.getCustomerId());
    System.out.println("-".repeat(80));
    System.out.printf("%-10s %-35s %-8s %-12s %-12s%n",
             "ID", "Sản phẩm", "SL", "Đơn giá", "Thành tiền");
    System.out.println("-".repeat(80));
        
        for (InvoiceItem item : invoice.getItems()) {
            System.out.printf("%-10s %-35s %-8d %,12.0f %,12.0f%n",
                            item.getProductId(), item.productName, item.getQuantity(),
                            item.unitPrice, item.getSubtotal());
        }
        
        System.out.println("-".repeat(80));
        System.out.printf("%67s %,12.0f%n", "Tổng:", invoice.totalAmount);
        if (invoice.discountAmount > 0) {
            System.out.printf("%67s -%,11.0f%n", "Giảm giá:", invoice.discountAmount);
            System.out.printf("%67s %,12.0f%n", "Thanh toán:", invoice.finalAmount);
        }
        System.out.println("=".repeat(80));
        System.out.println(invoice.getDiscountDescription());
        System.out.println("Cảm ơn bạn đã mua hàng!");
    }
    
    private static void warrantyManagement() {
    System.out.println("\n--- Quản lý bảo hành ---");
    System.out.println("1. Xem tất cả bảo hành");
    System.out.println("2. Kiểm tra tình trạng bảo hành");
    System.out.println("3. Tạo yêu cầu sửa chữa");
    System.out.println("4. Xem sửa chữa");
    int choice = getIntInput("Chọn: ");
        
        switch (choice) {
            case 1: viewAllWarranties(); break;
            case 2: checkWarrantyStatus(); break;
            case 3: createRepairRequest(); break;
            case 4: viewRepairs(); break;
        }
    }
    
    private static void viewAllWarranties() {
    System.out.println("\n--- Tất cả bảo hành ---");
    System.out.printf("%-12s %-12s %-12s %-12s %-12s %-10s%n",
             "Mã", "Sản phẩm", "Khách hàng", "Bắt đầu", "Kết thúc", "Trạng thái");
        System.out.println("-".repeat(80));
        
        for (Warranty w : store.getWarranties().values()) {
            System.out.printf("%-12s %-12s %-12s %-12s %-12s %-10s%n",
                            w.getId(), w.getProductId(), w.customerId,
                            w.startDate, w.endDate, w.getStatus());
        }
    }
    
    private static void checkWarrantyStatus() {
    System.out.print("Mã bảo hành: ");
        String id = scanner.nextLine();
        Warranty warranty = store.getWarranties().get(id);
        
        if (warranty == null) {
            System.out.println("Không tìm thấy bảo hành!");
            return;
        }
        
        try {
            if (warranty.isValid()) {
                System.out.println("✓ Bảo hành hợp lệ");
                System.out.printf("Sản phẩm: %s%n", warranty.getProductId());
                System.out.printf("Hợp lệ đến: %s%n", warranty.endDate);
            } else {
                throw new WarrantyExpiredException("Bảo hành đã hết hạn vào " + warranty.endDate);
            }
        } catch (WarrantyExpiredException e) {
            System.out.println("✗ " + e.getMessage());
        }
    }
    
    private static void createRepairRequest() {
    System.out.print("Mã bảo hành: ");
        String warrantyId = scanner.nextLine();
        Warranty warranty = store.getWarranties().get(warrantyId);
        
        if (warranty == null) {
            System.out.println("Không tìm thấy bảo hành!");
            return;
        }
        
        try {
            if (!warranty.isValid()) {
                throw new WarrantyExpiredException("Không thể tạo sửa chữa - bảo hành đã hết hạn");
            }
            
            System.out.print("Mô tả lỗi: ");
            String issue = scanner.nextLine();
            
            Repair repair = new Repair(IDGenerator.generateRepairId(), warrantyId, issue);
            store.getRepairs().put(repair.getId(), repair);
            warranty.setStatus(WarrantyStatus.IN_PROCESS);

            System.out.println("Yêu cầu sửa chữa đã tạo! ID: " + repair.getId());
        } catch (WarrantyExpiredException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }
    
    private static void viewRepairs() {
    System.out.println("\n--- Tất cả sửa chữa ---");
    System.out.printf("%-12s %-12s %-30s %-12s %-12s%n",
             "Mã", "Bảo hành", "Mô tả", "Ngày nhận", "Trạng thái");
        System.out.println("-".repeat(85));
        
        for (Repair r : store.getRepairs().values()) {
            System.out.printf("%-12s %-12s %-30s %-12s %-12s%n",
                            r.getId(), r.warrantyId, r.issueDescription,
                            r.receiveDate, r.getStatus());
        }
    }
    
    private static void reportMenu() {
    System.out.println("\n--- Báo cáo & Thống kê ---");
    System.out.println("1. Doanh thu theo tháng");
    System.out.println("2. Top 5 sản phẩm bán chạy");
    System.out.println("3. Bảo hành đang hoạt động");
    System.out.println("4. Sản phẩm bán trong tháng");
    int choice = getIntInput("Chọn: ");
        
        switch (choice) {
            case 1: revenueByMonth(); break;
            case 2: topSellingProducts(); break;
            case 3: activeWarranties(); break;
            case 4: productsSoldThisMonth(); break;
        }
    }
    
    private static void revenueByMonth() {
    System.out.print("Nhập năm (YYYY): ");
    int year = scanner.nextInt();
    System.out.print("Nhập tháng (MM): ");
    int month = scanner.nextInt();
        scanner.nextLine();
        
        double totalRevenue = 0;
        int invoiceCount = 0;
        
    System.out.println("\n--- Báo cáo doanh thu ---");
    System.out.printf("Kỳ: %d-%02d%n", year, month);
        System.out.println("-".repeat(50));
        
        for (Invoice inv : store.getInvoices().values()) {
            LocalDate invDate = LocalDate.parse(inv.getDate());
            if (invDate.getYear() == year && invDate.getMonthValue() == month) {
                totalRevenue += inv.getFinalAmount();
                invoiceCount++;
                System.out.printf("%s - %s - %,15.0f VND%n",
                                inv.getId(), inv.getDate(), inv.getFinalAmount());
            }
        }
        
        System.out.println("-".repeat(50));
        System.out.printf("Tổng số hóa đơn: %d%n", invoiceCount);
        System.out.printf("Tổng doanh thu: %,.0f VND%n", totalRevenue);
    }
    
    private static void topSellingProducts() {
        Map<String, Integer> productSales = new HashMap<>();
        Map<String, String> productNames = new HashMap<>();
        
        for (Invoice inv : store.getInvoices().values()) {
            for (InvoiceItem item : inv.getItems()) {
                productSales.merge(item.getProductId(), item.getQuantity(), Integer::sum);
                productNames.put(item.getProductId(), item.productName);
            }
        }
        
    System.out.println("\n--- Top 5 sản phẩm bán chạy ---");
    System.out.printf("%-12s %-40s %-10s%n", "Mã sản phẩm", "Tên", "Đã bán");
        System.out.println("-".repeat(70));
        
        productSales.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(5)
            .forEach(entry -> {
                System.out.printf("%-12s %-40s %-10d%n",
                                entry.getKey(), productNames.get(entry.getKey()), entry.getValue());
            });
    }
    
    private static void activeWarranties() {
    System.out.println("\n--- Bảo hành đang hoạt động ---");
    System.out.printf("%-12s %-12s %-12s %-12s%n",
             "Mã bảo hành", "Sản phẩm", "Khách hàng", "Hợp lệ đến");
    System.out.println("-".repeat(60));
        
        int count = 0;
        for (Warranty w : store.getWarranties().values()) {
            if (w.isValid()) {
                System.out.printf("%-12s %-12s %-12s %-12s%n",
                                w.getId(), w.getProductId(), w.customerId, w.endDate);
                count++;
            }
        }
        System.out.printf("%nTổng số bảo hành đang hoạt động: %d%n", count);
    }
    
    private static void productsSoldThisMonth() {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();
        
    System.out.println("\n--- Sản phẩm bán trong tháng ---");
    System.out.printf("%-12s %-35s %-8s %-12s%n", "Hóa đơn", "Sản phẩm", "SL", "Số tiền");
    System.out.println("-".repeat(75));
        
        for (Invoice inv : store.getInvoices().values()) {
            LocalDate invDate = LocalDate.parse(inv.getDate());
            if (invDate.getYear() == currentYear && invDate.getMonthValue() == currentMonth) {
                for (InvoiceItem item : inv.getItems()) {
                    System.out.printf("%-12s %-35s %-8d %,12.0f%n",
                                    inv.getId(), item.productName, 
                                    item.getQuantity(), item.getSubtotal());
                }
            }
        }
    }
    
    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Dữ liệu không hợp lệ. " + prompt);
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
    
    private static void initializeSampleData() {
        boolean productsExist = !store.getProducts().isEmpty();
        boolean customersExist = !store.getCustomers().isEmpty();

        if (productsExist && customersExist) return;

        System.out.println("Khởi tạo dữ liệu mẫu...");

        if (!productsExist) {
            
            SmartPhone[] smartphones = {

                new SmartPhone(IDGenerator.generateProductId(), "Galaxy S23 Ultra", "Samsung", 29990000, 15,
                    "Flagship Samsung với bút S-Pen", "Android 13", 12, 256, "Snapdragon 8 Gen 2"),
                new SmartPhone(IDGenerator.generateProductId(), "Galaxy S23+", "Samsung", 24990000, 20,
                    "Màn hình Dynamic AMOLED 2X", "Android 13", 8, 256, "Snapdragon 8 Gen 2"),
                new SmartPhone(IDGenerator.generateProductId(), "Galaxy A54", "Samsung", 10990000, 30,
                    "Camera chống rung OIS", "Android 13", 8, 128, "Exynos 1380"),
                new SmartPhone(IDGenerator.generateProductId(), "Galaxy A34", "Samsung", 8490000, 25,
                    "Pin 5000mAh", "Android 13", 8, 128, "Dimensity 1080"),

                new SmartPhone(IDGenerator.generateProductId(), "iPhone 15 Pro Max", "Apple", 34990000, 10,
                    "Chip A17 Pro mạnh mẽ", "iOS 17", 8, 256, "A17 Pro"),
                new SmartPhone(IDGenerator.generateProductId(), "iPhone 15 Pro", "Apple", 29990000, 12,
                    "Khung titan cao cấp", "iOS 17", 8, 128, "A17 Pro"),
                new SmartPhone(IDGenerator.generateProductId(), "iPhone 15", "Apple", 24990000, 15,
                    "Dynamic Island", "iOS 17", 6, 128, "A16 Bionic"),
                new SmartPhone(IDGenerator.generateProductId(), "iPhone 14", "Apple", 19990000, 18,
                    "Camera kép 12MP", "iOS 17", 6, 128, "A15 Bionic"),
                    
                new SmartPhone(IDGenerator.generateProductId(), "Redmi Note 12 Pro", "Xiaomi", 7990000, 40,
                    "Camera 108MP", "Android 13", 8, 256, "Dimensity 1080"),
                new SmartPhone(IDGenerator.generateProductId(), "Redmi Note 12", "Xiaomi", 4990000, 50,
                    "Màn hình AMOLED 120Hz", "Android 13", 8, 128, "Snapdragon 685"),
                new SmartPhone(IDGenerator.generateProductId(), "POCO X5 Pro", "Xiaomi", 7490000, 35,
                    "5G, Sạc nhanh 67W", "Android 13", 8, 256, "Snapdragon 778G"),
                new SmartPhone(IDGenerator.generateProductId(), "POCO X5", "Xiaomi", 5990000, 45,
                    "Pin 5000mAh", "Android 13", 6, 128, "Snapdragon 695"),

                new SmartPhone(IDGenerator.generateProductId(), "Find N3", "OPPO", 44990000, 8,
                    "Màn hình gập cao cấp", "Android 13", 16, 512, "Snapdragon 8 Gen 2"),
                new SmartPhone(IDGenerator.generateProductId(), "Reno10 Pro+", "OPPO", 19990000, 15,
                    "Camera tele 64MP", "Android 13", 12, 256, "Snapdragon 8+ Gen 1"),
                new SmartPhone(IDGenerator.generateProductId(), "Reno10", "OPPO", 10990000, 25,
                    "Camera chân dung 32MP", "Android 13", 8, 256, "Dimensity 7050"),
                new SmartPhone(IDGenerator.generateProductId(), "A78", "OPPO", 6990000, 35,
                    "Sạc nhanh SUPERVOOC", "Android 13", 8, 128, "Snapdragon 680"),

                new SmartPhone(IDGenerator.generateProductId(), "X90 Pro", "Vivo", 19990000, 12,
                    "Camera ZEISS 50MP", "Android 13", 12, 256, "Dimensity 9200"),
                new SmartPhone(IDGenerator.generateProductId(), "V27e", "Vivo", 8990000, 28,
                    "Màn hình AMOLED 120Hz", "Android 13", 8, 256, "Helio G99"),
                new SmartPhone(IDGenerator.generateProductId(), "Y35", "Vivo", 6490000, 40,
                    "RAM mở rộng 8GB", "Android 13", 8, 128, "Snapdragon 680"),
                new SmartPhone(IDGenerator.generateProductId(), "Y22s", "Vivo", 5290000, 45,
                    "Pin 5000mAh", "Android 12", 8, 128, "Snapdragon 680")
            };

            for (SmartPhone phone : smartphones) {
                store.getProducts().put(phone.getId(), phone);
            }

            FeaturePhone[] featurePhones = {
                new FeaturePhone(IDGenerator.generateProductId(), "Nokia 110 4G", "Nokia", 790000, 50,
                    "Điện thoại phổ thông 4G", "1020mAh", true),
                new FeaturePhone(IDGenerator.generateProductId(), "Nokia 8210 4G", "Nokia", 1490000, 40,
                    "Thiết kế hoài cổ", "1450mAh", true),
                new FeaturePhone(IDGenerator.generateProductId(), "Nokia 5710", "Nokia", 1890000, 30,
                    "Tích hợp tai nghe không dây", "1450mAh", true),
                new FeaturePhone(IDGenerator.generateProductId(), "Nokia 2660 Flip", "Nokia", 1990000, 25,
                    "Điện thoại nắp gập", "1450mAh", false),
                new FeaturePhone(IDGenerator.generateProductId(), "Nokia 6310", "Nokia", 1590000, 35,
                    "Pin siêu bền", "1150mAh", true)
            };

            for (FeaturePhone phone : featurePhones) {
                store.getProducts().put(phone.getId(), phone);
            }
        }

        if (!customersExist) {
            Customer[] customers = {
                new Customer(IDGenerator.generateCustomerId(), "Nguyễn Văn An", "0912345001", "nguyenan@email.com", "123 Lê Lợi, Q1"),
                new Customer(IDGenerator.generateCustomerId(), "Trần Thị Bình", "0912345002", "tranbinh@email.com", "45 Nguyễn Huệ, Q1"),
                new Customer(IDGenerator.generateCustomerId(), "Lê Hoàng Cường", "0912345003", "lecuong@email.com", "67 Đồng Khởi, Q1"),
                new Customer(IDGenerator.generateCustomerId(), "Phạm Minh Đức", "0912345004", "phamduc@email.com", "89 Pasteur, Q1"),
                new Customer(IDGenerator.generateCustomerId(), "Hoàng Thị Em", "0912345005", "hoangem@email.com", "12 Nam Kỳ Khởi Nghĩa, Q3"),
                new Customer(IDGenerator.generateCustomerId(), "Võ Thành Giáp", "0912345006", "vogiap@email.com", "34 Cao Thắng, Q3"),
                new Customer(IDGenerator.generateCustomerId(), "Đặng Thu Hà", "0912345007", "dangha@email.com", "56 Võ Văn Tần, Q3"),
                new Customer(IDGenerator.generateCustomerId(), "Bùi Quốc Hưng", "0912345008", "buihung@email.com", "78 Nguyễn Đình Chiểu, Q3"),
                new Customer(IDGenerator.generateCustomerId(), "Lý Thị Lan", "0912345009", "lylan@email.com", "90 Điện Biên Phủ, Q3"),
                new Customer(IDGenerator.generateCustomerId(), "Mai Văn Khánh", "0912345010", "maikhach@email.com", "111 Lê Văn Sỹ, Q3"),
                new Customer(IDGenerator.generateCustomerId(), "Trương Minh Lộc", "0912345011", "truongloc@email.com", "222 Nguyễn Thị Minh Khai, Q1"),
                new Customer(IDGenerator.generateCustomerId(), "Hồ Thị Mai", "0912345012", "homai@email.com", "333 Cách Mạng Tháng 8, Q3"),
                new Customer(IDGenerator.generateCustomerId(), "Đỗ Thành Nam", "0912345013", "donam@email.com", "444 3 Tháng 2, Q10"),
                new Customer(IDGenerator.generateCustomerId(), "Lê Thị Oanh", "0912345014", "leoanh@email.com", "555 Lý Thái Tổ, Q10"),
                new Customer(IDGenerator.generateCustomerId(), "Nguyễn Đức Phát", "0912345015", "nguyenphat@email.com", "666 Sư Vạn Hạnh, Q10"),
                new Customer(IDGenerator.generateCustomerId(), "Trần Thị Quỳnh", "0912345016", "tranquynh@email.com", "777 Thành Thái, Q10"),
                new Customer(IDGenerator.generateCustomerId(), "Phạm Văn Rồng", "0912345017", "phamrong@email.com", "888 Tô Hiến Thành, Q10"),
                new Customer(IDGenerator.generateCustomerId(), "Huỳnh Thị Sen", "0912345018", "huynhsen@email.com", "999 Bà Hạt, Q10"),
                new Customer(IDGenerator.generateCustomerId(), "Võ Thành Tài", "0912345019", "votai@email.com", "123 Nguyễn Chí Thanh, Q5"),
                new Customer(IDGenerator.generateCustomerId(), "Đặng Văn Út", "0912345020", "dangut@email.com", "234 An Dương Vương, Q5"),
                new Customer(IDGenerator.generateCustomerId(), "Bùi Thị Vân", "0912345021", "buivan@email.com", "345 Hậu Giang, Q6"),
                new Customer(IDGenerator.generateCustomerId(), "Mai Thành Xuân", "0912345022", "maixuan@email.com", "456 Hồng Bàng, Q6"),
                new Customer(IDGenerator.generateCustomerId(), "Trương Thị Yến", "0912345023", "truongyen@email.com", "567 Minh Phụng, Q6"),
                new Customer(IDGenerator.generateCustomerId(), "Hoàng Đức Anh", "0912345024", "hoanganh@email.com", "678 Tản Đà, Q5"),
                new Customer(IDGenerator.generateCustomerId(), "Lý Văn Bảo", "0912345025", "lybao@email.com", "789 Nguyễn Trãi, Q5"),
                new Customer(IDGenerator.generateCustomerId(), "Đỗ Thị Cúc", "0912345026", "docuc@email.com", "890 Trần Phú, Q5"),
                new Customer(IDGenerator.generateCustomerId(), "Nguyễn Thành Danh", "0912345027", "nguyendanh@email.com", "901 Lý Thường Kiệt, Q10"),
                new Customer(IDGenerator.generateCustomerId(), "Trần Văn Phúc", "0912345028", "tranphuc@email.com", "1234 Cao Thắng, Q3"),
                new Customer(IDGenerator.generateCustomerId(), "Phạm Thị Giang", "0912345029", "phamgiang@email.com", "2345 Nguyễn Thiện Thuật, Q3"),
                new Customer(IDGenerator.generateCustomerId(), "Huỳnh Văn Hiếu", "0912345030", "huynhhieu@email.com", "3456 Điện Biên Phủ, Q10")
            };

            for (Customer customer : customers) {
                store.getCustomers().put(customer.getId(), customer);
            }
        }

        System.out.println("Đã khởi tạo: " + store.getProducts().size() + " sản phẩm, " + store.getCustomers().size() + " khách hàng");
    }
}