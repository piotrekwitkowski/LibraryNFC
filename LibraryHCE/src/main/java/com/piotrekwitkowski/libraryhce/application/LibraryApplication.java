package com.piotrekwitkowski.libraryhce.application;

import com.piotrekwitkowski.nfc.desfire.InvalidParameterException;
import com.piotrekwitkowski.nfc.desfire.applications.Application;

public class LibraryApplication extends Application {
    public LibraryApplication() throws InvalidParameterException {
        super(new LibraryAID(), new LibraryAESKey0());
    }
}
