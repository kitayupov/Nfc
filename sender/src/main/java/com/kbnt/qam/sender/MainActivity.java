package com.kbnt.qam.sender;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private NdefMessage ndefMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            System.out.println("No NFC found on this device");
            finish();
            return;
        }

        ndefMessage = new NdefMessage(new NdefRecord[]{
                NdefRecord.createUri(getHotspotJsonString()),
                NdefRecord.createUri(MainActivity.class.getPackage().toString()),
                NdefRecord.createTextRecord("en", "TextRecordEn"),
                NdefRecord.createTextRecord("ru", "ТекстРекордRu"),
                NdefRecord.createTextRecord("ру", "ТекстРекордРу"),
                NdefRecord.createUri(getJsonString()),
        });
        System.out.println(ndefMessage);
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

    private String getJsonString() {
        final JSONObject object = new JSONObject();
        try {
            object.put("key_file_path", "key_file_path")
                    .put("key_file_name", "key_file_name")
                    .put("key_file_size", "key_file_size")
                    .put("key_file_date", "key_file_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            nfcAdapter.enableForegroundNdefPush(this, ndefMessage);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundNdefPush(this);
        }
    }

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("NFC is not enabled. Please go to the wireless settings...");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
    }
}
