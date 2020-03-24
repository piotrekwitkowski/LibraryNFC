package com.piotrekwitkowski.nfc.desfire.aids;

import com.piotrekwitkowski.nfc.desfire.DESFireException;

public class AIDWrongLengthException extends DESFireException {
    AIDWrongLengthException() {
        super("Wrong AID length");
    }
}
