package com.generic.androidtracker.shipmentmvp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.Environment;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.generic.androidtracker.warehousemvp.WarehouseActivity;
import com.generic.androidtracker.interfaces.AddShipmentDialogListener;
import com.generic.androidtracker.interfaces.WarehouseTrackerMVP;
import com.generic.models.Shipment;
import com.generic.models.Warehouse;
import com.generic.models.WarehouseFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.List;

/**
 * Handles the shipment view
 */
public class ShipmentActivity extends AppCompatActivity implements
        WarehouseTrackerMVP.ShipmentView, AddShipmentDialogListener {

    private WarehouseTrackerMVP.ShipmentPresenter presenter;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 5;
    private String warehouseID;
    private boolean freightEnabled;
    private FloatingActionButton addButton;
    private List<Shipment> shipments;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        warehouseID = getIntent().getStringExtra("warehouseID");
        freightEnabled = getIntent().getBooleanExtra("freightStatus", true);
        setContentView(R.layout.recycler_view);
        Toolbar toolbar =  findViewById(R.id.my_toolbar);
        toolbar.setTitle("Warehouse " + warehouseID + " Shipments");
        toolbar.inflateMenu(R.menu.shipment_menu);
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return onOptionsItemSelected(item);
                    }
                });

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

        addButton = findViewById(R.id.fab);

        // Hide button if freight receipt
        // is disabled.
        if (!freightEnabled){
            addButton.hide();
        }else{
            addButton.show();
        }

        addButton.setOnClickListener(e -> presenter.addShipmentClicked());
    }


    @Override
    public void showShipments(List<Shipment> shipments) {
        this.shipments = shipments;
        RecyclerView recyclerView = findViewById(R.id.cardList);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        ShipmentRecyclerAdapter shipmentRecyclerAdapter = new ShipmentRecyclerAdapter(shipments, warehouseID);

        recyclerView.setAdapter(shipmentRecyclerAdapter);

    }

    @Override
    public void showAddNewShipment() { showEditDialog(); }

    /**
     * Fires up dialog fragment
     */
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
        warehouseChanged.putExtra("ignoreSavedInstance", true);
        startActivity(warehouseChanged);
    }

    /**
     * Indicates when the user has responded to a permission request.
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.export_warehouse:
                exportWarehouseToExternalStorage();
                Toast.makeText(this, "Warehouse saved to downloads", Toast.LENGTH_SHORT).show();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Exports warehouse to downloads directory
     */
    private void exportWarehouseToExternalStorage() {
        WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();
        Warehouse warehouse = warehouseFactory.getWarehouse(warehouseID);
        File exportPath = new File (Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + String.format("/warehouse_%s contents.json", warehouseID));
        warehouse.saveToDir(exportPath.getAbsolutePath());

    }

    @Override
    protected void onStop() {
        super.onStop();
        saveState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveState();
    }

    private void saveState() {
        WarehouseFactory warehouseFactory = WarehouseFactory.getInstance();
        File savedInstancePath = new File(getApplicationContext().getFilesDir(), "/warehousecontents.json");
        warehouseFactory.saveToDir(savedInstancePath.getAbsolutePath());
    }


}


