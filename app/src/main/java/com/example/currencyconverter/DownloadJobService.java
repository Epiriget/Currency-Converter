package com.example.currencyconverter;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.OutputStream;

public class DownloadJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Context context = getApplicationContext();
        OutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            ExchangeRateFetcher asyncTask = new ExchangeRateFetcher(outputStream);
            try {
                asyncTask.execute();
            }
            catch (Exception e) {
                Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        jobFinished(jobParameters, true);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
