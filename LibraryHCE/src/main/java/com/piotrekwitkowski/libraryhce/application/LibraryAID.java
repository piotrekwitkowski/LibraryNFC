package com.piotrekwitkowski.libraryhce.application;

import com.piotrekwitkowski.nfc.desfire.aids.AID;
import com.piotrekwitkowski.nfc.desfire.aids.AIDWrongLengthException;

class LibraryAID extends AID {
    LibraryAID() throws AIDWrongLengthException {
        super("015548");
    }
}
