package com.piotrekwitkowski.nfc.desfire.applications;

import com.piotrekwitkowski.nfc.desfire.aids.LibraryAID;
import com.piotrekwitkowski.nfc.desfire.keys.LibraryAESKey0;

class LibraryApplication extends Application {
    LibraryApplication() {
        super(new LibraryAID(), new LibraryAESKey0());
    }
}
