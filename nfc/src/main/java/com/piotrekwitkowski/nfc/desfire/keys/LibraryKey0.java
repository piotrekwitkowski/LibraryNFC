package com.piotrekwitkowski.nfc.desfire.keys;

import com.piotrekwitkowski.nfc.AESKey;

public class LibraryKey0 extends ApplicationKey {
    public LibraryKey0() {
        this.keyNumber = (byte) 0x00;
        this.key = new AESKey("00000000000000000000000000000000");
    }
}
