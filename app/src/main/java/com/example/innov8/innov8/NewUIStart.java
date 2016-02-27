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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ImageView backgroundMain = (ImageView) findViewById(R.id.mainBg);
        ImageView settingbtn = (ImageView) findViewById(R.id.settingsBtn);

        blclass=new BluetoothActivity();
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

        public void onSwipeRight() {
            setContentView(R.layout.waiting);
            blclass.execute();

            //initBlue();
            integrator.setPrompt("Scan Your Access Code");
            integrator.setCameraId(1);  // Use a specific camera of the device
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();

        }

        public void onSwipeLeft() {

            setContentView(R.layout.waiting);
            //initBlue();

            blclass.execute();

            integrator.setPrompt("Scan Your Access Code");
            integrator.setCameraId(1);  // Use a specific camera of the device
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();

        }

    });


}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //setContentView(R.layout.waiting);
        if(requestCode!=1){

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null) {
                if(result.getContents() == null) {
                    Log.d("MainActivity", "Cancelled scan");
                    //setContentView(R.layout.home);
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("MainActivity", "Scanned");
                    //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(NewUIStart.this, ParseActivity.class);
                    intent.putExtra("QR-Code",result.getContents());
                    startActivityForResult(intent, 1);
                    //sendData(result.getContents()); //Do this arduino thing after FireBase check
                }
            } else {
                setContentView(R.layout.waiting);
                Log.d("MainActivity", "Weird");
                // This is important, otherwise the result will not be passed to the fragment
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

        if(requestCode==1){
            setContentView(R.layout.home);
            //Handle FireBase Activity Result
            if(resultCode == Activity.RESULT_OK){

                String result=data.getStringExtra("result");
                try {

                    if (result.equals("USER_SUCCESS") ){
                        setContentView(R.layout.successpage);

                        blclass.writeBlue(teststring);
                    }
                    else if ( result.equals("USER_SUCCESS_LOGOUT"))
                    {
                        setContentView(R.layout.failpage);
                        blclass.writeBlue(teststring);

                    }

                }
                catch (Exception e){
                    Toast.makeText(this, "Communication With door lost", Toast.LENGTH_LONG).show();
                    integrator.setCameraId(1);
                    setContentView(R.layout.home);
                    integrator.initiateScan();

                }
                Toast.makeText(this, "Response code " + result, Toast.LENGTH_LONG).show();
                integrator.setCameraId(1);
                setContentView(R.layout.home);
                integrator.initiateScan();


            }

            else if (resultCode == Activity.RESULT_CANCELED) {
                integrator.setCameraId(1);
                setContentView(R.layout.home);
                integrator.initiateScan();

                //
                //Write your code if there's no result
            }
            else
            {
                Toast.makeText(this, "You are not authenticated", Toast.LENGTH_LONG).show();
                setContentView(R.layout.home);
                integrator.setCameraId(1);
                integrator.initiateScan();


            }

        }
    }

}

