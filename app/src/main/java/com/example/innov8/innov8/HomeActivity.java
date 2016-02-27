package com.example.innov8.innov8;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ParcelUuid;

import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

/**
 * Created by horous on 20/01/16.
 */
public class HomeActivity extends AppCompatActivity {
    private final static int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter mBluetoothAdapter;
    private OutputStream outputStream;
    private InputStream inStream;
    public String teststring="Q";
    public static final String PREFS_NAME = "Innov8";
    IntentIntegrator integrator;
    public String arduino_uno="98:D3:32:10:41:E8";
    public String arduino_nano="98:D3:32:30:3A:92";
    public String arduino_bluetooth="98:D3:32:30:3A:92";
    public Boolean logoutMode =  false ;
    BluetoothSocket socket;
    PowerManager.WakeLock wl;

    @Override
    protected void onDestroy() {
        try{

            wl.release();

        }
        catch (Exception e) {

        }
        super.onDestroy();
    }

    public void writeBlue(String s){
     try {

         outputStream.write(s.getBytes());
     }
     catch (IOException e){
         Log.d("EERRR", "ERRR");
     }
 }
    public void initBlue(){
        try {
            String stat="";
            if (socket!=null){
                if (socket.isConnected()) {
                    socket.close();
                    try {
                        Thread.sleep(2000);                 //1000 milliseconds is one second.
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }

                }

            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
// If there are paired devices
            if (pairedDevices.size() > 0) {
                // Loop through paired devices
                for (BluetoothDevice mydevice : pairedDevices) {

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
                    // Add the name and address to an array adapter to show in a ListView
                    if (mydevice.getAddress().toString().equals(arduino_bluetooth)) {
                        BluetoothDevice device = mydevice;
                        ParcelUuid[] uuids = device.getUuids();
                        Toast.makeText(getApplicationContext(), "here  " + device.getAddress(), Toast.LENGTH_SHORT).show();
                        try {
                             socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                            socket.connect();
                            outputStream = socket.getOutputStream();
                            inStream = socket.getInputStream();
                            Toast.makeText(getApplicationContext(), "  connected to Door in "+ stat+ device.getAddress(), Toast.LENGTH_SHORT).show();
                            //outputStream.write(teststring.getBytes());
                            break;

                        } catch (IOException e) {
                            Log.e("jj",e.getMessage());
                            Toast.makeText(getApplicationContext(), " Could not connect  " + device.getAddress(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }
            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), " BLUETOOTH ERROR", Toast.LENGTH_SHORT).show();

        }
    }
    public void showDialog()
    {

        LayoutInflater li = LayoutInflater.from(HomeActivity.this);
        View promptsView = li.inflate(R.layout.searchprompt, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.user_input);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Go",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                /** DO THE METHOD HERE WHEN PROCEED IS CLICKED*/
                                String user_text = (userInput.getText()).toString();

                                /** CHECK FOR USER'S INPUT **/
                                if (user_text.equals("renuravi01")) {
                                    Log.d(user_text, "HELLO THIS IS THE MESSAGE CAUGHT :)");
                                    writeBlue(teststring);

                                } else if (user_text.equals("#startbuildgrow")) {
                                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                                } else {
                                    Log.d(user_text, "string is empty");
                                    String message = "The password you have entered is incorrect." + " \n \n" + "Please try again!";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                                    builder.setTitle("Error");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("Cancel", null);
                                    builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            showDialog();
                                        }
                                    });
                                    builder.create().show();

                                }
                            }
                        })
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }

                        }

                );

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.innov8_new_ble);
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
        Button blEbutton = (Button) findViewById(R.id.startBle);
        Button settingsBtn = (Button) findViewById(R.id.settingsBtn);
        Button testBtn = (Button) findViewById(R.id.testBtn);
        Button scanButton = (Button) findViewById(R.id.scanBtn);
        Button inetButton = (Button) findViewById(R.id.interet);
        Button syncButton = (Button) findViewById(R.id.syncBtn);

        ToggleButton toggleBtn = (ToggleButton) findViewById(R.id.toggleBtn);

        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                //integrator.setDesiredBarcodeFoprmats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan a barcode");
                integrator.setCameraId(1);  // Use a specific camera of the device
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });
        inetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.cyberoam.corporateclient");
                startActivity(launchIntent);
            }
        });
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                    //writeBlue(teststring);


            }
        });
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("UserTracker");

                //ParseObject.pinAllInBackground(query);


                query.addDescendingOrder("createdAt");
                //query.fromLocalDatastore();

                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> scoreList, ParseException e) {

                        ParseObject.unpinAllInBackground(scoreList);
                        ParseObject.saveAllInBackground(scoreList);
                        ParseObject.pinAllInBackground(scoreList);
                    }});
                    //writeBlue(teststring);

                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("UserPlan");

                //ParseObject.pinAllInBackground(query);


                query1.addDescendingOrder("createdAt");
                //query.fromLocalDatastore();

                query1.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> scoreList, ParseException e) {
                        ParseObject.unpinAllInBackground(scoreList);
                        ParseObject.saveAllInBackground(scoreList);

                        ParseObject.pinAllInBackground(scoreList);


                    }});
                //writeBlue(teststring);



            }
            });
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });
        blEbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initBlue();
            }
        });
        toggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("logoutMode", isChecked);

                // Commit the edits!
                editor.commit();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode!=1){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null) {
                if(result.getContents() == null) {
                    Log.d("MainActivity", "Cancelled scan");
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("MainActivity", "Scanned");
                    //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(HomeActivity.this, ParseActivity.class);
                    intent.putExtra("QR-Code",result.getContents());
                    startActivityForResult(intent, 1);
                    //sendData(result.getContents()); //Do this arduino thing after FireBase check
                }
            } else {
                Log.d("MainActivity", "Weird");
                // This is important, otherwise the result will not be passed to the fragment
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

        if(requestCode==1){
            //Handle FireBase Activity Result
            if(resultCode == Activity.RESULT_OK){

                String result=data.getStringExtra("result");
                try {

                    if (result.equals("USER_SUCCESS") ){

                        writeBlue(teststring);
                    }
                    else if ( result.equals("USER_SUCCESS_LOGOUT"))
                    {
                        writeBlue(teststring);

                    }

                }
                catch (Exception e){
                    Toast.makeText(this, "Communication With door lost", Toast.LENGTH_LONG).show();
                    integrator.setCameraId(1);
                    integrator.initiateScan();

                }
                    Toast.makeText(this, "Response code " + result, Toast.LENGTH_LONG).show();
                    integrator.setCameraId(1);
                    integrator.initiateScan();


            }

            else if (resultCode == Activity.RESULT_CANCELED) {
                integrator.setCameraId(1);
                integrator.initiateScan();

                //
                //Write your code if there's no result
            }
            else
            {
                Toast.makeText(this, "You are not authenticated", Toast.LENGTH_LONG).show();
                integrator.setCameraId(1);
                integrator.initiateScan();


            }

        }
    }

}
