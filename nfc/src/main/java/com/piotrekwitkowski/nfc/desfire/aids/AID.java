package com.piotrekwitkowski.nfc.desfire.aids;

import java.util.Arrays;

public class AID {
    byte[] bytes;

    AID() {}

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
