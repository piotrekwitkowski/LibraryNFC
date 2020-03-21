package com.piotrekwitkowski.lbraryreader.nfc;

import android.util.Log;

import java.io.IOException;

public class DESFire {
    private static final String TAG = "DESFire";

    private final static byte SELECT_APPLICATION = (byte) 0x5A;

    public static byte[] selectApplication(IsoDep isoDep, byte[] aid) throws IOException {
        Log.i(TAG, "selectApplication()");
        byte[] command = ByteUtils.concatenate(SELECT_APPLICATION, aid);
        return isoDep.transceive(command);
    }
}
