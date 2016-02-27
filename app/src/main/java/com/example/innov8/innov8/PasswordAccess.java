package com.example.innov8.innov8;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by horous on 27/02/16.
 */
public class PasswordAccess extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passcode);

        ImageView bkbtn = (ImageView) findViewById(R.id.back);
        bkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  finish();
            }
        });
        final EditText editText = (EditText) findViewById(R.id.passinput);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String pass=editText.getText().toString();
                    // the user is done typing.
                    if(pass.equals("854733")){

                        Intent intent = new Intent(PasswordAccess.this, HomeActivity.class);
                        //  intent.putExtra("QR-Code",result.getContents());
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Sorry wrong password"+editText.getText(), Toast.LENGTH_LONG).show();


                        v.setText("");
                    }
                    handled = true;
                }
                return handled;
            }
        });
//
//        ((EditText)findViewById(R.id.passinput)).setOnEditorActionListener(
//                new EditText.OnEditorActionListener() {
//                    @Override
//                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
//                                actionId == EditorInfo.IME_ACTION_DONE ||
//                                event.getAction() == KeyEvent.ACTION_DOWN &&
//                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                            if (!event.isShiftPressed()) {
//                                // the user is done typing.
//                                if(v.getText().equals("854733")){
//                                    Intent intent = new Intent(PasswordAccess.this, HomeActivity.class);
//                                  //  intent.putExtra("QR-Code",result.getContents());
//                                    startActivity(intent);
//                                }
//                                else {
//                                    v.setText("");
//
//                                    Toast.makeText(getApplicationContext(), "Sorry wrong password", Toast.LENGTH_LONG).show();
//                                }
//                                return true; // consume.
//                            }
//                        }
//                        return false; // pass on to other listeners.
//                    }
//                });


    }
}
