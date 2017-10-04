package com.example.yoshi.sharedsecrets;


import android.content.Context;
import android.widget.Toast;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Orlando Aprea N97/226 on 04/07/2016.
 */

public class Utilities {
    private static boolean result;

    public static void NetworkNotWorking(Context context){
        Toast.makeText(context, "the device is offline, please connect to internet", Toast.LENGTH_LONG).show();

    }

    public   static  boolean   isOnline() {

        try {
            final HttpURLConnection urlc = (HttpURLConnection)(new URL("http://www.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(10000);
            Thread t = new Thread(){public void run(){
                try {
                    urlc.connect();
                    result = urlc.getResponseCode() == 200;
                } catch (IOException e) {
                    e.printStackTrace();
                    result = false;
                }
            }};
            t.start();
            t.join();
            return result ;

        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
