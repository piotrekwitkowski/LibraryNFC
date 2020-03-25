package com.piotrekwitkowski.nfc.desfire.aids;

import com.piotrekwitkowski.nfc.desfire.DESFireException;

@SuppressWarnings("WeakerAccess")
// https://youtrack.jetbrains.com/issue/IDEA-209050
public class AIDWrongLengthException extends DESFireException {
    AIDWrongLengthException() {
        super("Wrong AID length");
    }
}
