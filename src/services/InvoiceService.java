package services;

import models.transaction.Invoice;
import repository.DataStore;
import util.IDGenerator;

public class InvoiceService {
	public static Invoice createInvoice(String customerId, String customerName) {
		Invoice invoice = new Invoice(IDGenerator.generateInvoiceId(), customerId, customerName);
		DataStore.getInstance().getInvoices().put(invoice.getId(), invoice);
		return invoice;
	}

	public static Invoice findById(String id) {
		return DataStore.getInstance().getInvoices().get(id);
	}
}
