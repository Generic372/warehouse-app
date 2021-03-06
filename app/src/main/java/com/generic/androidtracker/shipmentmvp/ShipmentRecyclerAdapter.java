package com.generic.androidtracker.shipmentmvp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.generic.androidtracker.R;
import com.generic.models.Shipment;
import com.generic.models.WarehouseFactory;

import java.util.List;

/**
 * A Recycler adapter view holding shipment cardView
 * items. ShipmentViewHolder handles each cardView.
 */
public class ShipmentRecyclerAdapter extends RecyclerView.Adapter<ShipmentRecyclerAdapter.ShipmentViewHolder> {

    private final String warehouseID;
    List<Shipment> shipments;
    private Context wContext;
    private int toRemove;

    public static class ShipmentViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        //ImageView warehouseImage;
        TextView shipmentID;
        TextView receiptDate;
        TextView departureDate;
        TextView freightType;
        TextView weightUnit;
        TextView weight;
        Button shipOutButton;
        Button deleteShipmentButton;

        public ShipmentViewHolder(View itemView){
            super(itemView);
            cardView = itemView.findViewById(R.id.shipment_cv);
            shipmentID = itemView.findViewById(R.id.shipment_id);
            receiptDate = itemView.findViewById(R.id.receipt_date);
            departureDate = itemView.findViewById(R.id.departure_date);
            freightType = itemView.findViewById(R.id.freight_type);
            weight = itemView.findViewById(R.id.weight);
            weightUnit = itemView.findViewById(R.id.weight_unit);
            shipOutButton = itemView.findViewById(R.id.ship_out_button);
            deleteShipmentButton = itemView.findViewById(R.id.delete_shipment_button);
        }
    }

    @Override
    public ShipmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shipment_card_item, viewGroup, false);
        return new ShipmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ShipmentViewHolder warehouseViewHolder, int i) {
        warehouseViewHolder.shipmentID.setText("Shipment ID: " + shipments.get(i).getId());
        warehouseViewHolder.freightType.setText("Freight Type: " + shipments.get(i).getFreight());
        warehouseViewHolder.receiptDate.setText("Receipt Date: " + shipments.get(i).getReceiptDateString());
        warehouseViewHolder.weight.setText("Weight: " + shipments.get(i).getWeight());
        warehouseViewHolder.departureDate.setText("Departure Date: " + shipments.get(i).getDepartureDateString());
        warehouseViewHolder.weightUnit.setText("Weight Unit: " + shipments.get(i).getWeightUnit());

        if (!shipments.get(i).getDepartureDateString().equalsIgnoreCase(Shipment.SHIPMENT_HAS_NOT_DEPARTED)){
            warehouseViewHolder.shipOutButton.setVisibility(View.INVISIBLE);
        }

        warehouseViewHolder.shipOutButton.setOnClickListener(e -> {
            Shipment toShipOut = shipments.get(i);
            WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();
            warehouseFactory.shipOutShipment(warehouseID, toShipOut);
            notifyDataSetChanged();
        });

        warehouseViewHolder.deleteShipmentButton.setOnClickListener(e -> {
            toRemove = i;
            AlertDialog.Builder builder = new AlertDialog.Builder(wContext);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        });
    }

    /**
     * Dialog Listener for delete shipment confirmation.
     */
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();
                    Shipment shipmentToDelete = shipments.get(toRemove);
                    shipments.remove(toRemove);
                    warehouseFactory.removeShipment(warehouseID, shipmentToDelete);
                    notifyItemRemoved(toRemove);
                    notifyItemRangeChanged(toRemove, shipments.size());
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        wContext = recyclerView.getContext();
    }

    public ShipmentRecyclerAdapter(List<Shipment> shipments, String warehouseID){
        this.shipments = shipments;
        this.warehouseID = warehouseID;
    }

    @Override
    public int getItemCount() {
        return shipments.size();
    }

}
