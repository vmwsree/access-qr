package com.example.innov8.innov8;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.parse.FindCallback;
import com.parse.GetCallback;
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
public class ParseActivity extends AppCompatActivity{
     Intent returnIntent;
    Boolean ret= false;
     Boolean condition =true;
    String uid;
     Date endDate,startDate;
    long diffInMin;
    boolean logout=false;
    public static final String PREFS_NAME = "Innov8";
    ParseObject UserObjectGlobal,UserObjectLogoutGlobal;



public int compare(Number a, Number b){
        return new BigDecimal(a.toString()).compareTo(new BigDecimal(b.toString()));
    }

public boolean logoutUser(ParseObject UserObject)
{
    ParseQuery<ParseObject> queryw = ParseQuery.getQuery("UserTracker");

    //ParseObject.pinAllInBackground(query);

    queryw.whereEqualTo("user", UserObject);
    queryw.orderByDescending("createdAt");
    queryw.fromLocalDatastore();
    UserObjectLogoutGlobal = UserObject;
    queryw.findInBackground(new FindCallback<ParseObject>() {
        public void done(List<ParseObject> scoreList, ParseException e) {

            ParseObject.pinAllInBackground(scoreList);

            if (e == null) {
                try {
                    ParseObject ob = scoreList.get(0);
                    Log.d("jj", "Inside first login re check: " );
                    //Log.d("jj",ob.get("time_exit").toString());
                    if (ob.get("time_exit")!=null) {
                        // Log.d("jj",ob.get("time_exit").toString());
                        Toast.makeText(getApplicationContext(), "You have forgotten to login last time Please contact reception ", Toast.LENGTH_LONG).show();
                        condition=false;
                        returnIntent.putExtra("result", "USER_FAIL");
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    }
                    else {
                        Log.d("jj"," login");
                    }

                } catch (Exception es) {
                    Log.d("jj", "Error: " + es.getMessage());
                }
            }


            ///



            if(condition) {
                diffInMin=0;
                ParseQuery<ParseObject> query = ParseQuery.getQuery("UserTracker");

                //ParseObject.pinAllInBackground(query);

                query.whereEqualTo("user", UserObjectLogoutGlobal);
                query.addDescendingOrder("createdAt");
                query.fromLocalDatastore();
               // UserObjectLogoutGlobal = UserObject;
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> scoreList, ParseException e) {

                        ParseObject.pinAllInBackground(scoreList);

                        if (e == null) {
                            try {
                                ParseObject ob = scoreList.get(0);

                                ob.put("time_exit", new Date());
                                startDate = ob.getDate("time_entry");
                                endDate = new Date();
                                long diffInMs = endDate.getTime() - startDate.getTime();
                                diffInMin = (TimeUnit.MILLISECONDS.toSeconds(diffInMs)) / 60;
                                ob.put("time_spent", diffInMin);
                                ob.saveInBackground();
                                Log.d("jj", "Min" + Long.toString(diffInMs));
                                Log.d("jj", "Micro Sec" + Long.toString(diffInMin));
                                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("UserPlan");
                                query2.whereEqualTo("user", UserObjectLogoutGlobal);
                                Log.d("jj", "insider1");
                                query2.addDescendingOrder("createdAt");
                                query2.fromLocalDatastore();
                                query2.findInBackground(new FindCallback<ParseObject>() {
                                    public void done(List<ParseObject> scoreList, ParseException e) {

                                        if (e == null) {
                                            Log.d("jj", "inside1");


                                            try {
                                                ParseObject.pinAllInBackground(scoreList);

                                                ParseObject ob2 = scoreList.get(0);
                                                int minNow = ob2.getInt("minutes_spent");
                                                Log.d("jj", "Micro Sec" + Long.toString(diffInMin));
                                                Log.d("jj", "Micro " + Integer.toString(minNow));
                                                int temp =  ((int)diffInMin )+ minNow;
                                                Log.d("jj", "final  " + Integer.toString(temp));
                                                ob2.put("minutes_spent", temp);
                                                Log.d("jj", Long.toString(diffInMin));
                                                ob2.saveInBackground();
                                                Log.d("jj", "saved");
                                                returnIntent.putExtra("result", "USER_SUCCESS_LOGOUT");

                                                Log.d("jj", "success last1");
                                                setResult(Activity.RESULT_OK, returnIntent);
                                                finish();
                                            } catch (Exception ea) {
                                                Log.d("jj", "Error: " + ea.getMessage());
                                                returnIntent.putExtra("result", "USER_FAIL");

                                                setResult(Activity.RESULT_OK, returnIntent);
                                                finish();

                                            }

                                            //Date endDate= ob.getDate("");


                                        } else {

                                            Log.d("jj", "Error: " + e.getMessage());
                                        }
                                    }
                                });
                            } catch (Exception eb) {

                                returnIntent.putExtra("result", "USER_FAIL");

                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();

                            }
                        } else {

                            Log.d("jj", "Error: " + e.getMessage());
                        }
                    }
                });
//                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("UserPlan");
//                query2.whereEqualTo("user", UserObjectLogoutGlobal);
//                Log.d("jj", "insider");
//
//                query2.addDescendingOrder("createdAt");
//                query2.fromLocalDatastore();
//                query2.findInBackground(new FindCallback<ParseObject>() {
//                    public void done(List<ParseObject> scoreList, ParseException e) {
//
//                        if (e == null) {
//                            Log.d("jj", "inside");
//                            try {
//                                ParseObject.pinAllInBackground(scoreList);
//
//                                ParseObject ob2 = scoreList.get(0);
//                                long minNow = ob2.getInt("minutes_spent");
//                                diffInMin = diffInMin + minNow;
//                                ob2.put("minutes_spent", diffInMin);
//                                ob2.saveEventually();
//                                Log.d("jj", "saved");
//                                returnIntent.putExtra("result", "USER_SUCCESS_LOGOUT");
//
//                                Log.d("jj", "success last2");
//                                setResult(Activity.RESULT_OK, returnIntent);
//                                finish();
//                            } catch (Exception ea) {
//                                Log.e("jj", "exception", ea);
//                                returnIntent.putExtra("result", "USER_FAIL");
//
//                                setResult(Activity.RESULT_OK, returnIntent);
//                                finish();
//
//                            }
//
//                            //Date endDate= ob.getDate("");
//
//
//                        } else {
//
//                            Log.d("jj", "Error: " + e.getMessage());
//                        }
//                    }
//                });
condition=true;
            }

            ///
        }
    });


    return ret;


}

public Boolean checkPermission(ParseObject UserObject) {
    ParseQuery<ParseObject> queryw = ParseQuery.getQuery("UserTracker");

    //ParseObject.pinAllInBackground(query);
    condition=true;
    
    queryw.whereEqualTo("user", UserObject);
    queryw.orderByDescending("updatedAt");
    queryw.fromLocalDatastore();
    UserObjectGlobal = UserObject;


                queryw.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> scoreList, ParseException e) {
                        if ((e == null) && (scoreList != null)) {
                        if (scoreList.size() > 0) {

                            ParseObject.pinAllInBackground(scoreList);

                            try {
                                ParseObject ob = scoreList.get(0);
                                Log.d("jj", "Inside first login re check: ");
                                //Log.d("jj",ob.get("time_exit").toString());
                                if (ob.get("time_exit") == null) {
                                    // Log.d("jj",ob.get("time_exit").toString());
                                    Toast.makeText(getApplicationContext(), "You have forgotten to logout last time Please contact reception ", Toast.LENGTH_LONG).show();
                                    returnIntent.putExtra("result", "USER_FAIL");
                                    condition = false;
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();

                                } else {
                                    Log.d("jj", ob.get("time_exit").toString());
                                }


                            } catch (Exception es) {
                                Log.d("jj", "Error: " + es.getMessage());
                            }
                            // got the most recently modified object... do something with it here
                        }
                    }
//        }
//    });
//    queryw.findInBackground(new FindCallback<ParseObject>() {
//        public void done(List<ParseObject> scoreList, ParseException e) {
//
//            ParseObject.pinAllInBackground(scoreList);
//
//            if (e == null) {
//                try {
//                    ParseObject ob = scoreList.get(0);
//                    Log.d("jj", "Inside first login re check: ");
//                    //Log.d("jj",ob.get("time_exit").toString());
//                    if (ob.get("time_exit") == null) {
//                        // Log.d("jj",ob.get("time_exit").toString());
//                        Toast.makeText(getApplicationContext(), "You have forgotten to logout last time Please contact reception ", Toast.LENGTH_LONG).show();
//                        returnIntent.putExtra("result", "USER_FAILED");
//                        condition = false;
//                        setResult(Activity.RESULT_OK, returnIntent);
//                        finish();
//
//                    } else {
//                        Log.d("jj", ob.get("time_exit").toString());
//                    }
//
//
//                } catch (Exception es) {
//                    Log.d("jj", "Error: " + es.getMessage());
//                }
//            }

                        if (condition) {

                            ///
                            try {
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("UserPlan");
                                query.orderByDescending("createdAt");
                                query.fromLocalDatastore();
                                query.whereEqualTo("user", UserObjectGlobal);


                                query.findInBackground(new FindCallback<ParseObject>() {
                                    public void done(List<ParseObject> scoreList, ParseException e) {
                                        if ((e == null) && (scoreList != null)) {

                                            Log.d("jj", "got obj: " + scoreList.toString());
                                            if (scoreList.size() > 0) {
                                                ParseObject.pinAllInBackground(scoreList);
                                                ParseObject ob = scoreList.get(0);
                                                if ((ob.getInt("total_minutes") > ob.getInt("minutes_spent"))) {
                                                    Toast.makeText(getApplicationContext(), "Welcome User ", Toast.LENGTH_LONG).show();
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
                                                    userTrackerObject.pinInBackground();
                                                    returnIntent.putExtra("result", "USER_SUCCESS");
                                                    //setContentView(R.layout.successpage);
                                                    Log.d("jj", "success last");
                                                    setResult(Activity.RESULT_OK, returnIntent);
                                                    finish();

                                                } else {

                                                    returnIntent.putExtra("result", "USER_FAIL");
                                                    //setContentView(R.layout.failpage);
                                                    Log.d("jj", "failed last_call in check permission Time Exceeded");
                                                    Log.d("jj", String.valueOf(ob.getInt("total_minutes")) + String.valueOf(ob.getInt("minutes_spent")));
                                                    setResult(Activity.RESULT_OK, returnIntent);
                                                    finish();
                                                    Log.d("jj", "got false:");
                                                    ret = false;
                                                }


                                            } else {

                                                returnIntent.putExtra("result", "USER_FAIL");
                                                //setContentView(R.layout.failpage);
                                                Log.d("jj", "failed last_call in check permission Plan not availble");
                                                setResult(Activity.RESULT_OK, returnIntent);
                                                finish();
                                                ret = false;
                                                // Log.d("score", "Error: " + e.getMessage());
                                            }
                                        } else {

                                            returnIntent.putExtra("result", "USER_FAIL");
                                            //setContentView(R.layout.failpage);
                                            Log.d("jj", "failed last_call in check permission Plan not availble" + e.toString());
                                            setResult(Activity.RESULT_OK, returnIntent);
                                            finish();
                                            ret = false;
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
                            } catch (Exception ec) {

                                Log.d("jj", "fail last");
                                returnIntent.putExtra("result", "USER_FAIL");
                                // setContentView(R.layout.failpage);
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();

                            }
                            condition = true;
                        }
                        ////


                    }
                });

                return ret;

            }

            @Override
            protected void onResume () {
                super.onResume();
            }

            private ViewFlipper mViewFlipper;
            @Override
            protected void onCreate (Bundle savedInstanceState){
                super.onCreate(savedInstanceState);
                Boolean stat;
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                logout = settings.getBoolean("logoutMode", false);

                setContentView(R.layout.logout);
                mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper2);



                //Parse.enableLocalDatastore(this);


                returnIntent = getIntent();
                uid = returnIntent.getStringExtra("QR-Code");

                ParseObject UserObject = ParseObject.createWithoutData("_User", uid);

                // UserObject.pinInBackground();
//    UserObject.fetchFromLocalDatastoreInBackground(new GetCallback<ParseObject>() {
//        public void done(ParseObject object, ParseException e) {
//            if (e == null) {
//                // object will be your game score
//            } else {
//                // something went wrong
//            }
//        }
//    });


                if (logout) {
                    mViewFlipper.setDisplayedChild(5);
                    logoutUser(UserObject);

                } else {mViewFlipper.setDisplayedChild(0);
                    stat = checkPermission(UserObject);
                    if (stat) {
                        Log.d("jj", "inside ");
                    } else {
                        Log.d("jj", "no why");

                    }

                }

                Log.d("jj", "rcc");
            }
        }
