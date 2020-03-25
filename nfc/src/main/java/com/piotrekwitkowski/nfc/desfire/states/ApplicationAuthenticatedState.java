package com.piotrekwitkowski.nfc.desfire.states;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.desfire.Command;
import com.piotrekwitkowski.nfc.desfire.ResponseCodes;
import com.piotrekwitkowski.nfc.desfire.applications.Application;

class ApplicationAuthenticatedState extends State {
    private static final String TAG = "ApplicationAuthenticatedState";
    private final Application application;
    private final byte[] sessionKey;

    ApplicationAuthenticatedState(Application application, byte[] sessionKey) {
        this.application = application;
        this.sessionKey = sessionKey;
    }

    public CommandResult processCommand(Command command) {
        Log.i(TAG, "processCommand()");
        return new CommandResult(this, ResponseCodes.ILLEGAL_COMMAND);
    }
}

