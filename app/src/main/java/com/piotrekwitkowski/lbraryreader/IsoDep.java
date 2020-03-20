package com.piotrekwitkowski.lbraryreader;

import android.nfc.Tag;

import java.io.IOException;

class IsoDep {
    private android.nfc.tech.IsoDep mIsoDep;

    private IsoDep(android.nfc.tech.IsoDep isoDep) {
        mIsoDep = isoDep;
    }

    static IsoDep get(Tag tag) {
        return new IsoDep(android.nfc.tech.IsoDep.get(tag));
    }

    byte[] transceive(byte[] data) throws IOException {
        return mIsoDep.transceive(data);
    }

    void connect() throws IOException {
        mIsoDep.connect();
    }

    void close() throws IOException {
        mIsoDep.close();
    }
}
