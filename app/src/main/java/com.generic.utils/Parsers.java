package com.generic.utils;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.generic.models.WarehouseFactory;
import com.generic.xml.models.ShipmentXml;
import com.generic.xml.models.ShipmentsXml;
import com.generic.xml.models.WarehouseXml;

/**
 * Parses data from Json and Xml files
 * @author GENERIC TEAM
 */
public class Parsers {

	private Parsers() {}

	/**
	 * Reads a file that is in JSON format containing various shipment information.
	 * @param filepath the path of JSON file
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void parseJson(String filepath) throws Exception  {
		JSONParser jsonParser = new JSONParser();
		FileReader reader = new FileReader(filepath);
		JSONObject jsonFile = (JSONObject) jsonParser.parse(reader);
		JSONArray warehouseContents = (JSONArray) jsonFile.get("warehouse_contents");
		warehouseContents.forEach(shipmentObject -> parseJsonContentsToObjects((JSONObject) shipmentObject));

		reader.close();
	}

	/**
	 * Parses and assigns shipment object for each warehouse
	 * @param shipmentObject shipment object in json
	 */
	private static void parseJsonContentsToObjects(JSONObject shipmentObject) {

		WarehouseFactory warehouseTracker = WarehouseFactory.getInstance();

		String warehouseID = (String) shipmentObject.get("warehouse_id");
		String warehouseName = (String) shipmentObject.get("warehouse_name");

		String fTypeString = (String) shipmentObject.get("shipment_method");

		String weightUnitString = (String)shipmentObject.get("weight_unit");

		Map<String, Object> parsedData = new HashMap<String, Object>();
		parsedData.put("warehouseID", warehouseID);
		parsedData.put("warehouseName", warehouseName);
		parsedData.put("shipmentID", shipmentObject.get("shipment_id"));
		parsedData.put("fTypeString", fTypeString);
		parsedData.put("wUnitString", weightUnitString);
		parsedData.put("weight", shipmentObject.get("weight"));
		parsedData.put("receiptDate", shipmentObject.get("receipt_date"));

		warehouseTracker.addParsedData(parsedData);
	}

	/**
	 * Reads the metadata from specified xml file.
	 * @param file xml file to parse.
	 * @throws Exception throws exceptions specified in the
	 * 					 simplexml API if parsing if unsuccessful.
	 */
	public static void parseXmlFromFile(File file) throws Exception {
		Serializer serializer = new Persister();
		ShipmentsXml shipments = null;

		shipments = serializer.read(ShipmentsXml.class, file);

		if (shipments != null) {
			shipments.getWarehouse().forEach(warehouseXmlObject -> parseXmlToWarehousePojo(warehouseXmlObject));
		}
	}

	/**
	 * Parse the xml warehouse object to actual warehouse object
	 * @param warehouseXmlObject the xml warehouse object
	 */
	private static void parseXmlToWarehousePojo(WarehouseXml warehouseXmlObject) {


		Map<String, Object> parsedData = new HashMap<String, Object>();


		String warehouseID = warehouseXmlObject.getId();
		String warehouseName = warehouseXmlObject.getName();
		//Warehouse mWarehouse = new Warehouse(warehouseName, warehouseID);
		//warehouseTracker.addWarehouse(mWarehouse);

		parsedData.put("warehouseID", warehouseID);
		parsedData.put("warehouseName", warehouseName);

		if (!warehouseXmlObject.getShipments().isEmpty()) {
			warehouseXmlObject.getShipments()
			.forEach(shipmentXmlObject -> parseToXmlShipmentsPojo(parsedData, shipmentXmlObject));
		}
	}

	/**
	 * Parse the xml shipment object to  actual shipment object
	 * @param parsedData the warehouseID to add shipment to.
	 * @param shipmentXmlObject the xml shipment object
	 */
	private static void parseToXmlShipmentsPojo(Map<String, Object> parsedData, ShipmentXml shipmentXmlObject) {
		WarehouseFactory warehouseTracker = WarehouseFactory.getInstance();

		parsedData.put("shipmentID", shipmentXmlObject.getId());
		parsedData.put("fTypeString", shipmentXmlObject.getType());
		parsedData.put("wUnitString", shipmentXmlObject.getWeightUnit());
		parsedData.put("weight", shipmentXmlObject.getWeight());
		parsedData.put("receiptDate", shipmentXmlObject.getReceiptDate());

		warehouseTracker.addParsedData(parsedData);

	}
}
