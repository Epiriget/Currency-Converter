package com.example.currencyconverter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListItemAdapter extends ArrayAdapter<ListItem> {
    private LayoutInflater inflater;
    private int layout;
    private List<ListItem> itemList;

    public ListItemAdapter(@NonNull Context context, int resource, @NonNull List<ListItem> objects) {
        super(context, resource, objects);
        itemList = objects;
        layout = resource;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(this.layout, parent, false);

        TextView charCodeView = view.findViewById(R.id.list_item_charcode);
        TextView fullNameView = view.findViewById(R.id.list_item_name);
        TextView valueView = view.findViewById(R.id.list_item_value);

        ListItem listItem = itemList.get(position);

        charCodeView.setText(listItem.getCharCode());
        fullNameView.setText(listItem.getFullName());
        String dbl = String.valueOf(listItem.getValue());
        valueView.setText(dbl);
        return view;
    }
}
