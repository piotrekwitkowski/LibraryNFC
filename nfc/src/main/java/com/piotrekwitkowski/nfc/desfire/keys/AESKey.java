package com.piotrekwitkowski.nfc.desfire.keys;

import com.piotrekwitkowski.nfc.ByteUtils;
import com.piotrekwitkowski.nfc.desfire.InvalidParameterException;

public class AESKey {
    private final byte[] key;

    public AESKey(String key) throws InvalidParameterException {
        if (key.length() == 32) {
            this.key = ByteUtils.toByteArray(key);
        } else {
            throw new InvalidParameterException("AES key length should be 32 chars");
        }
    }

    public byte[] getKey() {
        return key;
    }
}
