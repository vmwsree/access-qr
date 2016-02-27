package com.example.innov8.innov8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

//import com.firebase.client.DataSnapshot;
//import com.firebase.client.Firebase;
//import com.firebase.client.FirebaseError;
//import com.firebase.client.ServerValue;
//import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by athulsuresh on 21/12/15.
 */
public class FirebaseActivity extends AppCompatActivity{
//    Intent returnIntent;
//    Firebase myFirebaseRef;
//    String uid;
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        setContentView(R.layout.camera);
//        Firebase.setAndroidContext(getApplicationContext());
//
//         myFirebaseRef = new Firebase("https://innov8login.firebaseio.com/users");

//        myFirebaseRef.child("message").setValue("Do you have data? You'll love Firebase.");
//
//        myFirebaseRef.child("message").addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
//                Toast.makeText(getApplicationContext(), "dATA CHANGED port: ", Toast.LENGTH_LONG).show();
//
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError error) {
//                Toast.makeText(getApplicationContext(), "DATA CANCEL port: ", Toast.LENGTH_LONG).show();
//
//            }
//
//        });

//         returnIntent = getIntent();
//        uid=returnIntent.getStringExtra("QR-Code");
//        Toast.makeText(getApplicationContext(),uid, Toast.LENGTH_SHORT).show();
//        myFirebaseRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                if (snapshot.exists() && ((Long)snapshot.child("state").getValue() == 1)?false:true ) {
//                    Firebase alanRef = myFirebaseRef.child(uid);
//                    Map<String, Object> nickname = new HashMap<String, Object>();
//                    Long tsLong = System.currentTimeMillis() / 1000;
//                    String ts = tsLong.toString();
//                    nickname.put("lastlogin", ServerValue.TIMESTAMP);
//                    nickname.put("state",1);
//
//                    Long timeallot =(Long) snapshot.child("timeAllotted").getValue();
//                    Long timespent = (Long) snapshot.child("timeSpent").getValue();
//                    if(timespent>timeallot){
//                        Toast.makeText(getApplicationContext(), "You have Run out of Credits. Please Recharge ", Toast.LENGTH_SHORT).show();
//                        returnIntent.putExtra("result", "CREDIT_FAIL");
//                        setResult(Activity.RESULT_OK, returnIntent);
//                        finish();
//                    }
//                    else {
//
//                        alanRef.updateChildren(nickname);
//                        Toast.makeText(getApplicationContext(), "You are logged in successfully ", Toast.LENGTH_SHORT).show();
//                        returnIntent.putExtra("result", "USER_SUCCESS");
//                        setResult(Activity.RESULT_OK, returnIntent);
//                        finish();
//
//                    }
//
//                } else {
//                    Toast.makeText(getApplicationContext(), "Invalid User, Or YOu are already logged out", Toast.LENGTH_SHORT).show();
//                    returnIntent.putExtra("result", "INVALID_ATTEMPT");
//                    setResult(Activity.RESULT_OK, returnIntent);
//                    finish();
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError error) {
//                Toast.makeText(getApplicationContext(), "Auth failed ", Toast.LENGTH_SHORT).show();
//
//            }
//
//        });
//    }
}
