package com.kbnt.qam.receiver_button;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;

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

        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        findViewById(R.id.buttonNfc).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        nfcAdapter.enableForegroundDispatch(MainActivity.this, pendingIntent, null, null);
                        break;
                    case MotionEvent.ACTION_UP:
                        nfcAdapter.disableForegroundDispatch(MainActivity.this);
                }
                return false;
            }
        });
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            System.out.println(action);
            if (action != null) {
                switch (action) {
                    case NfcAdapter.ACTION_NDEF_DISCOVERED:
                        resolveIntent(intent);
                        break;
                }
            }
        }
    }

    private void resolveIntent(Intent intent) {
        final Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMessages != null) {
            final NdefMessage[] messages = new NdefMessage[rawMessages.length];
            for (int i = 0; i < rawMessages.length; i++) {
                messages[i] = (NdefMessage) rawMessages[i];
            }
            System.out.println(Arrays.toString(messages));
        }
    }
}
