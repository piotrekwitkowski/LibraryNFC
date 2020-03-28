package com.piotrekwitkowski.nfc.desfire;

import com.piotrekwitkowski.nfc.ByteUtils;

import java.util.Arrays;

public class File {
    private final byte[] data;

    @SuppressWarnings("SameParameterValue")
    protected File(String data) {
        this.data = ByteUtils.toByteArray(data);
    }

    public byte[] readData(int offset, int length) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NullPointerException {
        if (length == 0) {
            return Arrays.copyOfRange(data, offset, data.length);
        } else {
            return Arrays.copyOfRange(data, offset, offset + length);
        }
    }
}
