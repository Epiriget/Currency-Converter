package com.example.currencyconverter;

import android.app.Activity;
import android.app.ActivityOptions;
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
    private TextView labelFrom;
    private TextView labelTo;
    private Converter mConverter;
    private String sharedPrefFile = "com.example.currencyconverter";
    private SharedPreferences mPreferences;


    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("editTextFrom", editTextFrom.getText().toString());
        editor.putString("editTextTo", editTextTo.getText().toString());
        editor.putInt("spinnerTo", spinnerTo.getSelectedItemPosition());
        editor.putInt("spinnerFrom", spinnerFrom.getSelectedItemPosition());
        editor.putString("labelFrom", labelFrom.getText().toString());
        editor.putString("labelTo", labelTo.getText().toString());
        editor.apply();
    }


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
        mPreferences = getContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
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
        CharSequence[] charCodes = valueMap.keySet().toArray(new CharSequence[0]);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_spinner_item, charCodes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);


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
        if(savedInstanceState == null) {
            int usdPosition = adapter.getPosition("USD");
            int rubPosition = adapter.getPosition("RUB");
            spinnerFrom.setSelection(usdPosition);
            spinnerTo.setSelection(rubPosition);
            editTextFrom.setText(String.format("%.3f", 1.0));
        }
        if(mPreferences.contains("editTextFrom")) {
            editTextFrom.setText(mPreferences.getString("editTextFrom", ""));
            editTextTo.setText(mPreferences.getString("editTextTo", ""));
            labelFrom.setText(mPreferences.getString("labelFrom", ""));
            labelTo.setText(mPreferences.getString("labelTo", ""));
            spinnerTo.setSelection(mPreferences.getInt("spinnerTo", 0));
            spinnerFrom.setSelection(mPreferences.getInt("spinnerFrom", 0));
        }



    }
}