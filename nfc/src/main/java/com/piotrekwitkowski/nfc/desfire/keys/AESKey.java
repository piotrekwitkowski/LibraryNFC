package com.piotrekwitkowski.nfc.desfire.keys;

import com.piotrekwitkowski.nfc.ByteUtils;

public class AESKey {
    public static final int AES_KEY_LENGTH = 16;
    private final byte[] key;

    public AESKey(String key) {
        this.key = ByteUtils.toByteArray(key);
    }

    public byte[] getKey() {
        return key;
    }
}
