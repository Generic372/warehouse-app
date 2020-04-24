package com.generic.androidtracker.interfaces;

import com.generic.models.Shipment;
import com.generic.models.Warehouse;
import java.util.List;

/**
 * MVP Interface for the Warehouse
 * Application. Contains specifications
 * for Warehouse and Shipment MVP Components.
 */
public interface WarehouseTrackerMVP {

    interface WarehouseView {
        /**
         * Shows warehouse recycler view
         * @param warehouses warehouses to show
         */
        void showWarehouses(List<Warehouse> warehouses);

        /**
         * Shows add warehouse dialog fragment view
         */
        void showAddNewWarehouse();
    }

    interface WarehousePresenter {
        /**
         * Sets a view to the warehouse presenter.
         * @param view view to set.
         */
        void setView(WarehouseView view);

        /**
         * Handles the add warehouse click
         * event.
         */
        void addWarehouseClicked();

        /**
         * Handles the user input from
         * the add warehouse clicked
         * dialog fragment.
         * @param warehouseName the warehouse name.
         * @param warehouseID the warehouse id.
         * @param freightStatus the warehouse freight status.
         */
        void addWarehouseCompleted(String warehouseName,
                                   String warehouseID,
                                   String freightStatus);
    }

    interface WarehouseModel {
        /**
         * Gets all the warehouse objects
         * available.
         * @return a list of warehouses.
         */
        List<Warehouse> getWarehouses();

        /**
         * Creates a new warehouse object.
         * @param warehouseName the warehouse name.
         * @param warehouseID the warehouse id.
         * @param freightStatus the warehouse freight status.
         */
        void createWarehouse(String warehouseName,
                             String warehouseID,
                             String freightStatus);
    }

    interface ShipmentView {
        /**
         * Shows shipment recycler view
         * @param shipments shipments to show.
         */
        void showShipments(List<Shipment> shipments);

        /**
         * Shows add warehouse dialog fragment view
         */
        void showAddNewShipment();
    }



    interface ShipmentPresenter {
        /**
         * Sets a view to the shipment presenter.
         * @param view view to set.
         */
        void setView(ShipmentView view);

        /**
         * Handles the add shipment click
         * event.
         */
        void addShipmentClicked();

        /**
         * Handles the user input from
         * the add warehouse clicked
         * dialog fragment.
         * @param shipmentID the shipment id.
         * @param weight the shipment weight.
         * @param receiptDate the shipment receipt date.
         * @param freightType the shipment freight type.
         * @param weightUnit the shipment weight unit
         */
        void addShipmentCompleted(String shipmentID,
                                  String weight,
                                  String receiptDate,
                                  String freightType,
                                  String weightUnit);
    }

    interface ShipmentModel {
        /**
         * Gets all the shipment objects
         * available.
         * @return a list of shipments.
         */
        List<Shipment> getShipments(String warehouseID);

        /**
         * Creates a new shipment object.
         * @param shipmentID the shipment id.
         * @param weight the shipment weight.
         * @param receiptDate the shipment receipt date.
         * @param freightType the shipment freight type.
         * @param weightUnit the shipment weight unit
         */
        void createShipment(String warehouseID,
                            String shipmentID,
                            String weight,
                            String receiptDate,
                            String freightType,
                            String weightUnit);
    }

}


