package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Converter converter;
    private static final int JOB_ID = 694;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        converter = new Converter(getApplicationContext());

        HashMap<String, Double> valueMap = converter.getValueMap();
        if(valueMap == null || valueMap.isEmpty()) {
            try {
                OutputStream outputStream = openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
                ExchangeRateFetcher asyncTask = new ExchangeRateFetcher(outputStream);
                try {
                    asyncTask.execute();
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Converter"));
        tabLayout.addTab(tabLayout.newTab().setText("List"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    OutputStream outputStream = openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
                    ExchangeRateFetcher asyncTask = new ExchangeRateFetcher(outputStream);
                    try {
                        asyncTask.execute();
                    }
                    catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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
        scheduleJob();
    }

    public void scheduleJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        ComponentName serviceName = new ComponentName
                (getPackageName(), DownloadJobService.class.getName());
        JobInfo.Builder jobBuilder = new JobInfo.Builder(JOB_ID, serviceName);

        //update every 12 hrs
        long period = 12 * 60 *  60 * 1000;
        jobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(period);
        JobInfo jobInfo = jobBuilder.build();
        scheduler.schedule(jobInfo);
    }


}
