package com.example.yoshi.sharedsecrets;

import android.content.Context;
import android.content.Intent;
import java.util.LinkedList;

/**
 * Created by Orlando Aprea N97/226 on 04/07/2016.
 */

public class SwitchingPageHandler {
    private int id;
    private LinkedList<String> DataName = new LinkedList<String>();
    private LinkedList<String> Data = new LinkedList<String>();
    private Intent NewPage;

    public void setId(int x){
        id=x;
    }

    public void setData(LinkedList<String> Name,LinkedList<String> d ){
        DataName = Name;
        Data=d;
    }

    public  boolean openNewPage(Context c, int ActualCLassId){
        //scelgo quale activity caricare in base al click dell utente
        if(id == ActualCLassId)
            return false;
        if (id == R.id.viewsecretfriends) {
            newPage(DataName,Data,c , WatchSecretFriendsActivity.class);
            return true;
        }else if(id == R.id.joingrouppage){
            newPage(DataName,Data,c , JoinGroupActivity.class);
            return true;
        }else if(id == R.id.creategrouppage){
            newPage(DataName,Data,c , CreateGroupActivity.class);
            return true;
        }else if(id == R.id.selgrouppage){
            newPage(DataName,Data,c , SelectGroupActivity.class);
            return true;
        }
        return false;
    }

    public Intent getIntent(){
        return NewPage;
    }

    private void newPage(LinkedList<String> DName, LinkedList<String> DValue, Context context , Class c) {

        NewPage = new Intent(context, c);
        for(int i = 0; i<=DName.size();i++) {
            NewPage.putExtra(DName.removeFirst(), DValue.removeFirst());
        }
    }
}
