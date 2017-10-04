package com.example.yoshi.sharedsecrets;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Orlando Aprea N97/226 on 04/07/2016.
 */

public class CreateGroupActivity extends SharedSecretsBasicActivity {

    private Toolbar toolbar;
    private FloatingActionButton ConfirmButton;
    private EditText GroupName;
    private EditText Password;
    private EditText Secret;
    private TextView PercentageLabel;
    private String Label;
    private SeekBar FriendsPercentage;
    private int Percentage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        //parametri per distinguere la pagina attuale dalle altre pagine dell'app
        context=this;
        currentPageId = R.id.creategrouppage;

        //recupero gli elementi della pagina
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GroupName = (EditText) findViewById(R.id.groupName);
        Password = (EditText) findViewById(R.id.password);
        Secret =  (EditText) findViewById(R.id.secretMessage);
        FriendsPercentage =(SeekBar)findViewById(R.id.friendsPercentage);
        PercentageLabel = (TextView)findViewById(R.id.percentageLabel);
        ConfirmButton = (FloatingActionButton) findViewById(R.id.fab);

        //inizializzo la barra di percentuale in basso nella pagina
        Label = PercentageLabel.getText().toString();
        PercentageLabel.setText(0+Label);

        //setto le azioni da fare con i vari componenti
        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Utilities.isOnline()) {
                    Utilities.NetworkNotWorking(context);
                }else{
                    onlineWork = new Thread() {
                        public void run() {
                            db.insertGroup(GroupName.getText().toString().trim(),
                                    Password.getText().toString(), Secret.getText().toString().trim(), Percentage + "", userId);
                        }
                    };
                    onlineWork.start();
                    try {
                        onlineWork.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context,  db.getResponse(), Toast.LENGTH_LONG).show();
                }
            }
        });

        FriendsPercentage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                Percentage = progress;
                String Display = Percentage+Label;
                PercentageLabel.setText(Display);
            }
        });
    }









}
