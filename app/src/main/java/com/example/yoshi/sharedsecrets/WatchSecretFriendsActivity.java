package com.example.yoshi.sharedsecrets;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Orlando Aprea N97/226 on 04/07/2016.
 */

public class WatchSecretFriendsActivity extends SharedSecretsBasicActivity implements OnMapReadyCallback {
    private Thread onlineWork;
    private Toolbar toolbar;
    private FloatingActionButton ConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_secret);

        //parametri per distinguere la pagina attuale dalle altre pagine dell'app
        context=this;
        currentPageId = R.id.viewsecretfriends;

        //se la connessione è attiva parto con l'acquisizione delle coordinate dei componenti del gruppo selezionato
        onlineWork = new Thread() {
            public void run() {
                db.getLatLongOfGroup(grupposelezionato);
            }
        };
        if(!Utilities.isOnline()) {
            Utilities.NetworkNotWorking(context);
        }else if( grupposelezionato !=null) {
            onlineWork.start();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ConfirmButton = (FloatingActionButton) findViewById(R.id.fab);
        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!grupposelezionato.equals("")) {
                    Toast.makeText(context, "Selected Group: " + grupposelezionato, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context, "please select a group in the 'select group' page, " +
                            "if you don't have groups, then join or create one", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {
        try {onlineWork.join();} catch (InterruptedException e) {}
        double[][] coord = db.getLatLongOfGroup();
        if (coord != null) {
            for (int i = 0; i < coord.length; i++) {
                map.addMarker(new MarkerOptions().position(new LatLng(coord[i][0], coord[i][1])).title(""));
            }
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coord[0][0], coord[0][1]), 10));
        }
    }

}
