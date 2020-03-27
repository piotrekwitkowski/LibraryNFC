package com.piotrekwitkowski.nfc.desfire;

public class Commands {
    public static final byte SELECT_APPLICATION = (byte) 0x5A;
    public static final byte AUTHENTICATE_AES = (byte) 0xAA;
    static final byte ADDITIONAL_FRAME = (byte) 0xAF;
    public static final byte READ_DATA = (byte) 0xBD;
}
