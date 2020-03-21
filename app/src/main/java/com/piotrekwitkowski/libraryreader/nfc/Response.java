package com.piotrekwitkowski.libraryreader.nfc;

import java.util.Arrays;

public class Response {
    byte[] bytes;

    Response(byte[] responseBytes) {
        this.bytes = responseBytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    byte getStatus() {
        return bytes[0];
    }

    byte[] getData() {
        return Arrays.copyOfRange(bytes, 1, bytes.length);

    }
}
