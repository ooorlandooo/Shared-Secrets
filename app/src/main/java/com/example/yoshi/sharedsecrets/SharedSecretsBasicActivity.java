package com.example.yoshi.sharedsecrets;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.LinkedList;


/**
 * Created by Orlando Aprea N97/226 on 03/07/2016.
 */

public class SharedSecretsBasicActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleApiClient client;
    private SwitchingPageHandler SwitchHandler = new SwitchingPageHandler();
    private SharedPreferences userDetails;
    private Location loc;
    protected Intent UserLocationUpdates;
    protected String grupposelezionato = null;
    protected String userId;
    protected Context context;
    protected int currentPageId;
    protected DatabaseOperation db = new DatabaseOperation();
    protected Thread onlineWork;


    private void showGPSDisabledAlertToUser(){
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!statusOfGPS) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Goto Settings Page To Enable GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showGPSDisabledAlertToUser();
        client = StartLocator();

    }

    @Override
    public void onStop() {
        super.onStop();
        client.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context=this;
        //creo il service x la geolocalizzazione
        UserLocationUpdates = new Intent(context, LocatorService.class);

        //recupero dati
        userId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if (getIntent().hasExtra("gs") && getIntent().getExtras().getString("gs")!=null && !getIntent().getExtras().getString("gs").equals(""))
            grupposelezionato = getIntent().getExtras().getString("gs");
        else
            grupposelezionato = "";

        //creazione di un profilo utente online la prima volta che viene usata sul telefono l'app
        userDetails = getSharedPreferences("userdetails", MODE_PRIVATE);
        if (!userDetails.getString("firstTimeAccessing", "").equals("no")) {
            SharedPreferences.Editor edit = userDetails.edit();
            edit.clear();
            if (Utilities.isOnline()) {
                edit.putString("firstTimeAccessing", "no");
                edit.apply();
                onlineWork = new Thread() {
                    public void run() {
                        //coordinate casuali che inizializzano il profilo utente al primo accesso mai effettuato in assoluto
                        db.insertUser(userId, "0", "0");
                    }
                };
                onlineWork.start();
                try {
                    onlineWork.join();
                } catch (InterruptedException e) {
                }
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_group, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //setto i parametri da passare tra gli activity
        LinkedList<String> nomeDati = new LinkedList<String>();
        LinkedList<String> dati = new LinkedList<String>();
        nomeDati.addFirst("gs");
        dati.addFirst(grupposelezionato);

        //setto il gestore per il cambio pagina
        SwitchHandler.setId(item.getItemId());
        SwitchHandler.setData(nomeDati,dati);
        if(SwitchHandler.openNewPage(context,currentPageId)) {
            startActivity(SwitchHandler.getIntent());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        }

        if(!(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) ) {
            return;
        }
            LocationRequest LocRequest;
        LocRequest = LocationRequest.create();
        LocRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocRequest.setInterval(7000); // Update location every  7 second
        LocRequest.setFastestInterval(2000);
        LocationServices.FusedLocationApi.requestLocationUpdates(client, LocRequest, this);
        loc = LocationServices.FusedLocationApi.getLastLocation(client);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(final Location location) {
        if(userId!=null) {
            UserLocationUpdates.putExtra("userId", userId);
            UserLocationUpdates.putExtra("lat", String.valueOf(location.getLatitude()));
            UserLocationUpdates.putExtra("long", String.valueOf(location.getLongitude()));
            this.startService(UserLocationUpdates);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        client.connect();
    }

    protected GoogleApiClient StartLocator(){
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();
        return client;
    }
}
