package com.generic.androidtracker.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.generic.androidtracker.interfaces.AddWarehouseDialogListener;
import com.generic.androidtracker.interfaces.OnWarehouseListener;
import com.generic.androidtracker.warehousemvp.AddWarehouseDialogFragment;
import com.generic.androidtracker.R;
import com.generic.androidtracker.interfaces.WarehouseTrackerMVP;
import com.generic.androidtracker.warehousemvp.WarehouseActivityPresenter;
import com.generic.androidtracker.WarehouseApplication;
import com.generic.androidtracker.warehousemvp.WarehouseRecyclerAdapter;
import com.generic.models.Warehouse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class WarehouseActivity extends AppCompatActivity
        implements WarehouseTrackerMVP.WarehouseView, AddWarehouseDialogListener, OnWarehouseListener {

    List<Warehouse> warehouses;

    private WarehouseTrackerMVP.WarehousePresenter presenter;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 5;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_recycler_view);
        Toolbar toolbar =  findViewById(R.id.my_toolbar);
        toolbar.setTitle("Warehouses");
        toolbar.inflateMenu(R.menu.bottom_nav_menu);

        //  Check Storage Permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        }


        final WarehouseApplication application = (WarehouseApplication) getApplication();

        presenter = new WarehouseActivityPresenter(application);
        presenter.setView(this);


        FloatingActionButton addButton = findViewById(R.id.fab);

        addButton.setOnClickListener(e -> presenter.addWarehouseClicked());
    }


    @Override
    public void showWarehouses(List<Warehouse> warehouses) {
        this.warehouses = warehouses;
        RecyclerView recyclerView = findViewById(R.id.cardList);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        WarehouseRecyclerAdapter warehouseRecyclerAdapter = new WarehouseRecyclerAdapter(warehouses, this);

        recyclerView.setAdapter(warehouseRecyclerAdapter);

    }

    @Override
    public void showAddNewWarehouse() {
        showEditDialog();
    }

    private void showEditDialog(){
        FragmentManager fm = getSupportFragmentManager();
        AddWarehouseDialogFragment editNameDialogFragment = AddWarehouseDialogFragment.newInstance("Add New Warehouse");
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    @Override
    public void onFinishEditDialog(String warehouseIDInput,
                                   String warehouseNameInput,
                                   String freightStatus) {

        presenter.addWarehouseCompleted(warehouseNameInput, warehouseIDInput, freightStatus);
        presenter.setView(this);

    }

    /**
     * Indicates when the user has responded to a permission request
     * @param requestCode The request code
     * @param permissions The permissions requested
     * @param grantResults The result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case WRITE_STORAGE_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission required, closing application", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }

    @Override
    public void onWarehouseClicked(int position) {
        String warehouseID = warehouses.get(position).getId();
        Intent intent = new Intent(this, ShipmentActivity.class);
        intent.putExtra("warehouseID", warehouseID);
        startActivity(intent);
    }


}
