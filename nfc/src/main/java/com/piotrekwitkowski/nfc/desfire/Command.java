package com.piotrekwitkowski.nfc.desfire;

import java.util.Arrays;

public class Command {
    private byte[] bytes;

    Command(byte[] bytes) {
        this.bytes = bytes;
    }

    byte[] getBytes() {
        return bytes;
    }

    public byte getCode() {
        return bytes[0];
    }

    public byte[] getData() {
        return Arrays.copyOfRange(bytes, 1, bytes.length);
    }
}
