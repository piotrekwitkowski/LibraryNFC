package com.piotrekwitkowski.nfc.desfire.states;

import com.piotrekwitkowski.nfc.desfire.Command;

public abstract class State {
    public abstract CommandResult processCommand(Command c);
}
