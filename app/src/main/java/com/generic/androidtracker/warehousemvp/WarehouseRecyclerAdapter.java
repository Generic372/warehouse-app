package com.generic.androidtracker.warehousemvp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.generic.androidtracker.R;
import com.generic.androidtracker.interfaces.OnWarehouseListener;
import com.generic.models.Warehouse;

import java.util.List;

/**
 * A Recycler adapter view holding warehouse cardView
 * items. WarehouseViewHolder handles each cardView.
 */
public class WarehouseRecyclerAdapter extends RecyclerView.Adapter<WarehouseRecyclerAdapter.WarehouseViewHolder> implements OnWarehouseListener{

    private List<Warehouse> warehouses;
    private OnWarehouseListener onWarehouseListener;

    @Override
    public void onWarehouseClicked(int position) {}

    public static class WarehouseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        //ImageView warehouseImage;
        TextView warehouseID;
        TextView warehouseName;
        TextView freightReceipt;
        TextView shipmentsNum;
        OnWarehouseListener onWarehouseListener;


        public WarehouseViewHolder(View itemView, OnWarehouseListener onWarehouseListener){
            super(itemView);
            cardView = itemView.findViewById(R.id.warehouse_cv);
            warehouseID = itemView.findViewById(R.id.warehouse_id);
            warehouseName = itemView.findViewById(R.id.warehouse_name);
            freightReceipt = itemView.findViewById(R.id.freight_receipt);
            shipmentsNum = itemView.findViewById(R.id.shipments_available);
            this.onWarehouseListener = onWarehouseListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) { onWarehouseListener.onWarehouseClicked(getAdapterPosition()); }
    }

    @Override
    public WarehouseRecyclerAdapter.WarehouseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.warehouse_list_item, viewGroup, false);
        return new WarehouseViewHolder(v, onWarehouseListener);
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

    public WarehouseRecyclerAdapter(List<Warehouse> warehouses, OnWarehouseListener onWarehouseListener){
        this.warehouses = warehouses;
        this.onWarehouseListener = onWarehouseListener;
    }

    @Override
    public int getItemCount() { return warehouses.size(); }
}
