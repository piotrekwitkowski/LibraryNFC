package com.piotrekwitkowski.libraryreader.nfc;

public class AESKey {
    private byte[] key;

    public AESKey(String key) {
        this.key = ByteUtils.toByteArray(key);
    }

    public byte[] getKey() {
        return key;
    }
}
