package com.piotrekwitkowski.nfc.se.states;

// https://youtrack.jetbrains.com/issue/IDEA-209050
@SuppressWarnings("WeakerAccess")
public class ApplicationNotFoundException extends Exception {
    public ApplicationNotFoundException() {
        super("Application not found");
    }
}
