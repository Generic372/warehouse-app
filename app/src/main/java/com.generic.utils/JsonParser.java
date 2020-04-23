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
}
