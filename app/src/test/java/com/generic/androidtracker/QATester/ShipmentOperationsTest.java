package com.generic.androidtracker.QATester;

import com.generic.models.FreightType;
import com.generic.models.Shipment;
import com.generic.models.Warehouse;
import com.generic.models.WarehouseFactory;
import com.generic.models.WeightUnit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * A class to run unit tests against shipment operations
 * @author GENERIC TEAM
 */
public class ShipmentOperationsTest {

	@Test
	public void addShipmentToANonExistentWarehouse() {

		WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();
		Shipment shipment = new Shipment
				.Builder()
				.id("1212ad")
				.type(FreightType.TRUCK)
				.date(1121313100L)
				.weight(323D)
				.weightUnit(WeightUnit.KG)
				.build();

		boolean result = warehouseFactory.addShipment("1221", shipment);

		assertFalse(result);

		warehouseFactory.deleteAllWarehouses();
	}

	@Test
	public void addShipmentToAnExistingWarehouse() {

		WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();
		Warehouse warehouse = new Warehouse("W120", "12121");

		warehouseFactory.addWarehouse(warehouse);
		Shipment shipment = new Shipment
				.Builder()
				.id("1212ad")
				.type(FreightType.TRUCK)
				.date(1121313100L)
				.weight(323D)
				.weightUnit(WeightUnit.KG)
				.build();
		boolean result = warehouseFactory.addShipment(warehouse, shipment);

		assertTrue(result);

		warehouseFactory.deleteAllWarehouses();

	}

	@Test
	public void addShipmentFromGUITextField() {
		WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();
		HashMap<String, Object> textFieldsMap = new HashMap<>();
		Warehouse warehouse = new Warehouse("W120", "11221");

		warehouseFactory.addWarehouse(warehouse);
		textFieldsMap.put("shipmentID", "121as");
		textFieldsMap.put("fType", FreightType.AIR);
		textFieldsMap.put("weightUnit", WeightUnit.KG);
		textFieldsMap.put("weight", 121.03D);
		textFieldsMap.put("receiptDate", 1219219121L);

		boolean result = warehouseFactory.addShipment("11221", textFieldsMap);

		assertTrue(result);

		warehouseFactory.deleteAllWarehouses();
	}

	@Test
	public void removeShipments () {

		WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();

		Warehouse warehouse = new Warehouse("W120", "1212");
		warehouseFactory.addWarehouse(warehouse);
		List<Shipment> shipments = new ArrayList<Shipment>();

		Shipment shipment = new Shipment
				.Builder()
				.id("12aad")
				.type(FreightType.TRUCK)
				.date(1121313100L)
				.weight(323D)
				.build();
		Shipment shipment2 = new Shipment
				.Builder()
				.id("12a1d")
				.type(FreightType.TRUCK)
				.date(1121313100L)
				.weight(323D)
				.build();

		warehouseFactory.addShipment("1212", shipment);
		warehouseFactory.addShipment("1212", shipment2);

		// remove a single shipment
		boolean result1 = warehouseFactory.removeShipment("1212", shipment2);
		int shipmentSize = warehouseFactory.getWarehouseShipmentsSize("1212");

		// remove all shipments
		warehouseFactory.addShipment("1212", shipment2);
		shipments.add(shipment);
		shipments.add(shipment2);
		warehouseFactory.removeAllShipments("1212", shipments);
		int shipmentSize2 = warehouseFactory.getWarehouseShipmentsSize("1212");

		assertTrue(result1);
		assertEquals(1, shipmentSize);
		assertEquals(0, shipmentSize2);

		warehouseFactory.deleteAllWarehouses();



	}


}
