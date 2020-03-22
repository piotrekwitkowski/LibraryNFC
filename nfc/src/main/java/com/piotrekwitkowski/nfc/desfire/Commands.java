package com.piotrekwitkowski.nfc.desfire;

class Commands {
    static final byte SELECT_APPLICATION = (byte) 0x5A;
    static final byte AUTHENTICATE_AES = (byte) 0xAA;
    static final byte ADDITIONAL_FRAME = (byte) 0xAF;
    static final byte GET_VALUE = (byte) 0xBD;
    static final byte RESPONSE_SUCCESS = (byte) 0x00;
}
