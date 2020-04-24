package com.generic.androidtracker.warehousemvp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.generic.androidtracker.R;
import com.generic.androidtracker.interfaces.OnWarehouseListener;
import com.generic.models.Warehouse;
import com.generic.models.WarehouseFactory;

import java.util.List;

/**
 * A Recycler adapter view holding warehouse cardView
 * items. WarehouseViewHolder handles each cardView.
 */
public class WarehouseRecyclerAdapter extends RecyclerView.Adapter<WarehouseRecyclerAdapter.WarehouseViewHolder> implements OnWarehouseListener{

    private List<Warehouse> warehouses;
    private OnWarehouseListener onWarehouseListener;
    private Warehouse warehouseToDelete;
    private Context wContext;
    private int toRemove;



    @Override
    public void onWarehouseClicked(int position) {}

    public static class WarehouseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardView;
        //ImageView warehouseImage;
        TextView warehouseID;
        TextView warehouseName;
        TextView freightReceipt;
        TextView shipmentsNum;
        Button freightButton;
        Button deleteButton;
        OnWarehouseListener onWarehouseListener;



        public WarehouseViewHolder(View itemView, OnWarehouseListener onWarehouseListener){
            super(itemView);
            cardView = itemView.findViewById(R.id.warehouse_cv);
            warehouseID = itemView.findViewById(R.id.warehouse_id);
            warehouseName = itemView.findViewById(R.id.warehouse_name);
            freightReceipt = itemView.findViewById(R.id.freight_receipt);
            shipmentsNum = itemView.findViewById(R.id.shipments_available);
            freightButton = itemView.findViewById(R.id.freight_button);
            deleteButton = itemView.findViewById(R.id.delete_button);

            this.onWarehouseListener = onWarehouseListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) { onWarehouseListener.onWarehouseClicked(getAdapterPosition()); }
    }

    @Override
    public WarehouseRecyclerAdapter.WarehouseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.warehouse_card_item, viewGroup, false);
        return new WarehouseViewHolder(v, onWarehouseListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(WarehouseViewHolder warehouseViewHolder, int i) {
        warehouseViewHolder.warehouseID.setText("Warehouse ID: " + warehouses.get(i).getWarehouseID());
        warehouseViewHolder.warehouseName.setText("Warehouse Name: " + warehouses.get(i).getWarehouseName());
        String freightReceiptStatus = warehouses.get(i).getFreightReceiptEnabled() ? "ENABLED" : "DISABLED";
        warehouseViewHolder.freightReceipt.setText("Receipt Status: " + freightReceiptStatus);
        warehouseViewHolder.shipmentsNum.setText("Shipment Available: " + warehouses.get(i).getShipmentSize());
        String buttonString = warehouses.get(i).getFreightReceiptEnabled() ? "DISABLE FREIGHT RECEIPT" : "ENABLE FREIGHT RECEIPT";
        warehouseViewHolder.freightButton.setText(buttonString);

        warehouseViewHolder.freightButton.setOnClickListener(e -> {
            if (warehouses.get(i).getFreightReceiptEnabled()){
                warehouses.get(i).disableFreight();
                notifyDataSetChanged();
            }else {
                warehouses.get(i).enableFreight();
                notifyDataSetChanged();
            }
        });

        warehouseViewHolder.deleteButton.setOnClickListener(e -> {
            toRemove = i;
            AlertDialog.Builder builder = new AlertDialog.Builder(wContext);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        });
    }

    /**
     * Dialog Listener for delete warehouse confirmation.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();
                    warehouseToDelete = warehouses.get(toRemove);
                    warehouses.remove(toRemove);
                    warehouseFactory.removeWarehouse(warehouseToDelete);
                    notifyItemRemoved(toRemove);
                    notifyItemRemoved(toRemove);
                    notifyItemRangeChanged(toRemove, warehouses.size());
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

    public WarehouseRecyclerAdapter(List<Warehouse> warehouses, OnWarehouseListener onWarehouseListener){
        this.warehouses = warehouses;
        this.onWarehouseListener = onWarehouseListener;
    }

    @Override
    public int getItemCount() { return warehouses.size(); }
}
