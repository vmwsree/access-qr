package com.example.innov8.innov8;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.IOException;

import java.io.InputStream;
import java.lang.Runnable;
import android.os.Handler;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by horous on 26/02/16.
 */
public class NewUIStart extends AppCompatActivity {


    private final static int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothActivity blclass;
    private OutputStream outputStream;
    private InputStream inStream;
    public String teststring="Q";
    public static final String PREFS_NAME = "Innov8";
    IntentIntegrator integrator;
    public String arduino_uno="98:D3:32:10:41:E8";
    public String arduino_nano="98:D3:32:30:3A:92";
    public String arduino_bluetooth="98:D3:32:30:3A:92";
    public Boolean logoutMode =  false ;
    public int homeuiIndex = 0;
    BluetoothDevice device;


    BluetoothSocket socket;
    PowerManager.WakeLock wl;

    Runnable successRunnable,failRunnable;

public void Datasync(){
    ParseQuery<ParseObject> query = ParseQuery.getQuery("UserPlan");


    query.findInBackground(new FindCallback<ParseObject>() {
        public void done(List<ParseObject> scoreList, ParseException e) {
            if ((e == null) && (scoreList != null)) {

                Log.d("jj", "got obj: " + scoreList.toString());
                if (scoreList.size() > 0) {
                    ParseObject.pinAllInBackground(scoreList);

                } else {


                }
            }
        }
    });
    ParseQuery<ParseObject> queryw = ParseQuery.getQuery("UserTracker");


    queryw.findInBackground(new FindCallback<ParseObject>() {
        public void done(List<ParseObject> scoreList, ParseException e) {
            if ((e == null) && (scoreList != null)) {

                Log.d("jj", "got obj: " + scoreList.toString());
                if (scoreList.size() > 0) {
                    ParseObject.pinAllInBackground(scoreList);

                } else {


                }
            }
        }
    });
}
    public void writeBlue(String s){
        try {
            if (socket.isConnected()) {
                outputStream.write(s.getBytes());
            }
            else {
                initBlue();
                outputStream.write(s.getBytes());
            }
        }
        catch (IOException e){
            Log.d("EERRR", "ERRR");
        }
    }
    public void initBlue(){


        String stat="";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Boolean logout = settings.getBoolean("logoutMode", false);

        if (logout){

            arduino_bluetooth = arduino_uno;
            stat="Logout Mode, ";

        }
        else {


            arduino_bluetooth= arduino_nano;
            stat="Login Mode, ";
        }
        try {
            if (socket!=null){
                if (socket.isConnected()) {
                    socket.close();

                        Thread.sleep(2000);                 //1000 milliseconds is one second.


                }
                    //outputStream.write(teststring.getBytes());
//                    socket.close();
//                    try {
//                        Thread.sleep(1000);                 //1000 milliseconds is one second.
//                    } catch(InterruptedException ex) {
//                        Thread.currentThread().interrupt();
//                    }

              //  }
             //   else{
//                    Toast.makeText(getApplicationContext(), " GOIN NOT IS CONNECt ", Toast.LENGTH_SHORT).show();
//
//
//
//                    socket.connect();
//                    outputStream = socket.getOutputStream();
//                    inStream = socket.getInputStream();
//                    Toast.makeText(getApplicationContext(), "  connected to Door in "+ stat, Toast.LENGTH_SHORT).show();
//                    outputStream.write(teststring.getBytes());
            //    }

            }


            BluetoothDevice remoteDevice = mBluetoothAdapter.getRemoteDevice(arduino_bluetooth);
            ParcelUuid[] uuids = remoteDevice.getUuids();
            socket = remoteDevice.createRfcommSocketToServiceRecord(uuids[0].getUuid());

            socket.connect();
            outputStream = socket.getOutputStream();
                            inStream = socket.getInputStream();
                            Toast.makeText(getApplicationContext(), "  connected to Door in "+ stat+ remoteDevice.getAddress(), Toast.LENGTH_SHORT).show();
                           // outputStream.write(teststring.getBytes());

               // Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
// If there are paired devices
//            if (pairedDevices.size() > 0) {
//                // Loop through paired devices
//                for (BluetoothDevice mydevice : pairedDevices) {
//
//
//                    // Add the name and address to an array adapter to show in a ListView
//                    if (mydevice.getAddress().toString().equals(arduino_bluetooth)) {
//                         device = mydevice;
//                        ParcelUuid[] uuids = device.getUuids();
//                        Toast.makeText(getApplicationContext(), "here  " + device.getAddress(), Toast.LENGTH_SHORT).show();
//                        try {
//                            socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
//                           // Log.e("jj",e.getMessage());
//                            socket.connect();
//                            outputStream = socket.getOutputStream();
//                            inStream = socket.getInputStream();
//                            Toast.makeText(getApplicationContext(), "  connected to Door in "+ stat+ device.getAddress(), Toast.LENGTH_SHORT).show();
//                            outputStream.write(teststring.getBytes());
//                            break;
//
//                        } catch (IOException e) {
//                            Log.e("jj",e.getMessage());
//                            Toast.makeText(getApplicationContext(), " Loading  " + device.getAddress(), Toast.LENGTH_SHORT).show();
//
//                        }
//
//                    }
//                }
//            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), " BLUETOOTH ERROR Retrying Socket"+e.getMessage(), Toast.LENGTH_SHORT).show();
//

//                    try {
//
//                        Thread.sleep(1000);                 //1000 milliseconds is one second.
//                    } catch(InterruptedException ex) {
//                        Thread.currentThread().interrupt();
//                    }
//            initBlue();
        }
    }
    @Override
    protected void onDestroy() {
        try{

            wl.release();

        }
        catch (Exception e) {

        }
        super.onDestroy();
    }

    private ViewFlipper mViewFlipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.home);
        mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Boolean logout = settings.getBoolean("logoutMode", false);

        if (logout){homeuiIndex=5;}else{homeuiIndex=0;}
        mViewFlipper.setDisplayedChild(homeuiIndex);
        ImageView backgroundMain = (ImageView) findViewById(R.id.mainBg);
        ImageView settingbtn = (ImageView) findViewById(R.id.settingsBtn);
Datasync();

        Handler mHandler=new Handler();

        successRunnable=new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mViewFlipper.setDisplayedChild(0);

                //This will remove the View. and free s the space occupied by the View
            }

        };

        failRunnable=new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mViewFlipper.setDisplayedChild(3);

                //This will remove the View. and free s the space occupied by the View
            }
        };
       // blclass=new BluetoothActivity();
        settingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewUIStart.this, PasswordAccess.class);
               // intent.putExtra("QR-Code",result.getContents());
                startActivity(intent);

            }
        });
        integrator = new IntentIntegrator(this);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        wl.acquire();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        initBlue();
        startup();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
public void startup(){
    ImageView backgroundMain = (ImageView) findViewById(R.id.mainBg);
    ImageView settingbtn = (ImageView) findViewById(R.id.settingsBtn);

    settingbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(NewUIStart.this, PasswordAccess.class);
            // intent.putExtra("QR-Code",result.getContents());
            startActivity(intent);

        }
    });
    backgroundMain.setOnTouchListener(new OnSwipeTouchListener(NewUIStart.this) {

        public void onSwipeTop() {

            mViewFlipper.setDisplayedChild(1);
//integrator.setOrientationLocked()
Datasync();
            //initBlue();
            integrator.setPrompt("Scan Your Access Code");

            integrator.setCaptureActivity(CaptureActivityDummy.class);
            integrator.setOrientationLocked(false);
            integrator.setCameraId(1);  // Use a specific camera of the device
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
            // blclass.execute();

        }
        public void onSwipeBottom() {

            mViewFlipper.setDisplayedChild(1);
            Datasync();

            //initBlue();
            integrator.setPrompt("Scan Your Access Code");
            integrator.setCaptureActivity(CaptureActivityDummy.class);
            integrator.setOrientationLocked(false);
            integrator.setCameraId(1);  // Use a specific camera of the device
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
            // blclass.execute();

        }
        public void onSwipeRight() {

            mViewFlipper.setDisplayedChild(1);

            Datasync();
            //initBlue();
            integrator.setPrompt("Scan Your Access Code");

            integrator.setCaptureActivity(CaptureActivityDummy.class);
            integrator.setOrientationLocked(false);
            integrator.setCameraId(1);  // Use a specific camera of the device
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
           // blclass.execute();

        }

        public void onSwipeLeft() {

            mViewFlipper.setDisplayedChild(1);
            //initBlue();
            Datasync();

            integrator.setPrompt("Scan Your Access Code");

            integrator.setCaptureActivity(CaptureActivityDummy.class);
            integrator.setOrientationLocked(false);
            integrator.setCameraId(1);  // Use a specific camera of the device
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();

           // blclass.execute();

        }

    });


}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode!=1){

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if(result.getContents() == null) {
                    Log.d("MainActivity", "Cancelled scan");

                    mViewFlipper.setDisplayedChild(homeuiIndex);
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();

                } else {
                    Log.d("MainActivity", "Scanned");
                    //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NewUIStart.this, ParseActivity.class);
                    intent.putExtra("QR-Code",result.getContents());
                    mViewFlipper.setDisplayedChild(1);
                    startActivityForResult(intent, 1);
                    //sendData(result.getContents()); //Do this arduino thing after FireBase check
                }
            } else {
                mViewFlipper.setDisplayedChild(1);
                Log.d("MainActivity", "Weird");
                // This is important, otherwise the result will not be passed to the fragment
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

        if(requestCode==1){
           mViewFlipper.setDisplayedChild(homeuiIndex);
            //Handle FireBase Activity Resultset
            if(resultCode == Activity.RESULT_OK){

                String result=data.getStringExtra("result");
                try {

                    if (result.equals("USER_SUCCESS") ){


                        mViewFlipper.setDisplayedChild(2);

                        Handler mHandler=new Handler();
                        mHandler.postDelayed(successRunnable, 2500);


writeBlue(teststring);
                Datasync();
                        //blclass.execute();
                    }
                    else if ( result.equals("USER_SUCCESS_LOGOUT"))


                    {

                        mViewFlipper.setDisplayedChild(4);

                        writeBlue(teststring);

                        Handler mHandler=new Handler();
                        mHandler.postDelayed(successRunnable, 2500);
                      Datasync();
                       // blclass.execute();

                    }
                    else if (result.equals("USER_FAIL")){

                        mViewFlipper.setDisplayedChild(3);
                        Handler mHandler=new Handler();
                        mHandler.postDelayed(successRunnable, 2500);
                      Datasync();
                    }

                }
                catch (Exception e){
                    Toast.makeText(this, "Communication With door lost", Toast.LENGTH_LONG).show();

                    mViewFlipper.setDisplayedChild(homeuiIndex);


                }
                Toast.makeText(this, "Response code " + result, Toast.LENGTH_LONG).show();




            }

            else if (resultCode == Activity.RESULT_CANCELED) {

                mViewFlipper.setDisplayedChild(homeuiIndex);


                //
                //Write your code if there's no result
            }
            else
            {
                Toast.makeText(this, "You are not authenticated", Toast.LENGTH_LONG).show();
                mViewFlipper.setDisplayedChild(homeuiIndex);



            }

        }
    }

}

