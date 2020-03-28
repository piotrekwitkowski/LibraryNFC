package com.piotrekwitkowski.libraryhce.application;

import com.piotrekwitkowski.nfc.desfire.InvalidParameterException;
import com.piotrekwitkowski.nfc.se.Application;

public class LibraryApplication extends Application {
    public LibraryApplication() throws InvalidParameterException {
        super(new LibraryAID(), new LibraryAESKey0(), new LibraryFile0());
    }
}
