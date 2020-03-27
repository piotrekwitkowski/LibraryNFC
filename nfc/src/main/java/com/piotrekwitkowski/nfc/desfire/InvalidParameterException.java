package com.piotrekwitkowski.nfc.desfire;

// https://youtrack.jetbrains.com/issue/IDEA-209050
@SuppressWarnings("WeakerAccess")
public class InvalidParameterException extends Exception {
    public InvalidParameterException(String message) {
        super(message);
    }
}
