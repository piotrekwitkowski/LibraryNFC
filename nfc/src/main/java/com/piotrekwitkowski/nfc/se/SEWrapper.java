package com.piotrekwitkowski.nfc.se;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.se.states.CommandResult;
import com.piotrekwitkowski.nfc.se.states.InitialState;
import com.piotrekwitkowski.nfc.se.states.State;

public class SEWrapper {
    private static final String TAG = "SoftwareSEWrapper";
    private State state;

    public SEWrapper(Application[] applications) {
        this.state = new InitialState(applications);
    }

    public byte[] processCommand(Command command) {
        Log.i(TAG, "processCommand()");

        CommandResult result = this.state.processCommand(command);
        this.state = result.getState();
        return result.getResponse();
    }

}
