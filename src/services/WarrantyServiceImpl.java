package services;

import models.transaction.Warranty;
import repository.DataStore;
import services.interfaces.WarrantyService;
import util.IDGenerator;

    
public class WarrantyServiceImpl implements WarrantyService {
	@Override
	public Warranty createWarranty(String productId, String customerId, String invoiceId) {
		Warranty w = new Warranty(IDGenerator.generateWarrantyId(), productId, customerId, invoiceId, 12);
		DataStore.getInstance().getWarranties().put(w.getId(), w);
		return w;
	}

	@Override
	public boolean isWarrantyValid(String warrantyId) {
		Warranty w = DataStore.getInstance().getWarranties().get(warrantyId);
		return w != null && w.isValid();
	}

	@Override
	public void processRepairRequest(String warrantyId, String issueDescription) {
        
		RepairService.createRepair(warrantyId, issueDescription);
	}
}
