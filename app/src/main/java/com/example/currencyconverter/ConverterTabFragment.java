package com.example.currencyconverter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
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

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConverterTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConverterTabFragment extends Fragment {
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private EditText editTextFrom;
    private EditText editTextTo;
    private Converter mConverter;

    public ConverterTabFragment(Converter converter) {
        mConverter = converter;
    }

    // TODO: Rename and change types and number of parameters
    public static ConverterTabFragment newInstance(Converter converter) {
        ConverterTabFragment fragment = new ConverterTabFragment(converter);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_converter_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerFrom = view.findViewById(R.id.spinner_from);
        spinnerTo = view.findViewById(R.id.spinner_to);
        editTextFrom = view.findViewById(R.id.value_input_from);
        editTextTo = view.findViewById(R.id.value_input_to);

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
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        HashMap<String, Double> valueMap = mConverter.getValueMap();
        CharSequence[] charCodes = valueMap.keySet().toArray(new CharSequence[0]);
        ArrayAdapter<CharSequence> adapterFrom = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item, charCodes);
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapterFrom);
        spinnerTo.setAdapter(adapterFrom);
    }
}