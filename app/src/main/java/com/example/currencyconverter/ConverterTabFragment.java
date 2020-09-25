package com.example.currencyconverter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

public class ConverterTabFragment extends Fragment {
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private EditText editTextFrom;
    private EditText editTextTo;
    private TextView labelFrom;
    private TextView labelTo;
    private Converter mConverter;
    private String sharedPrefFile = "com.example.currencyconverter";
    private SharedPreferences mPreferences;


    public ConverterTabFragment() {
    }

    public static ConverterTabFragment newInstance() {
        ConverterTabFragment fragment = new ConverterTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = getContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        mConverter = new Converter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_converter_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerFrom = view.findViewById(R.id.spinner_from);
        spinnerTo = view.findViewById(R.id.spinner_to);
        editTextFrom = view.findViewById(R.id.value_input_from);
        editTextTo = view.findViewById(R.id.value_input_to);
        labelFrom = view.findViewById(R.id.currency_label_from);
        labelTo = view.findViewById(R.id.currency_label_to);



        HashMap<String, Double> valueMap = mConverter.getValueMap();
        if (valueMap != null) {
            CharSequence[] charCodes = valueMap.keySet().toArray(new CharSequence[0]);
            ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(view.getContext(),
                    android.R.layout.simple_spinner_item, charCodes);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerFrom.setAdapter(adapter);
            spinnerTo.setAdapter(adapter);
            if(savedInstanceState == null) {
                int usdPosition = adapter.getPosition("USD");
                int rubPosition = adapter.getPosition("RUB");
                spinnerFrom.setSelection(usdPosition);
                spinnerTo.setSelection(rubPosition);
            }
        }


        editTextFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    double value = Double.parseDouble(s.toString());
                    String codeFrom = spinnerFrom.getSelectedItem().toString();
                    String codeTo = spinnerTo.getSelectedItem().toString();
                    double nominal = mConverter.convert(codeFrom, codeTo);
                    double result = nominal * value;
                    editTextTo.setText(String.format("%.3f", result));
                }
                else {
                    editTextTo.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                labelTo.setText(mConverter.getFullName(parent.getItemAtPosition(position).toString()));
                editTextFrom.setText(editTextFrom.getText());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editTextFrom.setText(editTextFrom.getText());
                labelFrom.setText(mConverter.getFullName(parent.getItemAtPosition(position).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}