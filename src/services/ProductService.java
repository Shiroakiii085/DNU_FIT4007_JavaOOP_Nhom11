package services;

import models.product.Product;
import repository.DataStore;

public class ProductService {
	public static void addProduct(Product p) {
		DataStore.getInstance().getProducts().put(p.getId(), p);
	}

	public static Product findById(String id) {
		return DataStore.getInstance().getProducts().get(id);
	}

	public static java.util.Collection<Product> getAll() {
		return DataStore.getInstance().getProducts().values();
	}
}
