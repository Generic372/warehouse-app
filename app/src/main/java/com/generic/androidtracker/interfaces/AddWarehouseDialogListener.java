package com.generic.androidtracker.interfaces;

/**
 * Interface for a dialog listener.
 */
public interface AddWarehouseDialogListener {
    /**
     * Receives input data from add shipment dialog fragment.
     * @param warehouseIDInput warehouseid input.
     * @param warehouseNameInput warehousename input.
     * @param freightStatus freight status input.
     */
    void onFinishEditDialog(String warehouseIDInput,
                            String warehouseNameInput,
                            String freightStatus);
}
