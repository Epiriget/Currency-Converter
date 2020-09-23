package com.example.currencyconverter;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class DownloadJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //todo: implement async download
        jobFinished(jobParameters, true);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
