package com.generic.androidtracker.interfaces;

/**
 * Defines the listener interface  for adding shipments
 * with a method passing back data result.
 */
public interface AddShipmentDialogListener {
    /**
     * Receives input data from add shipment dialog fragment.
     * @param shipmentID shipment id input.
     * @param weight weight input.
     * @param receiptDate receipt date input.
     * @param freightType freight type input.
     * @param weightUnit weight unit input.
     */
    void onFinishEditDialog(String shipmentID,
                            String weight,
                            String receiptDate,
                            String freightType,
                            String weightUnit);
}