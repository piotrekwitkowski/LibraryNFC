package com.piotrekwitkowski.nfc.desfire;

import com.piotrekwitkowski.nfc.ByteUtils;

import java.util.Arrays;

public class AID {
    private final byte[] bytes;

    public AID(String aid) throws InvalidParameterException {
        if (aid.length() == 6) {
            this.bytes = ByteUtils.toByteArray(aid);
        } else {
            throw new InvalidParameterException("AID length should be 6 chars");
        }
    }

    public AID(byte[] aid) throws InvalidParameterException {
        if (aid.length == 3) {
            this.bytes = aid;
        } else {
            throw new InvalidParameterException("AID length should be 3 bytes");
        }
    }

    public boolean equals(AID aid) {
        return Arrays.equals(this.bytes, aid.bytes);
    }

    public byte[] getBytes() {
        return bytes;
    }

}
