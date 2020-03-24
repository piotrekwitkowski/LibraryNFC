package com.piotrekwitkowski.nfc.desfire.keys;

import com.piotrekwitkowski.nfc.ByteUtils;

public class AESKey {
    private byte[] key;

    public AESKey(String key) {
        this.key = ByteUtils.toByteArray(key);
    }

    public byte[] getKey() {
        return key;
    }
}
