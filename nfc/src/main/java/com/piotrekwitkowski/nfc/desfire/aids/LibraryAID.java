package com.piotrekwitkowski.nfc.desfire.aids;

import com.piotrekwitkowski.nfc.ByteUtils;

public class LibraryAID extends AID {
    public LibraryAID() {
        this.bytes = ByteUtils.toByteArray("015548");
    }
}
