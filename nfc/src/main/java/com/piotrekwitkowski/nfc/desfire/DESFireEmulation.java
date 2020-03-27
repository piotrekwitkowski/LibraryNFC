package com.piotrekwitkowski.nfc.desfire;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.desfire.states.CommandResult;
import com.piotrekwitkowski.nfc.desfire.states.State;
import com.piotrekwitkowski.nfc.desfire.states.InitialState;

public class DESFireEmulation {
    private static final String TAG = "DESFireEmulation";
    private State state;

    public DESFireEmulation(Application application) {
        Application[] applications = new Application[] {application};
        this.state = new InitialState(applications);
    }

    public byte[] getResponse(byte[] apdu) {
        Log.i(TAG, "getResponse()");

        CommandResult result = state.processCommand(new Command(apdu));
        state = result.getState();
        return result.getResponse();
    }

}
