package com.piotrekwitkowski.nfc.se.states;

import com.piotrekwitkowski.nfc.se.Command;

public abstract class State {
    public abstract CommandResult processCommand(Command c);
}
