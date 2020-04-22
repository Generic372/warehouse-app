package com.generic.androidtracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.generic.androidtracker.R;
import com.generic.models.Shipment;

import java.util.List;

public class ShipmentAdapter extends RecyclerView.Adapter<ShipmentAdapter.ShipmentViewHolder> {

    List<Shipment> shipments;

    public static class ShipmentViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        //ImageView warehouseImage;
        TextView shipmentID;
        TextView receiptDate;
        //TextView depatureDate;
        TextView freightType;
        TextView weightUnit;
        TextView weight;


        public ShipmentViewHolder(View itemView){
            super(itemView);
            cardView = itemView.findViewById(R.id.shipment_cv);
            shipmentID = itemView.findViewById(R.id.shipment_id);
            receiptDate = itemView.findViewById(R.id.receipt_date);
            freightType = itemView.findViewById(R.id.freight_type);
            weight = itemView.findViewById(R.id.weight);
            weightUnit = itemView.findViewById(R.id.weight_unit);
        }
    }

    @Override
    public ShipmentAdapter.ShipmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shipment_list_item, viewGroup, false);
        return new ShipmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ShipmentViewHolder warehouseViewHolder, int i) {
        warehouseViewHolder.shipmentID.setText("Shipment ID: " + shipments.get(i).getId());
        warehouseViewHolder.freightType.setText("Freight Type: " + shipments.get(i).getFreight());
        warehouseViewHolder.receiptDate.setText("Receipt Date: " + shipments.get(i).getReceiptDateString());
        warehouseViewHolder.weight.setText("Weight: " + shipments.get(i).getWeight());
        //warehouseViewHolder.depatureDate.setText("Receipt Date: " + shipments.get(i).getDepatureDate());
        warehouseViewHolder.weightUnit.setText("Weight Unit: " + shipments.get(i).getWeightUnit());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) { super.onAttachedToRecyclerView(recyclerView); }

    public ShipmentAdapter(List<Shipment> shipments){ this.shipments = shipments; }

    @Override
    public int getItemCount() {
        return shipments.size();
    }
}
