package services;

import models.transaction.Repair;
import models.transaction.Warranty;
import repository.DataStore;
import util.IDGenerator;

public class RepairService {
	public static Repair createRepair(String warrantyId, String issue) {
		Warranty w = DataStore.getInstance().getWarranties().get(warrantyId);
		if (w == null) return null;
		Repair r = new Repair(IDGenerator.generateRepairId(), warrantyId, issue);
		DataStore.getInstance().getRepairs().put(r.getId(), r);
        
		w.setStatus(models.enums.WarrantyStatus.IN_PROCESS);
		return r;
	}

	public static java.util.Collection<Repair> getAll() {
		return DataStore.getInstance().getRepairs().values();
	}
}
