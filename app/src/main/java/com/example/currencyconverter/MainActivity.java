package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private Converter converter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        converter = new Converter(getApplicationContext());
        if(converter.getValueMap() == null) {
            new ExchangeRateFetcher().execute();
        }

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Converter"));
        tabLayout.addTab(tabLayout.newTab().setText("List"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount(), converter);
        viewPager.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ExchangeRateFetcher().execute();
            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public class ExchangeRateFetcher extends AsyncTask<Void, Void, String> {
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

        @Override
        protected void onPostExecute(String s) {
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


            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
                outputStream.write(output.toString().getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }



            super.onPostExecute(s);
        }
    }
}
