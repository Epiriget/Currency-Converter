package com.example.currencyconverter;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ExchangeRateFetcher extends AsyncTask <Void, Void, String> {
    private static final String BANK_URL = "https://www.cbr-xml-daily.ru/daily_json.js";
    @Override
    protected String doInBackground(Void... voids) {
        String tmp = getRateInfo();
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
}
