package com.example.yoshi.sharedsecrets;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Orlando Aprea N97/226 on 04/07/2016.
 */
public class LocatorService extends IntentService {
    private DatabaseOperation db = new DatabaseOperation();
    private int i = 0;

    public LocatorService() {
        super("Locator Service");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String userId = workIntent.getExtras().getString("userId");
        String lat = workIntent.getExtras().getString("lat");
        String longi = workIntent.getExtras().getString("long");
        db.updateUserPosition(userId, lat, longi);
    }
}