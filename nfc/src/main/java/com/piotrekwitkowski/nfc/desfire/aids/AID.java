package com.piotrekwitkowski.nfc.desfire.aids;

import com.piotrekwitkowski.nfc.ByteUtils;

import java.util.Arrays;

public class AID {
    byte[] bytes;

    AID() {}

    protected AID(String byteString) throws AIDWrongLengthException {
        if (byteString.length() == 6) {
            bytes = ByteUtils.toByteArray(byteString);
        } else {
            throw new AIDWrongLengthException();
        }
    }

    public AID(byte[] aid) throws AIDWrongLengthException {
        if (aid.length == 3) {
            this.bytes = aid;
        } else {
            throw new AIDWrongLengthException();
        }
    }

    public boolean equals(byte[] bytes) {
        return Arrays.equals(this.bytes, bytes);
    }

    public boolean equals(AID aid) {
        return Arrays.equals(this.bytes, aid.bytes);
    }

    public byte[] getBytes() {
        return bytes;
    }

}
