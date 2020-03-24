package com.piotrekwitkowski.nfc.desfire.keys;

import com.piotrekwitkowski.nfc.AESKey;

public abstract class ApplicationKey {
    byte keyNumber;
    AESKey key;

    public byte getKeyNumber() {
        return keyNumber;
    }

    public AESKey getKey() {
        return key;
    }

}
