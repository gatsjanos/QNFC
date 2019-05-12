package com.feverkill.gatsj.qnfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        Button btn_go_back = findViewById(R.id.btn_go_back);
        btn_go_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });


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
        boolean messageWrittenSuccessfully = false;
        try
        {
            super.onNewIntent(intent);
            //boolean messageWrittenSuccessfully = NFCHelper.sendNFCMessage("Teszt Payload 123", intent);
            Uri UriToWrite = Uri.parse(Eszk.mainActivity.getEditText_text_to_writeText());
            messageWrittenSuccessfully = NFCHelper.sendNFCUri(UriToWrite, intent);
            textViewResult.setText((messageWrittenSuccessfully) ? "Successfully Written to Tag!" : "Something Went wrong. Try Again!");
        }
        catch (Exception e)
        {
            textViewResult.setText("Something Went wrong. Try Again! (" + String.valueOf(e.getMessage()) + ")");
        }
        Eszk.vibrator.vibrate(100);
    }
}

