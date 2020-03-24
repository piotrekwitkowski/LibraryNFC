package com.piotrekwitkowski.nfc.desfire.applications;

import com.piotrekwitkowski.nfc.desfire.aids.AID;

public class Applications {
    private static final Application library = new LibraryApplication();

    public Application get(AID aid) throws ApplicationNotFoundException {
        if (library.getAid().equals(aid)) {
            return library;
        } else {
            throw new ApplicationNotFoundException();
        }
    }

}
