package com.piotrekwitkowski.nfc.desfire.keys;

public class ApplicationKey {
    private final AESKey aesKey;
    private final byte keyNumber;

    ApplicationKey(AESKey aesKey, int keyNumber) {
        this.aesKey = aesKey;
        this.keyNumber = (byte) keyNumber;
    }

    public AESKey getAESKey() {
        return aesKey;
    }

    public byte getKeyNumber() {
        return keyNumber;
    }
}
