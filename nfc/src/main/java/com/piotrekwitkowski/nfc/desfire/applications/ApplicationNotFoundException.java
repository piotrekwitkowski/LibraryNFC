package com.piotrekwitkowski.nfc.desfire.applications;

import com.piotrekwitkowski.nfc.desfire.DESFireException;

public class ApplicationNotFoundException extends DESFireException {
    ApplicationNotFoundException() {
        super("Application not found");
    }
}
