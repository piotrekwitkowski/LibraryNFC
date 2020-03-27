package com.piotrekwitkowski.libraryhce.application;

import com.piotrekwitkowski.nfc.desfire.InvalidParameterException;
import com.piotrekwitkowski.nfc.desfire.keys.AESKey;

class LibraryAESKey0 extends AESKey {
    LibraryAESKey0() throws InvalidParameterException {
        super("00000000000000000000000000000000");
    }
}
