package com.feverkill.gatsj.qnfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.net.Uri;

public class WriteNFCActivity extends AppCompatActivity
{

    private NfcAdapter mNfcAdapter = null;

    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_nfc);

        textViewResult = findViewById(R.id.textViewResult);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (mNfcAdapter != null)
            NFCHelper.enableNFCInForeground(mNfcAdapter, this, this.getClass());
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (mNfcAdapter != null)
            NFCHelper.disableNFCInForeground(mNfcAdapter, this);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
//        boolean messageWrittenSuccessfully = NFCHelper.sendNFCMessage("Teszt Payload 123", intent);
        boolean messageWrittenSuccessfully = NFCHelper.sendNFCUri(Uri.parse("https://github.com/gatsjanos"), intent);

        textViewResult.setText((messageWrittenSuccessfully) ? "Successful Written to Tag!" : "Something When wrong. Try Again!");
    }
}

