package com.example.currencyconverter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ListTabFragment extends Fragment {
    private ListView listView;
    private Converter mConverter;
    public ListTabFragment() {
        // Required empty public constructor
    }

    public static ListTabFragment newInstance() {
        return new ListTabFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConverter = new Converter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.list_view);
        HashMap<String, Double> valueMap = mConverter.getValueMap();
        HashMap<String, String> nameMap = mConverter.getFullNameMap();

        if (valueMap != null) {
            String[] charCodes = valueMap.keySet().toArray(new String[0]);
            List<ListItem> listItems = new ArrayList<>();
            for (String s: charCodes) {
                double dbl = valueMap.get(s);
                listItems.add(new ListItem(s, nameMap.get(s), valueMap.get(s)));
            }
            ListView listView = view.findViewById(R.id.list_view);
            ListItemAdapter adapter = new ListItemAdapter
                    (view.getContext(), R.layout.list_item, listItems);
            listView.setAdapter(adapter);
        }
    }
}