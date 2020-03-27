package com.piotrekwitkowski.libraryhce.application;

import com.piotrekwitkowski.nfc.desfire.aids.AID;
import com.piotrekwitkowski.nfc.desfire.InvalidParameterException;

class LibraryAID extends AID {
    LibraryAID() throws InvalidParameterException {
        super("015548");
    }
}
