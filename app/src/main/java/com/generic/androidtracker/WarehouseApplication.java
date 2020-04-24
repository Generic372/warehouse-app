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
 * Responsible for updating the models.
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
    public List<Warehouse> getWarehouses() {
        return warehouseFactory.getWarehousesList();
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
    public void onCreate() { super.onCreate(); }
}
