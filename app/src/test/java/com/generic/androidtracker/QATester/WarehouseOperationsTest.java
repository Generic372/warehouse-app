package com.generic.androidtracker.QATester;

import com.generic.models.Warehouse;
import com.generic.models.WarehouseFactory;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * A class to run unit tests against warehouse operations.
 * @author GENERIC TEAM
 *
 */
public class WarehouseOperationsTest {

	@Test
	public void addWarehouse() {
		WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();

		boolean result = warehouseFactory.addWarehouse("W121", "12100");

		assertTrue(result);
		warehouseFactory.deleteAllWarehouses();
	}

	@Test
	public void addDuplicateWarehouse() {
		WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();
		Warehouse warehouse = new Warehouse("W121", "12100");

		boolean result = warehouseFactory.addWarehouse("W121", "12100");
		boolean result2 = warehouseFactory.addWarehouse(warehouse);

		assertTrue(result);
		assertFalse(result2);
		warehouseFactory.deleteAllWarehouses();
	}

	@Test
	public void toggleFreightReceipt() {

		WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();
		Warehouse warehouse = new Warehouse("W121", "12100");
		warehouseFactory.addWarehouse(warehouse);

		boolean beTrue1 = warehouseFactory.endFreight(warehouse.getId());
		boolean beFalse1 = warehouseFactory.freightIsEnabled(warehouse.getId());
		boolean beTrue2 = warehouseFactory.enableFreight(warehouse.getId());
		boolean beTrue3 = warehouseFactory.freightIsEnabled(warehouse.getId());

		assertTrue(beTrue1);
		assertFalse(beFalse1);
		assertTrue(beTrue2);
		assertTrue(beTrue3);
		warehouseFactory.deleteAllWarehouses();

	}

	@Test
	public void removeWarehouses() {

		WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();
		Warehouse warehouse = new Warehouse("W121", "12100");
		Warehouse warehouse2 = new Warehouse("W1213", "12110");
		warehouseFactory.addWarehouse(warehouse);
		warehouseFactory.addWarehouse(warehouse2);

		List<Warehouse> warehousesItems = new ArrayList<>();
		warehousesItems.add(warehouse2);
		warehousesItems.add(warehouse);

		// Remove a single warehouse
		boolean beTrue1 = warehouseFactory.removeWarehouse(warehouse);
		boolean beFalse1 = warehouseFactory.warehouseExists("12100");
		int warehousesSize = warehouseFactory.getWarehousesList().size();

		//Remove all warehouses from supplied list
		boolean beTrue2 = warehouseFactory.addWarehouse(warehouse);
		warehouseFactory.removeAllWarehouses(warehousesItems);
		int warehousesSize2 = warehouseFactory.getWarehousesList().size();

		assertTrue(beTrue1);
		assertFalse(beFalse1);
		assertEquals(1, warehousesSize);
		assertTrue(beTrue2);
		assertEquals(0, warehousesSize2);
		warehouseFactory.deleteAllWarehouses();
	}

}
