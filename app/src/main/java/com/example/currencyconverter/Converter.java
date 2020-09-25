package com.example.currencyconverter;

import android.content.Context;

import java.io.FileInputStream;
import java.util.HashMap;

public class Converter {
    private Context mContext;
    private HashMap<String, Double> valueMap = new HashMap<>();
    private HashMap<String, String> fullNameMap = new HashMap<>();
    public Converter(Context context) {
        mContext = context;
        updateExchangeRate();
    }

    public void updateExchangeRate() {
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
        updateExchangeRate();
        double fromValue = valueMap.get(from);
        double toValue = valueMap.get(to);
        return (fromValue / toValue);
    }

    public String getFullName(String charCode) {
        updateExchangeRate();
        return fullNameMap.get(charCode);
    }

    public HashMap<String, String> getFullNameMap() {
        updateExchangeRate();
        return fullNameMap;
    }

    public HashMap<String, Double> getValueMap() {
        updateExchangeRate();
        return valueMap;
    }
}