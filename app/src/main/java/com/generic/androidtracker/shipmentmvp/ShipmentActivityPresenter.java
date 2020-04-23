package com.generic.androidtracker.shipmentmvp;

import com.generic.androidtracker.interfaces.WarehouseTrackerMVP;

/**
 * Handles the presentation of the shipment view and model.
 */
public class ShipmentActivityPresenter implements WarehouseTrackerMVP.ShipmentPresenter {
    private WarehouseTrackerMVP.ShipmentView view;
    private WarehouseTrackerMVP.ShipmentModel model;
    private String warehouseID;

    public ShipmentActivityPresenter(WarehouseTrackerMVP.ShipmentModel model, String warehouseID){
        this.model = model;
        this.warehouseID = warehouseID;
    }


    @Override
    public void setView(WarehouseTrackerMVP.ShipmentView view) {
        this.view = view;
        view.showShipments(model.getShipments(warehouseID));
    }

    @Override
    public void addShipmentClicked() {
        view.showAddNewShipment();
    }

    @Override
    public void addShipmentCompleted(String shipmentID,
                                     String weight,
                                     String receiptDate,
                                     String freightType,
                                     String weightUnit) {
        model.createShipment(warehouseID,
                shipmentID,
                weight,
                receiptDate,
                freightType,
                weightUnit);
        view.showShipments(model.getShipments(warehouseID));
    }
}
