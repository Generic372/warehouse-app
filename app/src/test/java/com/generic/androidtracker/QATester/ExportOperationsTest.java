package com.generic.androidtracker.QATester;

import com.generic.models.FreightType;
import com.generic.models.Shipment;
import com.generic.models.Warehouse;
import com.generic.models.WarehouseFactory;
import com.generic.models.WeightUnit;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class ExportOperationsTest {

	@Test
	public void exportWarehouseToJson() {
		WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();
		Warehouse mWarehouse = new Warehouse("Ware120", "1231");
		warehouseFactory.addWarehouse(mWarehouse);

		Shipment mShipment = new Shipment
				.Builder()
				.id("1212ad")
				.type(FreightType.TRUCK)
				.date(1121313100L)
				.weight(323D)
				.weightUnit(WeightUnit.KG)
				.build();
		mWarehouse.addShipment(mShipment);

		// creating new files for both a single warehouse JSON object(warehouse_<id> contents)
		// and a whole list of them (warehouse_contents)
		File singleWarehouseFile = new File("output/" + String.format("/warehouse_%s contents.json", mWarehouse.getWarehouseID()));
		File allWarehousesFile = new File("output/" + "/warehouse_contents.json");

		// Check and create directory
		if (!singleWarehouseFile.getParentFile().exists())
			singleWarehouseFile.getParentFile().mkdirs();

		// Calling method that save the objects to JSON files
		mWarehouse.saveToDir(singleWarehouseFile.getAbsolutePath());
		warehouseFactory.saveToDir(allWarehousesFile.getAbsolutePath());

		// we assert here that the file we exported exists
		assertTrue(singleWarehouseFile.exists());
		assertTrue(allWarehousesFile.exists());

		// delete the files as we have no use
		// for them anymore
		singleWarehouseFile.delete();
		allWarehousesFile.delete();

		warehouseFactory.deleteAllWarehouses();

	}
}
