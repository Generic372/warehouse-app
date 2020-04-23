package com.generic.androidtracker.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.generic.androidtracker.R;
import com.generic.androidtracker.WarehouseApplication;
import com.generic.androidtracker.shipmentmvp.ShipmentAdapter;
import com.generic.androidtracker.interfaces.AddShipmentDialogListener;
import com.generic.androidtracker.interfaces.WarehouseTrackerMVP;
import com.generic.androidtracker.shipmentmvp.AddShipmentDialogFragment;
import com.generic.androidtracker.shipmentmvp.ShipmentActivityPresenter;
import com.generic.models.Shipment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

/**
 *
 */
public class ShipmentActivity extends AppCompatActivity implements
        WarehouseTrackerMVP.ShipmentView, AddShipmentDialogListener {

    private WarehouseTrackerMVP.ShipmentPresenter presenter;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 5;
    String warehouseID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        warehouseID = getIntent().getStringExtra("warehouseID");
        setContentView(R.layout.my_recycler_view);
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Warehouse " + warehouseID + " Shipments");
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

        presenter = new ShipmentActivityPresenter(application, warehouseID);
        presenter.setView(this);

        FloatingActionButton addButton = findViewById(R.id.fab);

        addButton.setOnClickListener(e -> presenter.addShipmentClicked());
    }


    @Override
    public void showShipments(List<Shipment> shipments) {
        RecyclerView recyclerView = findViewById(R.id.cardList);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        ShipmentAdapter shipmentAdapter = new ShipmentAdapter(shipments);

        recyclerView.setAdapter(shipmentAdapter);

    }

    @Override
    public void showAddNewShipment() {
        showEditDialog();
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AddShipmentDialogFragment editNameDialogFragment = AddShipmentDialogFragment.newInstance("Add New Warehouse");
        editNameDialogFragment.show(fm, "fragment_edit_name");
    }

    /**
     * Receives input data from dialog activity.
     * @param shipmentID shipment id input.
     * @param weight weight input.
     * @param receiptDate receipt date input.
     * @param freightType freight type input.
     * @param weightUnit weight unit input.
     */
    @Override
    public void onFinishEditDialog(String shipmentID,
                                   String weight,
                                   String receiptDate,
                                   String freightType,
                                   String weightUnit) {
        presenter.addShipmentCompleted(shipmentID,
                weight,
                receiptDate,
                freightType,
                weightUnit);
        presenter.setView(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent warehouseChanged;
        warehouseChanged = new Intent(this, WarehouseActivity.class);
        startActivity(warehouseChanged);
    }

    /**
     * Indicates when the user has responded to a permission request
     *
     * @param requestCode  The request code
     * @param permissions  The permissions requested
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
            }
        }
    }
}


