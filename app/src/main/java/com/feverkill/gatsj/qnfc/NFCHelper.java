package com.feverkill.gatsj.qnfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;

import java.io.IOException;
import java.lang.reflect.Type;

public class NFCHelper
{

    static boolean sendNFCMessage(String payload, Intent intent)
    {
        String pathPrefix = "feverkill.com:qnfc";

        byte bpathPrefixbuff[] = new byte[pathPrefix.length()];
        for (int i = 0; i < bpathPrefixbuff.length; ++i)
            bpathPrefixbuff[i] = (byte) pathPrefix.charAt(i);


        byte bpayloadbuff[] = new byte[payload.length()];
        for (int i = 0; i < bpayloadbuff.length; ++i)
            bpayloadbuff[i] = (byte) payload.charAt(i);

        byte barray[] = null;
        NdefRecord nfcRecord = new NdefRecord(NdefRecord.TNF_EXTERNAL_TYPE, bpathPrefixbuff, barray, bpayloadbuff);

        NdefMessage nfcMessage = new NdefMessage(nfcRecord);

        if (intent != null)
        {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            return writeMessageToTag(nfcMessage, tag);
        }
        return false;
    }

    static boolean sendNFCUri(Uri uri, Intent intent)
    {
        NdefRecord nfcRecord = NdefRecord.createUri(uri);

        NdefMessage nfcMessage = new NdefMessage(nfcRecord);

        if (intent != null)
        {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            return writeMessageToTag(nfcMessage, tag);
        }
        return false;
    }

    static boolean writeMessageToTag(NdefMessage nfcMessage, Tag tag)
    {
        try
        {
            Ndef nDefTag = Ndef.get(tag);

            if (nDefTag != null)
            {
                nDefTag.connect();
                if (nDefTag.getMaxSize() < nfcMessage.toByteArray().length)
                {
                    //Message to large to write to NFC tag
                    return false;
                }
                if (nDefTag.isWritable())
                {
                    nDefTag.writeNdefMessage(nfcMessage);
                    nDefTag.close();
                    //Message is written to tag
                    return true;
                }
                else
                {
                    //NFC tag is read-only
                    return false;
                }
            }

            NdefFormatable nDefFormatableTag = NdefFormatable.get(tag);

            if (nDefFormatableTag != null)
            {
                try
                {
                    nDefFormatableTag.connect();
                    nDefFormatableTag.format(nfcMessage);
                    nDefFormatableTag.close();
                    //The data is written to the tag
                    return true;
                }
                catch (IOException e)
                {
                    //Failed to format tag
                    return false;
                }
            }
            //NDEF is not supported
            return false;

        }
        catch (Exception e)
        {
            //Write operation has failed
        }
        return false;
    }

    static <T> void enableNFCInForeground(NfcAdapter nfcAdapter, Activity activity, Class<T> classType)
    {
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, classType).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter nfcIntentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter filters[] = new IntentFilter[]{nfcIntentFilter};

        String TechLists[][] = new String[2][];
        TechLists[0] = new String[]{Ndef.class.getName()};
        TechLists[1] = new String[]{NdefFormatable.class.getName()};

        nfcAdapter.enableForegroundDispatch(activity, pendingIntent, filters, TechLists);
    }

    static void disableNFCInForeground(NfcAdapter nfcAdapter, Activity activity)
    {
        nfcAdapter.disableForegroundDispatch(activity);
    }
}
