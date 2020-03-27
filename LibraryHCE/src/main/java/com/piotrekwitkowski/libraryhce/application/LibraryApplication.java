package com.piotrekwitkowski.libraryhce.application;

import com.piotrekwitkowski.nfc.desfire.aids.AIDWrongLengthException;
import com.piotrekwitkowski.nfc.desfire.applications.Application;

public class LibraryApplication extends Application {
    public LibraryApplication() throws AIDWrongLengthException {
        super(new LibraryAID(), new LibraryAESKey0());
    }
}
