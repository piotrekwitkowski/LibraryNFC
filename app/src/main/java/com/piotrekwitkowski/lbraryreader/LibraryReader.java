package com.piotrekwitkowski.lbraryreader;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

import java.io.IOException;

class LibraryReader {
    private String TAG = "LibraryReader";

    void processTag(Tag tag) {
        Log.i(TAG, "processTag called");
        IsoDep isoDep = IsoDep.get(tag);
        try {
            isoDep.connect();
            byte[] response = isoDep.transceive(ByteUtils.hexStringToByteArray("00A4040007A0000002471001"));
            Log.i(TAG, "response: " + ByteUtils.byteArrayToHexString(response));
            isoDep.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
