package com.piotrekwitkowski.nfc.desfire.states;

import com.piotrekwitkowski.nfc.desfire.Command;
import com.piotrekwitkowski.nfc.desfire.DESFireException;

public abstract class State {
    public abstract CommandResult processCommand(Command c) throws DESFireException;
}
