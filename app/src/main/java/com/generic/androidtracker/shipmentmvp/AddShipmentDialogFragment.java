package com.generic.androidtracker.shipmentmvp;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.generic.androidtracker.R;
import com.generic.androidtracker.interfaces.AddShipmentDialogListener;

public class AddShipmentDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {
    private EditText shipmentID;
    private EditText receiptDate;
    private EditText weight;
    private Spinner freightType;
    private Spinner weightUnit;
    private Button addButton;

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }

    public void onAddShipmentAction(){
        // Return input text back to activity through the implemented listener
        AddShipmentDialogListener listener = (AddShipmentDialogListener) getActivity();

        listener.onFinishEditDialog(shipmentID.getText().toString(),
                weight.getText().toString(),
                receiptDate.getText().toString(),
                freightType.getSelectedItem().toString(),
                weightUnit.getSelectedItem().toString());
        // Close the dialog and return back to the parent activity
        dismiss();
    }

    public AddShipmentDialogFragment(){}

    public static AddShipmentDialogFragment newInstance(String title){
        AddShipmentDialogFragment fragment = new AddShipmentDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_shipment_view, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        shipmentID = view.findViewById(R.id.shipment_id_input);
        receiptDate = view.findViewById(R.id.receipt_date_input);
        weight = view.findViewById(R.id.weight_input);
        addButton = view.findViewById(R.id.add_shipment_button);

        addButton.setOnClickListener(e -> {
            if (inputIsValid(shipmentID, receiptDate, weight)){
                onAddShipmentAction();
            }
        });

        freightType =  view.findViewById(R.id.freight_type_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> freightTypeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.freight_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        freightTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        freightType.setAdapter(freightTypeAdapter);

        weightUnit =  view.findViewById(R.id.weight_unit_input);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> weightUnitAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.weight_unit, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        weightUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        weightUnit.setAdapter(weightUnitAdapter);

        shipmentID.setOnEditorActionListener(this);
        receiptDate.setOnEditorActionListener(this);
        //freightStatus.setOnEditorActionListener(this);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        shipmentID.requestFocus();
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
            }else if (input.equals(receiptDate)){
                try {
                    Long.parseLong(inputString);
                }catch (Exception e){
                    allValid = false;
                    input.setError("Please enter a long value, i.e 1121131313");
                }
            }else if (input.equals(weight)){
                try {
                    Double.parseDouble(inputString);
                }catch (Exception e){
                    allValid = false;
                    input.setError("Please enter a double value, i.e 33.1, 22");
                }
            }
        }
        return allValid;
    }

}
