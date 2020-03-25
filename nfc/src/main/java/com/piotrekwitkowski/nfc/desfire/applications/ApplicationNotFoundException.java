package com.piotrekwitkowski.nfc.desfire.applications;

import com.piotrekwitkowski.nfc.desfire.DESFireException;

@SuppressWarnings("WeakerAccess")
// https://youtrack.jetbrains.com/issue/IDEA-209050
public class ApplicationNotFoundException extends DESFireException {
    ApplicationNotFoundException() {
        super("Application not found");
    }
}
