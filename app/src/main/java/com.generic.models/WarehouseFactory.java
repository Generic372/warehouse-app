package com.generic.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class will be responsible for creating and tracking a collection of warehouses and
 * the shipments in them. This will allow for consistency of objects as a
 * shipment can't exist without a warehouse. It would provide only the required
 * functionalities such as adding shipment, printing warehouse details, enabling
 * and disabled the freight receipt of a warehouse.
 *
 * @author GENERIC TEAM
 *
 */
public final class WarehouseFactory extends PersistentJson {
	private static WarehouseFactory warehouseTracker;

	// Stores a collection of warehouses mapped by their id
	private Map<String, Warehouse> warehouses;

	// private constructor
	private WarehouseFactory() {
	}

	public static WarehouseFactory getInstance() {
		if (warehouseTracker == null) {
			synchronized (WarehouseFactory.class) {
				warehouseTracker = new WarehouseFactory();
				warehouseTracker.warehouses = new HashMap<>();
				warehouseTracker.id = "warehouse_contents";
			}
		}
		return warehouseTracker;
	}

	/**
	 * Create a method that checks if a particular warehouse has it's freight
	 * receipt enabled
	 *
	 * @param warehouseID the warehouseID
	 * @return true if added, false if not
	 */

	public boolean freightIsEnabled(String warehouseID) {
		return warehouses.get(warehouseID).getFreightReceiptEnabled();
	}

	/**
	 *
	 * Adds a new warehouse to the warehouse collection. If the warehouse already
	 * exists, we return false.
	 *
	 * @param mWarehouse warehouse to add.
	 * @return true if add was successful, false if add wasn't.
	 */
	public boolean addWarehouse(Warehouse mWarehouse) {
		if (warehouses.get(mWarehouse.getWarehouseID()) == null) {
			warehouses.put(mWarehouse.getWarehouseID(), mWarehouse);
			return true;
		}
		return false;
	}

	public boolean addWarehouse(String warehouseName, String warehouseID) {
		return addWarehouse(new Warehouse(warehouseName, warehouseID));
	}

	/**
	 * Add shipment to a warehouse using warehouseID
	 *
	 * @param warehouseID warehouseID
	 * @param mShipment   shipment to add
	 * @return re
	 */
	public boolean addShipment(String warehouseID, Shipment mShipment) {
		Warehouse theWarehouse = warehouses.get(warehouseID);
		if (theWarehouse != null && theWarehouse.getFreightReceiptEnabled()) {
			theWarehouse.addShipment(mShipment);
			return true;
		}
		return false;
	}

	/**
	 * Gets a Warehouse Object
	 *
	 * @param warehouseID
	 * @return warehouse object
	 */
	public Warehouse getWarehouse(String warehouseID) {
		return warehouses.get(warehouseID);
	}

	/**
	 * Removes all warehouses in a given list
	 *
	 * @param warehousesItems list of warehouses to delete
	 */
	public void removeAllWarehouses(List<Warehouse> warehousesItems) {
		for (Warehouse warehouse : warehousesItems) {
			removeWarehouse(warehouse);
		}
	}

	/**
	 * Removes a Warehouse from the Tracker
	 *
	 * @param warehouse warehouse object to remove
	 * @return true if remove was successful, false if not
	 */
	public boolean removeWarehouse(Warehouse warehouse) {
		return warehouses.remove(warehouse.getId(), warehouse);
	}

	public void removeAllShipments(String warehouseID, List<Shipment> shipmentItems) {
		for (Shipment shipment : shipmentItems) {
			removeShipment(warehouseID, shipment);
		}
	}

	/**
	 * Removes a shipment from specified Warehouse
	 *
	 * @param warehouseID
	 * @param shipment    Shipment to remove
	 * @return true if remove was successful, false if not
	 */
	public boolean removeShipment(String warehouseID, Shipment shipment) {
		return getWarehouse(warehouseID).getShipmentList().remove(shipment);
	}

	/**
	 * Adds shipment to a warehouse using warehouse object
	 *
	 * @param theWarehouse warehouseID
	 * @param mShipment    shipment to add
	 * @return true if added, false if not
	 */
	public boolean addShipment(Warehouse theWarehouse, Shipment mShipment) {
		return addShipment(theWarehouse.getId(), mShipment);
	}

	/**
	 * Enables a freight receipt in a Warehouse.
	 *
	 * @param warehouseID warehouse id
	 * @return true if freight was successfully enabled, false if not.
	 */
	public boolean enableFreight(String warehouseID) {
		Warehouse theWarehouse = warehouses.get(warehouseID);
		if (theWarehouse != null) {
			theWarehouse.enableFreight();
			return true;
		}
		return false;
	}

	/**
	 * Disables freight receipt in a warehouse
	 *
	 * @param warehouseID warehouse id
	 * @return true if freight was successfully disabled, false if not.
	 */
	public boolean endFreight(String warehouseID) {
		Warehouse theWarehouse = warehouses.get(warehouseID);
		if (theWarehouse != null) {
			theWarehouse.disableFreight();
			return true;
		}
		return false;
	}

	/**
	 * Checks if we have an empty collection of warehouses.
	 *
	 * @return true if empty, false if not.
	 */
	public boolean isEmpty() {
		return warehouses.size() == 0;
	}

	/**
	 * Getter for shipments size for a specified warehouse
	 *
	 * @return the size of the warehouse, -1 if warehouse doesn't exist
	 */
	public int getWarehouseShipmentsSize(String warehouseID) {
		Warehouse theWarehouse = warehouses.get(warehouseID);
		return (theWarehouse != null ? theWarehouse.getShipmentSize() : -1);
	}

	/**
	 * Checks if a warehouse exists
	 *
	 * @param warehouseID warehouse id
	 * @return true if it does, false if not.
	 */
	public boolean warehouseExists(String warehouseID) {
		return (warehouses.get(warehouseID) != null);
	}

	/**
	 * Prints information about a warehouse for user to see.
	 *
	 * @param warehouseID the warehouse id number.
	 */
	public void printWarehouseDetails(String warehouseID) {
		Warehouse theWarehouse = warehouses.get(warehouseID);
		if (theWarehouse == null) {
			System.out.println("Warehouse cannot be found!");
			return; // TODO: throw exception
		}
		System.out.println(theWarehouse.toString());
	}

	/**
	 * Gets the list of warehouses, needed by GUI to populate the TableView
	 * @return ObservableList of warehouses
	 */
	public ObservableList<Warehouse> getWarehousesList() {
		List<Warehouse> warehousesAsList = new ArrayList<Warehouse>(warehouses.values());
		return FXCollections.observableList(warehousesAsList);
	}

	/**
	 * Prints all available warehouses
	 */
	public void printAll() {
		warehouses.forEach((k, v) -> printWarehouseDetails(k));
	}

	/*
	 * this will be unconventional because we are trying to reproduce the original
	 * file and the original file have quite a strange format...
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSON() {

		// FOR REPLICATING ORIGINAL FILE FORMAT:
		JSONObject warehouseTracker = new JSONObject();
		JSONArray shipmentJsonList = new JSONArray();

		List<Warehouse> warehousesList = new ArrayList<>(warehouses.values());
		List<Shipment> shipmentList;

		for (Warehouse warehouse : warehousesList) {
			shipmentList = warehouse.getShipmentList();
			// to reproduce the original file
			for (Shipment shipment : shipmentList) {
				JSONObject shipmentJson = shipment.toJSON();
				// "shipment has-a warehouse instead of warehouse has-many shipments"
				shipmentJson.put("warehouse_id", warehouse.getId());
				shipmentJson.put("warehouse_name", warehouse.getWarehouseName());
				shipmentJsonList.add(shipmentJson);
			}
		}
		warehouseTracker.put(id, shipmentJsonList);
		return warehouseTracker;
	}

	/**
	 * Adds parsed data from JSON/XML files
	 * @param parsedData Map containing data about attributes of warehouse /
	 *                   shipments
	 */
	public void addParsedData(Map<String, Object> parsedData) {
		Warehouse warehouse = new Warehouse((String)parsedData.get("warehouseName"),(String)parsedData.get("warehouseID"));

		String fTypeString = (String) parsedData.get("fTypeString");
		FreightType fTypeEnum = (fTypeString == null) ? FreightType.NULL : FreightType.valueOf(fTypeString.toUpperCase());

		String weightUnitString = (String)parsedData.get("wUnitString");
		WeightUnit weightUnitEnum = (weightUnitString == null) ? WeightUnit.NULL : WeightUnit.valueOf(weightUnitString.toUpperCase());

		// build a shipment
		Shipment shipment = new Shipment.Builder()
				.id((String)parsedData.get("shipmentID"))
				.type(fTypeEnum)
				.weight(((Number) parsedData.get("weight")).doubleValue())
				.weightUnit(weightUnitEnum)
				.date(((Number)parsedData.get("receiptDate")).longValue()).build();
		// add the warehouse
		warehouseTracker.addWarehouse(warehouse);
		// add the shipment to the warehouse
		warehouseTracker.addShipment(warehouse.getId(), shipment);

	}

	/**
	 * Creates new shipment from textfields input and adds
	 * it to a warehouse. This method is here so
	 * as to satisfy separation of concerns.
	 * @param id the warehouseId
	 * @param textFieldInput a map of user inputs
	 */
	public boolean addShipment(String id, Map<String, Object> textFieldInput) {
		if (textFieldInput.get("fType") != null && textFieldInput.get("weightUnit") != null) {
			Shipment nShipment = new Shipment
					.Builder()
					.id((String)textFieldInput.get("shipmentID"))
					.type((FreightType)textFieldInput.get("fType"))
					.weight((Double)textFieldInput.get("weight"))
					.weightUnit((WeightUnit)textFieldInput.get("weightUnit"))
					.date((Long)textFieldInput.get("receiptDate"))
					.build();
			return addShipment(id, nShipment);
		}
		return false;
	}

	/**
	 * Deletes all warehouses for testing purposes
	 */
	public void deleteAllWarehouses() {
		warehouses.clear();
	}

}
