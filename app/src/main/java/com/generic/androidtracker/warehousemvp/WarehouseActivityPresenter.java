package com.generic.androidtracker.warehousemvp;

import com.generic.androidtracker.interfaces.WarehouseTrackerMVP;

/**
 * Handles the presentation of warehouse model and view.
 */
public class WarehouseActivityPresenter implements WarehouseTrackerMVP.WarehousePresenter {

    private WarehouseTrackerMVP.WarehouseView view;
    private WarehouseTrackerMVP.WarehouseModel model;

    public WarehouseActivityPresenter(WarehouseTrackerMVP.WarehouseModel model){ this.model = model; }

    @Override
    public void setView(WarehouseTrackerMVP.WarehouseView view) {
        this.view = view;
        view.showWarehouses(model.getWarehouses());
    }

    @Override
    public void addWarehouseClicked() {
        view.showAddNewWarehouse();
    }

    @Override
    public void addWarehouseCompleted(String warehouseName, String warehouseID, String freightStatus) {
        model.createWarehouse(warehouseName, warehouseID, freightStatus);
        view.showWarehouses(model.getWarehouses());
    }
}
