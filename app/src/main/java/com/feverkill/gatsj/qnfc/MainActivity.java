package com.feverkill.gatsj.qnfc;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
{

    private EditText EditText_text_to_write;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Eszk.mainActivity = this;
        Eszk.vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        final Button btn_write_nfc_tag = findViewById(R.id.btn_write_nfc_tag);
        btn_write_nfc_tag.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), WriteNFCActivity.class);
                startActivity(intent);
            }
        });

        final Button btn_emulate_nfc_tag = findViewById(R.id.btn_emulate_nfc_tag);
        btn_emulate_nfc_tag.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), EmulateNFCActivity.class);
                startActivity(intent);
            }
        });

        final Button btn_scan_QRcode = findViewById(R.id.btn_scan_QRcode);
        btn_scan_QRcode.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), ScanQRCodeActivity.class);
                startActivity(intent);
            }
        });

        EditText_text_to_write = findViewById(R.id.editText_text_to_write);
    }

    void setEditText_text_to_writeText(String text)
    {
        EditText_text_to_write.setText(text);
    }
    String getEditText_text_to_writeText()
    {
        return String.valueOf(EditText_text_to_write.getText());
    }

}
