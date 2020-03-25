package com.piotrekwitkowski.nfc.desfire.applications;

import com.piotrekwitkowski.nfc.desfire.aids.LibraryAID;
import com.piotrekwitkowski.nfc.desfire.keys.LibraryKey0;

class LibraryApplication extends Application {
    LibraryApplication() {
        aid = new LibraryAID();
        this.applicationKey = new LibraryKey0();
    }
}
