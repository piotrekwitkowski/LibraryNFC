package com.piotrekwitkowski.nfc.desfire.states;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.desfire.Command;
import com.piotrekwitkowski.nfc.desfire.ResponseCodes;
import com.piotrekwitkowski.nfc.desfire.applications.Application;

public class AuthenticationInProgressState extends State {
    private static final String TAG = "AuthenticationInProgressState";
    private final Application application;
    private final byte[] B;
    private final byte[] cardChallenge;

    public AuthenticationInProgressState(Application application, byte[] B, byte[] challenge) {
        this.application = application;
        this.B = B;
        this.cardChallenge = challenge;
    }

    public CommandResult processCommand(Command command) {
        Log.i(TAG, "processCommand()");

        if (command.getCode() == ResponseCodes.ADDITIONAL_FRAME) {
            return proceedAuthentication(command.getData());
        } else {
            return new CommandResult(this, ResponseCodes.ILLEGAL_COMMAND);
        }
    }

    private CommandResult proceedAuthentication(byte[] data) {
        return null;
    }
}
