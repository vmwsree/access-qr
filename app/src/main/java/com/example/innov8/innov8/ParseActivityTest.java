package com.example.innov8.innov8;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by horous on 11/01/16.
 */
public class ParseActivityTest extends Application{
    Intent returnIntent;
    Boolean ret= false;
    String uid;
    Date endDate,startDate;
    long diffInMin;
    boolean logout=false;
    public String response;
    public static final String PREFS_NAME = "Innov8";
    ParseObject UserObjectGlobal,UserObjectLogoutGlobal;


public ParseActivityTest(){

    Parse.enableLocalDatastore(getApplicationContext());
    Parse.initialize(this);
}
    public int compare(Number a, Number b){
        return new BigDecimal(a.toString()).compareTo(new BigDecimal(b.toString()));
    }

    public String logoutUser(String  uidp)
    {

        ParseObject UserObject = ParseObject.createWithoutData("_User", uidp);
        diffInMin=0;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserTracker");
        query.whereEqualTo("user", UserObject);
        query.addDescendingOrder("createdAt");
        query.fromLocalDatastore();
        UserObjectLogoutGlobal = UserObject;
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {

                if (e == null) {
                    try{
                        ParseObject ob = scoreList.get(0);

                        ob.put("time_exit", new Date());
                        startDate =ob.getDate("time_entry");
                        endDate = new Date();
                        long diffInMs = endDate.getTime() - startDate.getTime();
                        diffInMin = (TimeUnit.MILLISECONDS.toSeconds(diffInMs))/60;
                        ob.put("time_spent", diffInMin);
                        ob.saveInBackground();
                        Log.d("jj", "Min" + Long.toString(diffInMs));
                        Log.d("jj", "Micro Sec"+Long.toString(diffInMin));
                        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("UserPlan");
                        query2.whereEqualTo("user", UserObjectLogoutGlobal);
                        Log.d("jj", "insider");
                        query2.addDescendingOrder("createdAt");
                        query2.fromLocalDatastore();
                        query2.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> scoreList, ParseException e) {

                                if (e == null) {
                                    Log.d("jj", "inside");
                                    try {
                                        ParseObject ob2 = scoreList.get(0);
                                        long minNow = ob2.getInt("minutes_spent");
                                        diffInMin = diffInMin + minNow;
                                        ob2.put("minutes_spent", diffInMin);
                                        ob2.saveInBackground();
                                        Log.d("jj", "saved");
                                        response= "USER_SUCCESS_LOGOUT";

                                        Log.d("jj", "success last");

                                    } catch (Exception ea) {

                                        response=  "USER_FAILED";


                                    }


                                    //Date endDate= ob.getDate("");


                                } else {

                                    Log.d("jj", "Error: " + e.getMessage());
                                }
                            }
                        });
                    }
                    catch (Exception eb ){

                        //return "USER_FAILED";


                    }
                }
                else {

                    Log.d("jj", "Error: " + e.getMessage());
                }

            }
        });
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("UserPlan");
        query2.whereEqualTo("user", UserObject);
        Log.d("jj", "insider");

        query2.addDescendingOrder("createdAt");
        query2.fromLocalDatastore();
        query2.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {

                if (e == null) {
                    Log.d("jj", "inside");
                    try{
                        ParseObject ob2=scoreList.get(0);
                        long minNow =ob2.getInt("minutes_spent");
                        diffInMin = diffInMin+minNow;
                        ob2.put("minutes_spent", diffInMin);
                        ob2.saveInBackground();
                        Log.d("jj", "saved");
                        //return "USER_SUCCESS_LOGOUT";

                        Log.d("jj", "success last");

                    }
                    catch (Exception ea ){

                        //return "USER_FAILED";


                    }

                    //Date endDate= ob.getDate("");



                } else {

                    Log.d("jj", "Error: " + e.getMessage());
                }
            }
        });

        return ret.toString();


    }
    public String checkPermission(String uidp){

        ParseObject UserObject = ParseObject.createWithoutData("_User", uidp);

        try{
            ParseQuery<ParseObject> query = ParseQuery.getQuery("UserPlan");
            query.addDescendingOrder("createdAt");
            query.fromLocalDatastore();
            query.whereEqualTo("user", UserObject);
            UserObjectGlobal=UserObject;

            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> scoreList, ParseException e) {
                    if ((e == null) && (scoreList != null) ) {

                        Log.d("jj", "got obj: " + scoreList.toString());
                        if(scoreList.size()>0)
                        {

                            ParseObject ob = scoreList.get(0);
                            if ((ob.getInt("total_minutes") > ob.getInt("minutes_spent"))) {
                                //Toast.makeText(, "Welcome User " , Toast.LENGTH_LONG).show();
                                Log.d("score", "true" + ob.getInt("total_minutes") + ob.getInt("minutes_spent"));
                                ret = true;
                                ParseObject userTrackerObject = new ParseObject("UserTracker");
                                userTrackerObject.put("time_entry", new Date());
                                userTrackerObject.put("tag", "TEST");
                                userTrackerObject.put("user", UserObjectGlobal);

                                userTrackerObject.saveInBackground(new SaveCallback() {
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Log.d("jj", "success");
                                        } else {
                                            Log.d("jj", "error");
                                            Log.e("jj", "exception: " + e.getMessage());
                                            Log.e("jj", "exception: " + e.toString());
                                        }
                                    }
                                });
                                //return "USER_SUCCESS";

                                Log.d("jj", "success last");

                            } else {

                                //return "USER_FAIL";

                                Log.d("jj", "failed last_call in check permission Time Exceeded");

                                Log.d("jj", "got false:");
                                ret = false;
                            }


                        }
                        else{

                            //return "USER_FAIL";


                            ret=false;
                            Log.d("score", "Error: " + e.getMessage());
                        }
                    }

                    else {

                        //return "USER_FAIL";

                        Log.d("jj", "failed last_call in check permission Plan not availble" + e.toString());

                        ret=false;
                        Log.d("score", "Error: " + e.getMessage());
                    }
                }
            });
//    ParseObject userTrackerObject = new ParseObject("UserTracker");
//    userTrackerObject.put("time_entry", new Date());
//    userTrackerObject.put("tag", "TEST");
//    userTrackerObject.put("user", UserObject);
//
//    userTrackerObject.saveInBackground(new SaveCallback() {
//        public void done(ParseException e) {
//            if (e == null) {
//                Log.d("jj", "success");
//            } else {
//                Log.d("jj", "error");
//                Log.e("jj", "exception: " + e.getMessage());
//                Log.e("jj", "exception: " + e.toString());
//            }
//        }
//    });
//    returnIntent.putExtra("result", "USER_SUCCESS");
//
//    Log.d("jj", "success last");
//    setResult(Activity.RESULT_OK, returnIntent);
//    finish();
        }
        catch (Exception ec ){

            Log.d("jj", "fail last");
           // return "USER_FAILED";



        }
        return ret.toString();

    }


}
