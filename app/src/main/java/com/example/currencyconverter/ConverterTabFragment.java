package com.example.currencyconverter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConverterTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConverterTabFragment extends Fragment {
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private MainActivity activity;
    private Context context;

    public ConverterTabFragment() {
        // Required empty public constructor
        activity = (MainActivity) getActivity();
        context = getContext();
//        spinnerFrom = activity.findViewById(R.id.spinner_from);
//        spinnerTo = activity.findViewById(R.id.spinner_to);
//        ArrayAdapter<CharSequence> adapterFrom = ArrayAdapter
//                .createFromResource(getContext(), R.array.currency_array,
//                        android.R.layout.simple_spinner_item);
//        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerFrom.setAdapter(adapterFrom);
//        spinnerTo.setAdapter(adapterFrom);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConverterTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConverterTabFragment newInstance(String param1, String param2) {
        ConverterTabFragment fragment = new ConverterTabFragment();
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
}