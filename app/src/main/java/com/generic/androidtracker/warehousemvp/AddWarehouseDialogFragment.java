package com.generic.androidtracker.warehousemvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.generic.androidtracker.R;
import com.generic.androidtracker.interfaces.AddWarehouseDialogListener;

/**
 * Custom Dialog Fragment for adding a warehouse.
 */
public class AddWarehouseDialogFragment extends DialogFragment  {
    private EditText warehouseID;
    private EditText warehouseName;
    private Spinner freightStatus;
    private Button addButton;

    public void onAddWarehouseAction(){
        // Return input text back to activity through the implemented listener
        AddWarehouseDialogListener listener = (AddWarehouseDialogListener) getActivity();
        listener.onFinishEditDialog(warehouseID.getText().toString(),
                warehouseName.getText().toString(),
                freightStatus.getSelectedItem().toString());
        // Close the dialog and return back to the parent activity
        dismiss();
    }

    public AddWarehouseDialogFragment(){}

    public static AddWarehouseDialogFragment newInstance(String title){
        AddWarehouseDialogFragment fragment = new AddWarehouseDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_warehouse_view, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        warehouseID = view.findViewById(R.id.warehouse_id_input);
        warehouseName = view.findViewById(R.id.warehouse_name_input);
        addButton = view.findViewById(R.id.add_warehouse_button);

        addButton.setOnClickListener(e ->{
            if (inputIsValid(warehouseID, warehouseName)){
                onAddWarehouseAction();
            }
        });

        freightStatus =  view.findViewById(R.id.freight_receipt_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.freight_status, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        freightStatus.setAdapter(adapter);

        //freightStatus.setOnEditorActionListener(this);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        warehouseID.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /**
     * Validates user input.
     * @param inputs variable length EditText inputs.
     * @return true, if input is valid.
     */
    public boolean inputIsValid(EditText ...inputs){
        boolean allValid = true;
        for (EditText input : inputs){
            String inputString = input.getText().toString().trim();
            if (inputString.trim().length() == 0){
                input.setError("Field is empty");
                allValid = false;
            }else if (input.equals(warehouseID)){
                try {
                    Integer.parseInt(inputString);
                }catch (Exception e){
                    allValid = false;
                    input.setError("Please enter an integer value, i.e 121, 1");
                }
            }
        }
        return allValid;
    }
}
