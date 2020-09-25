package com.example.currencyconverter;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class ExchangeRateFetcher extends AsyncTask<Void, Void, String> {
    private static final String BANK_URL = "https://www.cbr-xml-daily.ru/daily_json.js";
    private OutputStream mOutputStream;
    public ExchangeRateFetcher(OutputStream outputStream) {
        mOutputStream = outputStream;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return getRateInfo();
    }

    private String getRateInfo() {
        HttpsURLConnection urlConnection = null;
        BufferedReader reader = null;
        String rateJSONString = null;

        try {
            URL requestURL = new URL(BANK_URL);

            urlConnection = (HttpsURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            if (builder.length() == 0) {
                return null;
            }
            rateJSONString = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return rateJSONString;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        StringBuilder output = new StringBuilder();
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject valuteObject = jsonObject.getJSONObject("Valute");
            String charCode, nominal, value, name;

            for (Iterator<String> it = valuteObject.keys(); it.hasNext(); ) {
                String key = it.next();
                JSONObject item = valuteObject.getJSONObject(key);
                charCode = item.getString("CharCode");
                nominal = item.getString("Nominal");
                value = item.getString("Value");
                name = item.getString("Name");

                int intNominal = Integer.parseInt(nominal);
                double valuePerNote = Double.parseDouble(value) / intNominal;

                output.append(String.format("%s_%s_%f\n", charCode, name, valuePerNote));
            }
            output.append(String.format("%s_%s_%f\n", "RUB", "Российский рубль", 1.0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            mOutputStream.write(output.toString().getBytes());
            mOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Exchange Rate Fetcher", "Update was complete");
    }
}
