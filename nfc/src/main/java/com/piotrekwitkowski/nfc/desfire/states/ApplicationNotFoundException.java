package com.piotrekwitkowski.nfc.desfire.states;

// https://youtrack.jetbrains.com/issue/IDEA-209050
@SuppressWarnings("WeakerAccess")
class ApplicationNotFoundException extends Exception {
    ApplicationNotFoundException() {
        super("Application not found");
    }
}
