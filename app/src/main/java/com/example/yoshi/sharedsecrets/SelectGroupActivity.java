package com.example.yoshi.sharedsecrets;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Orlando Aprea N97/226 on 04/07/2016.
 */

public class SelectGroupActivity extends SharedSecretsBasicActivity{
    private String[]Groups;
    private  Toolbar toolbar;
    private EditText ModifySecret;
    private TextView ViewSecret;
    private FloatingActionButton ViewButton;
    private FloatingActionButton ModifyButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_group);
        //parametri per distinguere la pagina attuale dalle altre pagine dell'app
        context=this;
        currentPageId = R.id.selgrouppage;

        //recupero i gruppi creati precedentemente usando l'app
        onlineWork = new Thread() {
            public void run() {
                db.getGroups(userId);
            }
        };
        if(Utilities.isOnline()) {
            onlineWork.start();
        }
        //recupero gli elementi della pagina
        final Spinner GroupList = (Spinner) findViewById(R.id.grouplist);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ModifySecret = (EditText) findViewById(R.id.modifySecret);
        ViewSecret = (TextView) findViewById(R.id.viewSecret);
        ModifyButton = (FloatingActionButton) findViewById(R.id.modifyButton);
        ViewButton = (FloatingActionButton) findViewById(R.id.viewButton);


        try {onlineWork.join();} catch (InterruptedException e) {}
        //se dopo aver recuperato i gruppi su server non ci sono gruppi disponibili allora viene creata una lista vuota
        Groups = db.getGroups();
        if(Groups== null) {
            Groups = new String[1];
            Groups[0]="";
        }
        final ArrayAdapter<String> List = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Groups);
        GroupList.setAdapter(List);

        //setto le azioni da fare con i vari componenti
        GroupList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!Utilities.isOnline()) {
                    Utilities.NetworkNotWorking(context);
                } else{
                    grupposelezionato = GroupList.getSelectedItem().toString().trim();
                    Toast.makeText(context,  "Selected Group: " + grupposelezionato, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ModifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Utilities.isOnline()) {
                    Utilities.NetworkNotWorking(context);
                }else {

                    if (grupposelezionato != null && !grupposelezionato.equals("")) {
                        onlineWork = new Thread() {
                            public void run() {
                                db.updateSecret(grupposelezionato, ModifySecret.getText().toString().trim(),userId);
                            }
                        };
                        onlineWork.start();
                        try {
                            onlineWork.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(context,  db.getResponse(), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(context,  "Response: Select a Group from the list above and confirm", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        ViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Utilities.isOnline()) {
                    Utilities.NetworkNotWorking(context);
                }else{

                    if (grupposelezionato != null && !grupposelezionato.equals("")) {
                        onlineWork = new Thread() {
                            public void run() {
                                db.getSecret(grupposelezionato,userId);
                            }
                        };
                        onlineWork.start();
                        try {
                            onlineWork.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(context, "Secret:\n"+db.getResponse(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "ERROR: Select a Group from the list above and confirm", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

}
