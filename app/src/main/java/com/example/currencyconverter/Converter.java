package com.example.currencyconverter;

import android.content.Context;

import java.io.FileInputStream;
import java.util.HashMap;

public class Converter {
    private Context mContext;
    private HashMap<String, Double> valueMap;
    private HashMap<String, String> fullNameMap;
    public Converter(Context context) {
        mContext = context;
        readFile();
    }

    private void readFile() {
        String exchangeRate;
        String filename = mContext.getString(R.string.file_name);
        try  (FileInputStream inputStream = mContext.openFileInput(filename)){
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            exchangeRate = new String(buffer);
            for (String str: exchangeRate.split("\n")) {
                String[] items = str.split("_");
                String charCode = items[0];
                String fullName = items[1];
                double value = Double.parseDouble(items[2]);
                valueMap.put(charCode, value);
                fullNameMap.put(charCode, fullName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double convert(String from, String to) {
        double fromValue = valueMap.get(from);
        double toValue = valueMap.get(to);
        return (fromValue / toValue);
    }

    public String getFullName(String charCode) {
        return fullNameMap.get(charCode);
    }
}