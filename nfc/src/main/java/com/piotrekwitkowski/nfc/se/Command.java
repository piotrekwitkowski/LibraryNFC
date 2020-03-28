package com.piotrekwitkowski.nfc.se;

import java.util.Arrays;

public class Command {
    private final byte[] bytes;

    Command(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte getCode() {
        return bytes[0];
    }

    public byte[] getData() {
        return Arrays.copyOfRange(bytes, 1, bytes.length);
    }
}
