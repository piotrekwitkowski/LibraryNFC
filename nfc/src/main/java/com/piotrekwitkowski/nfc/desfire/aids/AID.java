package com.piotrekwitkowski.nfc.desfire.aids;

import com.piotrekwitkowski.nfc.ByteUtils;

import java.util.Arrays;

public class AID {
    private final byte[] bytes;

    public AID(String aid) {
        this.bytes = ByteUtils.toByteArray(aid);
    }

    public boolean equals(AID aid) {
        return Arrays.equals(this.bytes, aid.bytes);
    }

    public byte[] getBytes() {
        return bytes;
    }

}
