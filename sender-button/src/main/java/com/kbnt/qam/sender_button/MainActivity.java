package com.kbnt.qam.sender_button;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            System.out.println("No NFC found on this device");
            finish();
        }

        findViewById(R.id.buttonNfc).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        final NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{
                                NdefRecord.createUri(getHotspotJsonString())
                        });
                        nfcAdapter.enableForegroundNdefPush(MainActivity.this, ndefMessage);
                        break;
                    case MotionEvent.ACTION_UP:
                        nfcAdapter.disableForegroundNdefPush(MainActivity.this);
                        break;
                }
                return false;
            }
        });
    }

    private String getHotspotJsonString() {
        final JSONObject object = new JSONObject();
        try {
            object.put("hotspot_ssid", "VP-01")
                    .put("hotspot_is_open", "true")
                    .put("hotspot_password", "12345678")
                    .put("hotspot_is_hidden", "false");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}
