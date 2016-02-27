package com.example.innov8.innov8;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import android.content.SharedPreferences;

import com.google.zxing.integration.android.IntentIntegrator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

class BluetoothActivity extends AsyncTask<String, Void, String> {


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
    SharedPreferences settings;

    public void writeBlue(String s){
        try {

            outputStream.write(s.getBytes());
        }
        catch (IOException e){
            Log.d("EERRR", "ERRR");
        }
    }
    protected String doInBackground(String... params) {
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
                        //Toast.makeText(getApplicationContext(), "here  " + device.getAddress(), Toast.LENGTH_SHORT).show();
                        try {
                            socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                            socket.connect();
                            outputStream = socket.getOutputStream();
                            inStream = socket.getInputStream();
                         //   Toast.makeText(getApplicationContext(), "  connected to Door in "+ stat+ device.getAddress(), Toast.LENGTH_SHORT).show();
                            //outputStream.write(teststring.getBytes());
                            break;

                        } catch (IOException e) {
                            Log.e("jj", e.getMessage());
                            //Toast.makeText(getApplicationContext(), " Could not connect  " + device.getAddress(), Toast.LENGTH_SHORT).show();

                        }

                    }
                }
            }
        }
        catch (Exception e){
            //Toast.makeText(getApplicationContext(), " BLUETOOTH ERROR", Toast.LENGTH_SHORT).show();

        }
        String totla="";
        return totla;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //get sharedPreferences here
         //settings = getSharedPreferences(PREFS_NAME, 0);
    }
    protected void onProgressUpdate() {

    }

    protected void onPostExecute(Long result) {

    }
}
 