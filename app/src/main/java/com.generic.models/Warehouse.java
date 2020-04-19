package com.generic.models;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * This class creates a model of a Warehouse.
 * @author GENERIC TEAM
 */

public class Warehouse extends PersistentJson {

	private static final String WAREHOUSE_DETAIL_FORMAT_STRING = "| WAREHOUSEID: %s| FREIGHT RECEIPT STATUS: %s| SHIPMENT AVALIABLE: %d|";

	// TODO: Add warehouseName attribute to class
	private String warehouseName;
	private boolean freightReceiptEnabled; // freight receipt
	private List<Shipment> shipments; // List of shipments

	/**
	 * Construct a new warehouse
	 * @param id warehouse identification number
	 */
	public Warehouse(String warehouseID) {
		this.shipments = new ArrayList<Shipment>();
		this.id = warehouseID;
		this.freightReceiptEnabled = true;
	}

	/**
	 * This constructor creates
	 * @param warehouseName
	 * @param warehouseID
	 */
	public Warehouse(String warehouseName, String warehouseID) {
		this(warehouseID);
		this.warehouseName = warehouseName;
	}

	/**
	 * Enables freight receipt
	 */
	public void enableFreight() {
		freightReceiptEnabled = true;
	}

	/**
	 * Disables freight receipt
	 */
	public void disableFreight() {
		freightReceiptEnabled = false;
	}

	/**
	 * Gets the freightReceipt Status
	 * @return freightReceipt
	 */
	public boolean getFreightReceiptEnabled() {
		return freightReceiptEnabled;
	}

	public List<Shipment> getShipmentList() {
		// how to return a const (immutable) ?
		return shipments;
	}

	/**
	 * Gets the warehouseID
	 * @return warehouseID
	 */
	public String getWarehouseID() {
		return id;
	}

	// TODO: Create a getter method(getWarehouseName) for warehouseName attribute
	/**
	 * Gets warehouse name
	 * @return the warehouseName
	 */
	public String getWarehouseName() {
		return warehouseName;
	}

	/**
	 * Adds a shipment to the warehouse if freightReceipt
	 * enabled, does not add if it isn't enabled
	 * @param mShipment shipment to be received
	 * @return true if add successful, false if not.
	 */
	public boolean addShipment(Shipment mShipment) {
		if (freightReceiptEnabled) {
			shipments.add(mShipment);
			return true;
		}
		return false;
	}

	/**
	 * Getter for number of shipments
	 * in the warehouse
	 * @return
	 */
	public int getShipmentSize() {
		return shipments.size();
	}

	@Override
	public String toString() {
		String headerString = String.format(WAREHOUSE_DETAIL_FORMAT_STRING, id, (freightReceiptEnabled) ? "ENABLED" : "ENDED", getShipmentSize());
		String headerFormat = new String(new char[headerString.length()]).replace("\0", "-");
		StringBuilder warehouseInfo = new StringBuilder()
				.append(headerFormat).append("\n")
				.append(headerString).append("\n")
				.append(headerFormat).append("\n")
				.append("          *SHIPMENT RECEIVED*").append("\n")
				.append("****************************************").append("\n");
		if (!isEmpty()) {
			int count = 1;
			for (Shipment shipment : shipments) {
				String shipmentInfo = shipment.toString();
				warehouseInfo.append(count++).append(".").append(shipmentInfo).append("\n");
				warehouseInfo.append("****************************************").append("\n");
			}
		} else {
			warehouseInfo.append("        *NO SHIPMENTS RECEIVED YET*").append("\n");
		}
		return warehouseInfo.toString();
	}

	/**
	 * Checks if warehouse is empty.
	 * Obviously so we don't loop through an
	 * try to print an empty list.
	 * @return true if shipments size
	 * 		   is 0 and false if not
	 */
	public boolean isEmpty() {
		return (shipments.size() == 0);
	}

	/**
	 * Creates a JsonOnject from this Warehouse instance
	 * @return JSONObject the JsonObject created.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject warehouseInfo = new JSONObject();
		JSONArray shipmentList = new JSONArray();
		JSONObject shipmentContents;

		for (Shipment shipment : shipments) {
			shipmentContents = shipment.toJSON();
			shipmentList.add(shipmentContents);
		}
		warehouseInfo.put("Warehouse_" + id, shipmentList);
		return warehouseInfo;

	}
}
