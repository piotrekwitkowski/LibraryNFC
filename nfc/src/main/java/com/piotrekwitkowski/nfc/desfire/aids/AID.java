package com.piotrekwitkowski.nfc.desfire.aids;

import com.piotrekwitkowski.nfc.ByteUtils;

import java.util.Arrays;

public class AID {
    private final byte[] bytes;

    public AID(String aid) throws AIDWrongLengthException {
        if (aid.length() == 6) {
            this.bytes = ByteUtils.toByteArray(aid);
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

    public boolean equals(AID aid) {
        return Arrays.equals(this.bytes, aid.bytes);
    }

    public byte[] getBytes() {
        return bytes;
    }

}
