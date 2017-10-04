package com.example.yoshi.sharedsecrets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Orlando Aprea N97/226 on 04/07/2016.
 */
public class DatabaseOperation {
    //substitute with your server url
    private final String URLserver="http://orguilex.altervista.org/SharedSecret/index.php";
    String []Groups;
    double [][]coord;
    String Response;

    private String executeQuery(String link) throws IOException {
        URL url = new URL(link);
        URLConnection server = null;
        server = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        StringBuffer sb = new StringBuffer("");
        String line="";
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }
    public String[] getGroups(){
        return Groups;
    }
    public String getResponse(){return Response;}
    public void getGroups(String... arg0){

        String UserId = (String)arg0[0];
        String link = URLserver+"?Operation=getGroups&UserId="+UserId;
        try {
            this.Groups = executeQuery(link).split(",");
        }catch (MalformedURLException e1) {e1.printStackTrace();}
         catch (IOException e1) {e1.printStackTrace();}
    }
    public void insertUser(String... arg0){

        String UserId = (String)arg0[0];
        String Lat = (String)arg0[1];
        String Long = (String)arg0[2];
        String link = URLserver+"?Operation=InsertUser&UserId="+UserId+"&Lat="+Lat+"&Long="+Long;
        try {
            executeQuery(link);
        }catch (MalformedURLException e1) {e1.printStackTrace();}
        catch (IOException e1) {e1.printStackTrace();}
    }
    public void addUsertoGroup(String... arg0){

        String UserId = (String)arg0[0];
        String GroupName = (String)arg0[1];
        String Password = (String)arg0[2];
        GroupName=GroupName.replace(" ","%20");
        Password=Password.replace(" ","%20");
        String link = URLserver+"?Operation=AddUserToGroup&UserId="+UserId+"&GroupName="+GroupName+"&Password="+Password;

        try {
            Response=executeQuery(link);
        }catch (MalformedURLException e1) {e1.printStackTrace();}
        catch (IOException e1) {e1.printStackTrace();}
    }
    public void insertGroup(String... arg0){

        String GroupName = (String)arg0[0];
        String Password = (String)arg0[1];
        String Secret = (String)arg0[2];
        String FriendPercentage = (String)arg0[3];
        String UserId= (String)arg0[4];
        GroupName=GroupName.replace(" ","%20");
        Password=Password.replace(" ","%20");
        Secret=Secret.replace(" ","%20");
        String link = URLserver+"?Operation=InsertGroup&GroupName="+GroupName+"&Password="+Password+"&Secret="+Secret+"&Friends="+FriendPercentage+"&UserId="+UserId;
        try {
            Response=executeQuery(link);
        }catch (MalformedURLException e1) {e1.printStackTrace();}
        catch (IOException e1) {e1.printStackTrace();}
    }
    public void updateSecret(String... arg0){
        String GroupName = (String)arg0[0];
        String Secret = (String)arg0[1];
        String UserId = (String)arg0[2];
        GroupName=GroupName.replace(" ","%20");
        Secret=Secret.replace(" ","%20");
        String link = URLserver+"?Operation=UpdateSecret&GroupName="+GroupName+"&Secret="+Secret+"&UserId="+UserId;
        try {
            Response=executeQuery(link);
        }catch (MalformedURLException e1) {e1.printStackTrace();}
        catch (IOException e1) {e1.printStackTrace();}
    }
    public void getSecret (String...arg0){
        String GroupName = (String)arg0[0];
        String UserId = (String)arg0[1];
        GroupName=GroupName.replace(" ","%20");
        String link = URLserver+"?Operation=GetSecret&GroupName="+GroupName+"&UserId="+UserId;
        try {
            Response=executeQuery(link);
        }catch (MalformedURLException e1) {e1.printStackTrace();}
        catch (IOException e1) {e1.printStackTrace();}
    }
    public void updateUserPosition(String...arg0){
        String UserId = (String)arg0[0];
        String Latitude = (String)arg0[1];
        String Longitude = (String)arg0[2];
        String link = URLserver+"?Operation=UpdateUserPosition&UserId="+UserId+"&Lat="+Latitude+"&Long="+Longitude;
        try {
            executeQuery(link);
        }catch (MalformedURLException e1) {e1.printStackTrace();}
        catch (IOException e1) {e1.printStackTrace();}
    }
    public void getLatLongOfGroup(String...arg0){
        String Result;
        String []LatLong;
        String []Temp = new String[2];
        String GroupName = (String)arg0[0];
        GroupName = GroupName.replace(" ","%20");
        String link = URLserver+"?Operation=GetLatLongOfGroup&GroupName="+GroupName;
        try {
            Result = executeQuery(link);
            if(!Result.contains("ERROR")) {
                LatLong=Result.split("--");
                coord = new double[LatLong.length][2];
                for (int i = 0; i < LatLong.length; i++) {
                    Temp = LatLong[i].split(",,");
                    coord[i][0] = Double.parseDouble(Temp[0]);
                    coord[i][1] = Double.parseDouble(Temp[1]);
                }
            }

        }catch (MalformedURLException e1) {e1.printStackTrace();}
        catch (IOException e1) {e1.printStackTrace();}
    }

    public double[][] getLatLongOfGroup(){
        return coord;
    }
}