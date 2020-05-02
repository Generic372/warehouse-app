package com.generic.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.generic.models.WarehouseFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses JSON from file.
 * @author GENERIC-TEAM
 */
public class JsonParser implements IParser {
    /**
     * Reads a file that is in JSON format containing various shipment information.
     * @param filePath the path of JSON file
     * @throws Exception
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressWarnings("unchecked")
    @Override
    public void parse(String filePath) throws Exception{
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(filePath);
        JSONObject jsonFile = (JSONObject) jsonParser.parse(reader);
        JSONArray warehouseContents = (JSONArray) jsonFile.get("warehouse_contents");
        warehouseContents.forEach(warehouseObject -> parseJsonContentsToObjects((JSONObject) warehouseObject));
        reader.close();
    }

    /**
     * Parses and assigns shipment object for each warehouse
     * @param warehouseObject shipment object in json
     */
    private void parseJsonContentsToObjects(JSONObject warehouseObject) {

        WarehouseFactory warehouseTracker = WarehouseFactory.getInstance();

        String warehouseID = (String) warehouseObject.get("warehouse_id");
        String warehouseName = (String) warehouseObject.get("warehouse_name");
        String fTypeString = (String) warehouseObject.get("shipment_method");
        String weightUnitString = (String)warehouseObject.get("weight_unit");

        Map<String, Object> parsedData = new HashMap<String, Object>();
        parsedData.put("warehouseID", warehouseID);
        parsedData.put("warehouseName", warehouseName);
        parsedData.put("shipmentID", warehouseObject.get("shipment_id"));
        parsedData.put("fTypeString", fTypeString);
        parsedData.put("wUnitString", weightUnitString);
        parsedData.put("weight", warehouseObject.get("weight"));
        parsedData.put("departure_date", warehouseObject.get("departure_date"));
        parsedData.put("receiptDate", warehouseObject.get("receipt_date"));

        warehouseTracker.addParsedData(parsedData);
    }
}
