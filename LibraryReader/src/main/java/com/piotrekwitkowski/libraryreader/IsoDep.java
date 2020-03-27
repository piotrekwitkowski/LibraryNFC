package com.piotrekwitkowski.libraryreader;

import android.nfc.Tag;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.ByteUtils;

import java.io.IOException;

class IsoDep {
    private static final String TAG = "IsoDep";
    private final android.nfc.tech.IsoDep mIsoDep;

    private IsoDep(android.nfc.tech.IsoDep isoDep) {
        mIsoDep = isoDep;
    }

    static IsoDep get(Tag tag) {
        return new IsoDep(android.nfc.tech.IsoDep.get(tag));
    }

    void connect() throws IOException {
        Log.i(TAG, "connect()");
        mIsoDep.connect();
    }

    Response transceive(byte command, byte data) throws IOException {
        return transceive(ByteUtils.concatenate(command, data));
    }

    Response transceive(byte command, byte[] data) throws IOException {
        return transceive(ByteUtils.concatenate(command, data));
    }

    Response transceive(byte[] data) throws IOException {
        Log.i(TAG, "--> " + ByteUtils.toHexString(data));
        byte[] response = mIsoDep.transceive(data);
        Log.i(TAG, "<-- " + ByteUtils.toHexString(response));
        return new Response(response);
    }

    void close() throws IOException {
        Log.i(TAG, "close()");
        mIsoDep.close();
    }

    byte[] getHistoricalBytes() {
        Log.i(TAG, "getHistoricalBytes()");
        return mIsoDep.getHistoricalBytes();
    }

}
