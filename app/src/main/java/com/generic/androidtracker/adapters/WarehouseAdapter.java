package com.generic.androidtracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.generic.androidtracker.R;
import com.generic.models.Warehouse;

import java.util.List;

public class WarehouseAdapter extends RecyclerView.Adapter<WarehouseAdapter.WarehouseViewHolder> {

    List<Warehouse> warehouses;

    public static class WarehouseViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        //ImageView warehouseImage;
        TextView warehouseID;
        TextView warehouseName;
        TextView freightReceipt;
        TextView shipmentsNum;


        public WarehouseViewHolder(View itemView){
            super(itemView);
            cardView = itemView.findViewById(R.id.warehouse_cv);
            warehouseID = itemView.findViewById(R.id.warehouse_id);
            warehouseName = itemView.findViewById(R.id.warehouse_name);
            freightReceipt = itemView.findViewById(R.id.freight_receipt);
            shipmentsNum = itemView.findViewById(R.id.shipments_available);
        }
    }

    @Override
    public WarehouseAdapter.WarehouseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.warehouse_list_item, viewGroup, false);
        return new WarehouseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WarehouseViewHolder warehouseViewHolder, int i) {
        warehouseViewHolder.warehouseID.setText("Warehouse ID: " + warehouses.get(i).getWarehouseID());
        warehouseViewHolder.warehouseName.setText("Warehouse Name: " + warehouses.get(i).getWarehouseName());
        String freightReceiptStatus = warehouses.get(i).getFreightReceiptEnabled() ? "ENABLED" : "DISABLED";
        warehouseViewHolder.freightReceipt.setText("Receipt Status: " + freightReceiptStatus);
        warehouseViewHolder.shipmentsNum.setText("Shipment Available: " + warehouses.get(i).getShipmentSize());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) { super.onAttachedToRecyclerView(recyclerView); }

    public WarehouseAdapter(List<Warehouse> warehouses){ this.warehouses = warehouses; }

    @Override
    public int getItemCount() {
        return warehouses.size();
    }
}
