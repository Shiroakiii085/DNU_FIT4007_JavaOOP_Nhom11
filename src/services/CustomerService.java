package services;

import models.person.Customer;
import repository.DataStore;

public class CustomerService {
	public static void addCustomer(Customer c) {
		DataStore.getInstance().getCustomers().put(c.getId(), c);
	}

	public static Customer findById(String id) {
		return DataStore.getInstance().getCustomers().get(id);
	}

	public static java.util.Collection<Customer> getAll() {
		return DataStore.getInstance().getCustomers().values();
	}
}
