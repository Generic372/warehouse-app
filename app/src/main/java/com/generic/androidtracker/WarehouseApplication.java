package com.generic.androidtracker;

import android.app.Application;

import com.generic.androidtracker.interfaces.WarehouseTrackerMVP;
import com.generic.models.FreightType;
import com.generic.models.Shipment;
import com.generic.models.Warehouse;
import com.generic.models.WarehouseFactory;
import com.generic.models.WeightUnit;

import java.util.List;

/**
 * Responsible for manipulating models.
 */
public class WarehouseApplication extends Application implements
        WarehouseTrackerMVP.WarehouseModel, WarehouseTrackerMVP.ShipmentModel{

    private WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();


    @Override
    public void createWarehouse(String warehouseName,
                                String warehouseID,
                                String freightStatus) {

        warehouseFactory.addWarehouse(warehouseName, warehouseID);
        if (freightStatus != null){
            if (freightStatus.startsWith("E")){
                warehouseFactory.enableFreight(warehouseID);
            }else{
                warehouseFactory.endFreight(warehouseID);
            }
        }

    }

    @Override
    public List<Shipment> getShipments(String warehouseID) {
        return warehouseFactory.getWarehouse(warehouseID).getShipmentList();
    }



    @Override
    public void createShipment(String warehouseID,
                               String shipmentID,
                               String weight,
                               String receiptDate,
                               String freightType,
                               String weightUnit) {
        Shipment shipment = new Shipment
                .Builder()
                .id(shipmentID)
                .date(Long.parseLong(receiptDate))
                .type(FreightType.valueOf(freightType))
                .weight(Double.parseDouble(weight))
                .weightUnit(WeightUnit.valueOf(weightUnit))
                .build();

        warehouseFactory.addShipment(warehouseID, shipment);
    }

    @Override
    public List<Warehouse> getWarehouses() {
        return warehouseFactory.getWarehousesList();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Warehouse warehouse = new Warehouse("W121", "12100");
        Warehouse warehouse2 = new Warehouse("W1213", "12110");
        Warehouse warehouse3 = new Warehouse("W1293", "10110");
        Warehouse warehouse4 = new Warehouse("W1223", "11110");
        Warehouse warehouse5 = new Warehouse("W1203", "14110");
        Warehouse warehouse6 = new Warehouse("W1293", "10170");
        Warehouse warehouse7 = new Warehouse("W1293", "15110");

        warehouseFactory.addWarehouse(warehouse);
        warehouseFactory.addWarehouse(warehouse2);
        warehouseFactory.addWarehouse(warehouse3);
        warehouseFactory.addWarehouse(warehouse4);
        warehouseFactory.addWarehouse(warehouse5);
        warehouseFactory.addWarehouse(warehouse6);
        warehouseFactory.addWarehouse(warehouse7);

        for (int i = 0; i <= 6; i++){
            Shipment shipment = new Shipment
                    .Builder()
                    .id("Shipment " + i)
                    .date(1112121313L)
                    .type(FreightType.AIR)
                    .weight(98.9D)
                    .weightUnit(WeightUnit.KG)
                    .build();
            warehouseFactory.getWarehousesList().get(1).addShipment(shipment);
        }
    }
}
