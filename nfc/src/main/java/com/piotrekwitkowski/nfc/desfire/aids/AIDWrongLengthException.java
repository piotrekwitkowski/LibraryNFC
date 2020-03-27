package com.piotrekwitkowski.nfc.desfire.aids;

// https://youtrack.jetbrains.com/issue/IDEA-209050
@SuppressWarnings("WeakerAccess")
public class AIDWrongLengthException extends Exception {
    AIDWrongLengthException() {
        super("Wrong AID length");
    }
}
