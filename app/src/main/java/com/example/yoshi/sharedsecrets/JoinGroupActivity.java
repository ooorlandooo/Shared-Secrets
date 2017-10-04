package com.example.yoshi.sharedsecrets;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Orlando Aprea N97/226 on 04/07/2016.
 */

public class JoinGroupActivity extends SharedSecretsBasicActivity {
    private Toolbar toolbar;
    private EditText GroupName;
    private EditText Password;
    private FloatingActionButton ConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        context=this;
        currentPageId = R.id.joingrouppage;

        //recupero gli elementi della pagina
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GroupName = (EditText) findViewById(R.id.groupName);
        Password = (EditText) findViewById(R.id.password);
        ConfirmButton = (FloatingActionButton) findViewById(R.id.fab);


        //setto le azioni da fare con i vari componenti
        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Utilities.isOnline()) {
                    Utilities.NetworkNotWorking(context);
                }
                else {
                    onlineWork = new Thread() {
                        public void run() {

                            db.addUsertoGroup(userId, GroupName.getText().toString().trim(), Password.getText().toString());
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
    }

}

