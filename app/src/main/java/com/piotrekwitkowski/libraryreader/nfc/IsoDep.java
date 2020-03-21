package com.piotrekwitkowski.libraryreader.nfc;

import android.nfc.Tag;
import android.util.Log;

import java.io.IOException;

public class IsoDep {
    private static final String TAG = "IsoDep";
    private final android.nfc.tech.IsoDep mIsoDep;

    private IsoDep(android.nfc.tech.IsoDep isoDep) {
        mIsoDep = isoDep;
    }

    public static IsoDep get(Tag tag) {
        return new IsoDep(android.nfc.tech.IsoDep.get(tag));
    }

    public void connect() throws IOException {
        Log.i(TAG, "connect()");
        mIsoDep.connect();
    }

    byte[] transceive(byte[] data) throws IOException {
        Log.i(TAG, "--> " + ByteUtils.toHexString(data));
        byte[] response = mIsoDep.transceive(data);
        Log.i(TAG, "<-- " + ByteUtils.toHexString(response));
        return response;
    }

    public void close() throws IOException {
        Log.i(TAG, "close()");
        mIsoDep.close();
    }

    public byte[] getHistoricalBytes() {
        Log.i(TAG, "getHistoricalBytes()");
        return mIsoDep.getHistoricalBytes();
    }

}
